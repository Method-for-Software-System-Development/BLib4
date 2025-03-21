package logic;

import entities.logic.Notification;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * SchedulerController is responsible for scheduling and executing various periodic tasks.
 * These tasks include checking the database and notifying when specific periods have elapsed.
 */
public class SchedulerController
{
    private volatile static SchedulerController instance;

    private final ScheduledExecutorService scheduler;
    private final DbController dbController; // Controller for database interactions
    private final Notification_Controller notificationController;
    private final ReportsGenerator_Controller reportsGeneratorController;

    /**
     * Provides a singleton instance of SchedulerController.
     * Ensures thread-safe initialization.
     *
     * @return the singleton instance of SchedulerController
     */
    public static SchedulerController getInstance()
    {
        if (instance == null)
        {
            synchronized (SchedulerController.class)
            {
                if (instance == null)
                {
                    instance = new SchedulerController();
                }
            }
        }
        return instance;
    }

    /**
     * Private constructor for SchedulerController.
     * Initializes the scheduler and associated controllers.
     */
    private SchedulerController()
    {
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.dbController = DbController.getInstance();
        this.notificationController = Notification_Controller.getInstance();
        this.reportsGeneratorController = ReportsGenerator_Controller.getInstance();
    }

    /**
     * Starts scheduling all tasks.
     * Each task runs at a specified interval using ScheduledExecutorService.
     *
     * @see ScheduledExecutorService
     */
    public void startSchedulers()
    {
        // set schedule for each task
        scheduler.scheduleAtFixedRate(this::notifyDayBeforeReturnDate, 0, 1, TimeUnit.DAYS);
        scheduler.scheduleAtFixedRate(this::notifyDeleteUnfulfilledOrder, 0, 1, TimeUnit.DAYS);
        scheduler.scheduleAtFixedRate(this::triggerOneMonthFromFreezeDate, 0, 1, TimeUnit.DAYS);
        scheduler.scheduleAtFixedRate(this::triggerMonthlyReportCreation, 0, 1, TimeUnit.DAYS);
        scheduler.scheduleAtFixedRate(this::triggerDailyReportUpdate, 0, 1, TimeUnit.DAYS);
        System.out.println("Scheduler started successfully.");
    }

    /**
     * Stops all scheduled tasks.
     * Ensures a graceful shutdown of the scheduler.
     *
     * @see ScheduledExecutorService#shutdown()
     */
    public void stopSchedulers()
    {
        scheduler.shutdown();
    }

    /**
     * Notifies that one day remains before a book's return date.
     * Queries the database for relevant records.
     */
    private void notifyDayBeforeReturnDate()
    {
        try
        {
            System.out.println("Checking for books due tomorrow...");

            // Fetch subscribers with books due tomorrow
            List<List<String>> remindersList = dbController.handleGetReminderDayBeforeDetails();

            for (List<String> reminder : remindersList)
            {
                // Create a notification
                String message = "Hello " + reminder.get(1) + "<br><br>Reminder: You have a book: \"" + reminder.get(3) + "\" is due tomorrow.";


                Notification notification = new Notification(
                        1,
                        "Library",
                        reminder.get(2),
                        "Email",
                        message
                );

                // Send the notification using Notification_Controller
                notificationController.dayBeforeReturnDate_Email_SMS(reminder.get(0), reminder.get(2), notification.getContent());
            }
        }
        catch (Exception e)
        {
            System.out.println("Error while checking for books due tomorrow: " + e.getMessage());
        }
    }

    /**
     * Checks the waitlist daily to identify unfulfilled reservations older than two days
     * and sends a request to the server to handle the removal process.
     */
    private void notifyDeleteUnfulfilledOrder()
    {
        System.out.println("Checking for unfulfilled reservations older than two days...");
        dbController.handleDeleteUnfulfilledOrder();
    }

    /**
     * Sends a request to the server to check for subscribers who have been frozen for more than one calendar month.
     */
    public void triggerOneMonthFromFreezeDate()
    {
        System.out.println("Checking for subscribers who have been frozen for more than one calendar month...");
        dbController.handleOneMonthFromFreezeDate();
    }

    /**
     * The method trigger save daily data for the monthly report.
     */
    public void triggerDailyReportUpdate()
    {
        if (dbController.handleGetTriggerOfDailyReportUpdate())
        {
            // save daily data for the monthly report
            reportsGeneratorController.dbDailyUpdate_SubscriberStatus();
        }
    }

    /**
     * The method trigger creation of the monthly report in the start of the month.
     */
    public void triggerMonthlyReportCreation()
    {
        if (dbController.handleGetTriggerOfMonthlyReportUpdate())
        {
            // create a subscriber status report for next month
            reportsGeneratorController.autoGenerate_SubscribersStatusReport();
            // create a borrowing report for the prev month
            reportsGeneratorController.autoGenerate_BorrowingReport();
        }
    }
}




