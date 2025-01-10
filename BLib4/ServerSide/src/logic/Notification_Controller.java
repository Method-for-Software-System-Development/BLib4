package logic;
import io.github.cdimascio.dotenv.Dotenv;
import entities.logic.MessageType;
import entities.logic.Notification;
import entities.user.Subscriber;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.*;
import java.util.Map;
import java.util.Properties;

public class Notification_Controller {
    private Notification notification;
    private dbController dbController;

    // Email credentials
    Dotenv dotenv = Dotenv.load();
    private final String EMAIL_ADDRESS =dotenv.get("EMAIL_ADDRESS");
    private final String EMAIL_PASSWORD = dotenv.get("EMAIL_PASSWORD");

    public Notification_Controller(Notification notification) {
        this.notification = notification;
        this.dbController = logic.dbController.getInstance();
    }

    /**
     * Sends an email notification.
     *
     * @param recipientEmail Recipient's email address.
     * @param subject        Subject of the email.
     * @param content        Content of the email.
     */
    public void sendEmail(String recipientEmail, String subject, String content) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_ADDRESS, EMAIL_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_ADDRESS));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);
            System.out.println("Email sent successfully to " + recipientEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a day-before return reminder via email and SMS.
     *
     * @param subscriberID The subscriber's ID.
     * @param email        The subscriber's email.
     * @param phoneNumber  The subscriber's phone number.
     */
    public void dayBeforeReturnDate_Email_SMS(String subscriberID, String email, String phoneNumber) {
        String message = notification.getContent();

        // Send Email
        sendEmail(email, "Reminder: Book Due Tomorrow", message);

        System.out.println("Notification sent to subscriberID: " + subscriberID);
    }
    /**
     * Sends a notification to a subscriber to confirm that their extension request has been approved.
     *
     * @param subscriberID The ID of the subscriber.
     * @param copyID The ID of the book copy being extended.
     */
    public void extensionApproval(String subscriberID, String copyID) {
        try {
            // Fetch the subscriber's name and book title from DBController
            Map<String, String> orderDetails = dbController.fetchOrderDetails(subscriberID);
            String subscriberName = orderDetails.getOrDefault("subscriberName", "Subscriber");
            String bookTitle = orderDetails.getOrDefault("bookTitle", "Unknown Book");

            String message = "Dear " + subscriberName + ", your request to extend the loan period for the book \"" + bookTitle + "\" (ID: " + copyID + ") has been approved.";
            System.out.println("Sending approval notification to subscriberID: " + subscriberID);
            System.out.println("Message: " + message);

            // Simulate sending notification (e.g., email or SMS)
        } catch (Exception e) {
            System.err.println("Error while sending approval notification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Sends a notification to a subscriber to inform them that their extension request has been rejected.
     *
     * @param subscriberID The ID of the subscriber.
     * @param copyID The ID of the book copy for which the extension was requested.
     */
    public void extensionRejection(String subscriberID, String copyID) {
        try {
            // Fetch the subscriber's name and book title from DBController
            Map<String, String> orderDetails = dbController.fetchOrderDetails(subscriberID);
            String subscriberName = orderDetails.getOrDefault("subscriberName", "Subscriber");
            String bookTitle = orderDetails.getOrDefault("bookTitle", "Unknown Book");

            String message = "Dear " + subscriberName + ", we regret to inform you that your request to extend the loan period for the book \"" + bookTitle + "\" (ID: " + copyID + ") has been rejected.";
            System.out.println("Sending rejection notification to subscriberID: " + subscriberID);
            System.out.println("Message: " + message);

            // Simulate sending notification (e.g., email or SMS)
        } catch (Exception e) {
            System.err.println("Error while sending rejection notification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Sends a notification to subscribers when their order arrives.
     *
     * @param bookToSubscriberMap A map where the key is the book ID, and the value is the subscriber waiting for the book.
     */
    public static void orderArrived(Map<String, Subscriber> bookToSubscriberMap) {
        for (Map.Entry<String, Subscriber> entry : bookToSubscriberMap.entrySet()) {
            String bookId = entry.getKey();
            Subscriber subscriber = entry.getValue();

            if (subscriber != null) {
                String subscriberName = subscriber.getFirstName() + " " + subscriber.getLastName();
                String message = "Dear " + subscriberName + ", your order for the book with ID \"" + bookId + "\" has arrived. Please visit the library to collect it.";

                System.out.println("Sending order arrival notification to subscriberID: " + subscriber.getId());
                System.out.println("Message: " + message);

                // Simulate sending notification (e.g., email or SMS)
            } else {
                System.out.println("No subscriber waiting for book ID: " + bookId);
            }
        }
    }

}