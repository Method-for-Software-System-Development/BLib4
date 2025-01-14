package logic;
import entities.logic.Notification;
import entities.user.Subscriber;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * SchedulerController is responsible for scheduling and executing various periodic tasks.
 * These tasks include checking the database and notifying when specific periods have elapsed.
 */
public class SchedulerController {

    private ScheduledExecutorService scheduler;
    private DbController dbController; // Controller for database interactions

    /**
     * Constructor for SchedulerController.
     * Initializes the scheduler and database controller.
     */
    public SchedulerController() {
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.dbController = DbController.getInstance();

    }

    /**
     * Starts scheduling all tasks.
     * Each task runs at a specified interval using ScheduledExecutorService.
     *
     * @see ScheduledExecutorService
     */
    public void startSchedulers() {
        scheduler.scheduleAtFixedRate(this::notifyDayBeforeReturnDate, 0, 1, TimeUnit.DAYS);
        scheduler.scheduleAtFixedRate(this::notifyDeleteUnfulfilledOrder, 0, 1, TimeUnit.DAYS);
        scheduler.scheduleAtFixedRate(this::triggerOneMonthFromFreezeDate, 0, 1, TimeUnit.DAYS);


    }

    /**
     * Stops all scheduled tasks.
     * Ensures a graceful shutdown of the scheduler.
     *
     * @see ScheduledExecutorService#shutdown()
     */
    public void stopSchedulers() {
        scheduler.shutdown();
    }

    /**
     * Notifies that one day remains before a book's return date.
     * Queries the database for relevant records.
     */
    private void notifyDayBeforeReturnDate() {
        try {
            System.out.println("Checking for books due tomorrow...");

            // Fetch subscribers with books due tomorrow
            List<Subscriber> subscribers =  dbController.handleNotifyDayBeforeReturnDate();

            for (Subscriber subscriber : subscribers) {
                // Create a notification
                Notification notification = new Notification(
                        1,
                        "Library",
                        subscriber.getEmail(),
                        "Email",
                        "Reminder: A book is due tomorrow."
                );

                // Send the notification using Notification_Controller
                Notification_Controller notificationController = new Notification_Controller(notification);
                notificationController.dayBeforeReturnDate_Email_SMS(subscriber.getId(), subscriber.getEmail(), subscriber.getPhone());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks all subscribers daily to identify those eligible to extend their borrow period.
     * Sends a request to the server controller to process the data.
     */
    //ToDo: check if needed
//    private void notifyOpenOptionToExtendBorrow() {
//        try {
//            System.out.println("Checking for subscribers eligible to extend borrow...");
//
//            // Send a request to the ServerController to handle the operation
//            MessageType request = new MessageType("notifyOpenOptionToExtendBorrow", null);
//            ServerController.handleScheduler(request);
//
//            System.out.println("Database updated for eligible subscribers.");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * Checks the waitlist daily to identify unfulfilled reservations older than two days
     * and sends a request to the server to handle the removal process.
     */
    private void notifyDeleteUnfulfilledOrder() {
        try {
            System.out.println("Checking for unfulfilled reservations older than two days...");
            Map<String, Subscriber> bookToSubscriberMap = new HashMap<>();
            bookToSubscriberMap=dbController.handleDeleteUnfulfilledOrder();
            Notification_Controller.orderArrived(bookToSubscriberMap);
            System.out.println("Expired reservations removed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a request to the server to check for subscribers who have been frozen for more than one calendar month.
     *
     * @return List of subscribers who have been frozen for more than one calendar month.
     */
    public void triggerOneMonthFromFreezeDate()
    {

            System.out.println("Checking for subscribers who have been frozen for more than one month...");
            dbController.handleOneMonthFromFreezeDate();
            System.out.println("Subscribers who were frozen exactly one month ago have now been successfully unfrozen. ");



    }

}




