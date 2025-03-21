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
    Dotenv dotenv;
    private String EMAIL_ADDRESS;
    private String EMAIL_PASSWORD;

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
        try
        {
            dotenv = Dotenv.load();// Loads environment variables from the .env file
            EMAIL_ADDRESS = dotenv.get("EMAIL_ADDRESS");// Email address for sending notifications
            EMAIL_PASSWORD = dotenv.get("EMAIL_PASSWORD");// Password for the email account
        }
        catch (Exception e)
        {
            System.out.println("Error loading environment variables: " + e.getMessage());
        }
    }

    /**
     * Sends an email notification to the specified recipient.
     *
     * @param recipientEmail Recipient's email address.
     * @param subject        Subject of the email.
     * @param content        Content of the email.
     */
    public void sendEmail(String recipientEmail, String subject, String content)
    {
        Session session;

        // Email signature
        String signature = "<br><br>BLib 4 - Advanced Library Management System<br>" +
                "Efficient management of books, subscribers, and library data<br><br>" +
                "Team: Group 4<br>" +
                "Email: blib.4.braude@gmail.com<br>" +
                "Phone: +972-528797979<br><br>" +
                "\"BLib 4 - The smart and simple way to manage your library.\"";

        // Combine the content with the signature
        String fullContent = content + signature;

        try
        {
            // Configure email server properties
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");// Enable SMTP authentication
            properties.put("mail.smtp.starttls.enable", "true");// Enable STARTTLS
            properties.put("mail.smtp.host", "smtp.gmail.com");// SMTP server address
            properties.put("mail.smtp.port", "587");// SMTP server port
            properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

            // Create a session with an authenticator
            session = Session.getInstance(properties, new Authenticator()
            {
                @Override
                protected PasswordAuthentication getPasswordAuthentication()
                {
                    return new PasswordAuthentication(EMAIL_ADDRESS, EMAIL_PASSWORD);
                }
            });
        }
        catch (Exception e)
        {
            System.out.println("Error configuring email server properties: " + e.getMessage());
            return;
        }

        try
        {
            // Create a MimeMessage
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_ADDRESS));// Set sender's email address
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));// Set recipient(s)
            message.setSubject(subject);// Set email subject

            // Set the email content as HTML for rich formatting
            message.setContent(fullContent, "text/html; charset=utf-8");

            // Send the email using the Java Mail API
            Transport.send(message);
            System.out.println("Email sent successfully to " + recipientEmail);
        }
        catch (MessagingException e)
        {
            System.out.println("Error sending email: " + e.getMessage());
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