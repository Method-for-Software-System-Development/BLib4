package logic;

import io.github.cdimascio.dotenv.Dotenv;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Notification_Controller
{
    private static volatile Notification_Controller instance = null;

    // Email credentials
    Dotenv dotenv = Dotenv.load();
    private final String EMAIL_ADDRESS = dotenv.get("EMAIL_ADDRESS");
    private final String EMAIL_PASSWORD = dotenv.get("EMAIL_PASSWORD");

    /**
     * Singleton instance getter.
     *
     * @return - The singleton instance of Notification_Controller.
     */
    public static Notification_Controller getInstance()
    {
        if (instance == null)
        {
            synchronized (Notification_Controller.class)
            {
                if (instance == null)
                {
                    instance = new Notification_Controller();
                }
            }
        }
        return instance;
    }

    /**
     * Private constructor for Notification_Controller.
     */
    private Notification_Controller()
    {

    }

    /**
     * Sends an email notification.
     *
     * @param recipientEmail Recipient's email address.
     * @param subject        Subject of the email.
     * @param content        Content of the email.
     */
    public void sendEmail(String recipientEmail, String subject, String content)
    {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator()
        {
            @Override
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(EMAIL_ADDRESS, EMAIL_PASSWORD);
            }
        });

        try
        {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_ADDRESS));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);
            System.out.println("Email sent successfully to " + recipientEmail);
        }
        catch (MessagingException e)
        {
            System.out.println("Failed to send email to " + recipientEmail);
        }
    }

    /**
     * Sends a day-before return reminder via email and SMS.
     *
     * @param subscriberID The subscriber's ID.
     * @param email        The subscriber's email.
     * @param message      The message to send.
     */
    public void dayBeforeReturnDate_Email_SMS(String subscriberID, String email, String message)
    {
        // Send Email
        sendEmail(email, "Reminder: Book Due Tomorrow", message);

        // Simulate send SMS to subscriber
        ServerController.HandleSendSmsToSubscriber(subscriberID, message);

        System.out.println("Notification sent to subscriberID: " + subscriberID);
    }
}