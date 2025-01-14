package logic.librarian;

import entities.logic.MessageType;
import entities.report.BorrowingReport;
import entities.report.SubscriberStatusReport;
import entities.report.Report;
import logic.BlobUtil;
import logic.dbController;
import logic.communication.ChatClient;
import logic.communication.ClientUI;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

/**
 * Controller responsible for generating, saving, and displaying reports in the library system.
 */
public class ReportsGenerator_Controller {

    private static ReportsGenerator_Controller instance = null;
    private dbController dbController; // Controller for database interactions

    /**
     * Private constructor for ReportsGenerator_Controller.
     * Initializes the DB controller instance.
     */
    private ReportsGenerator_Controller() {
        this.dbController = logic.dbController.getInstance();
    }

    /**
     * Get the singleton instance of ReportsGenerator_Controller.
     *
     * @return the instance of ReportsGenerator_Controller.
     */
    public static ReportsGenerator_Controller getInstance() {
        if (instance == null) {
            instance = new ReportsGenerator_Controller();
        }
        return instance;
    }

    /**
     * Updates the daily status of subscribers in the database.
     * For example, it tracks active or frozen subscribers for reporting purposes.
     */
    public void DBdailyUpdate_SubscriberStatus()
    {
        System.out.println("Updating daily subscriber status...");
        String activeCount = String.valueOf(dbController.fetchActiveSubscribersCount()); // Fetch active subscribers count
        String frozenCount = String.valueOf(dbController.fetchFrozenSubscribersCount()); // Fetch frozen subscribers count
        String currentDate = LocalDate.now().toString(); // Get current date as string
        updateAndFetchMonthlySubscribersStatusReport("currentDate", "activeCount", "frozenCount"); // Update the monthly report
        System.out.println("Daily subscriber status updated successfully.");
    }



    /**
     * Fetches borrowing report data from the database and returns it as a Map.
     * The map contains book titles as keys and a map of borrowing data as values.
     *
     * @return A map of borrowing report data.
     */
    public Map<String, List<String>> fetchBorrowingReportData() {
        Map<String, String> totalBorrowTimeMap = dbController.fetchTotalBorrowTime();
        Map<String, String> lateBorrowTimeMap = dbController.fetchLateBorrowTime();
        Map<String, List<String>> borrowingReportData = new HashMap<>();

        for (String bookTitle : totalBorrowTimeMap.keySet()) {
            List<String> bookData = new ArrayList<>();
            bookData.set(0,totalBorrowTimeMap.get(bookTitle));
            bookData.set(1, lateBorrowTimeMap.getOrDefault(bookTitle, "0"));
            borrowingReportData.put(bookTitle, bookData);
        }

        return borrowingReportData;
    }

    /**
     * Updates the monthly SubscribersStatus report by adding today's data and fetching the updated report.
     *
     * @param day The day of the month.
     * @param activeCount The number of active subscribers for the day.
     * @param frozenCount The number of frozen subscribers for the day.
     * @return The updated monthly report as a List<String[]>.
     */
    public List<String[]> updateAndFetchMonthlySubscribersStatusReport(String day, String activeCount, String frozenCount) {
        System.out.println("Updating and fetching the monthly report...");

        // Step 1: Fetch existing report
        List<String[]> reportData = dbController.fetchMonthlySubscribersStatusReport();

        // Step 2: Add today's data
        String[] newRow = {day, activeCount, frozenCount};
        reportData.add(newRow);

        // Step 3: Update the report in the database
        try {
            byte[] updatedBlob = BlobUtil.convertListToBlob(reportData);
            dbController.updateMonthlySubscribersStatusReport(updatedBlob);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to update the monthly report in the database.");
        }

        System.out.println("Monthly report updated and fetched successfully.");
        return reportData;
    }



    /**
     * Generates a borrowing report based on data from the database and saves it to monthly_reports.
     */
    public void autoGenerate_BorrowingReport() {
        System.out.println("Generating borrowing report...");

        // Fetch borrowing report data
        Map<String, List<String>> borrowingReportData = fetchBorrowingReportData();

        // Convert the report data to List<String[]> format for BlobUtil
        List<String[]> reportData = new ArrayList<>();
        reportData.add(new String[]{"Book Title", "Total Borrow Time", "Late Borrow Time"}); // Header row

        for (Map.Entry<String, List<String>> entry : borrowingReportData.entrySet()) {
            String bookTitle = entry.getKey();
            String totalBorrowTime = entry.getValue().get(0);
            String lateBorrowTime = entry.getValue().get(1);
            reportData.add(new String[]{bookTitle, totalBorrowTime, lateBorrowTime});
        }

        // Convert report data to Blob
        byte[] reportBlob;
        try {
            reportBlob = BlobUtil.convertListToBlob(reportData);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate borrowing report blob");
        }

        // Save the report to the database
        String reportType = "BorrowingReport";
        try {
            dbController.saveMonthlyReport(reportType, reportBlob);
            System.out.println("Borrowing report generated and saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save borrowing report to the database");
        }


    }


    /**
     * Generates an empty subscriber status report for the next month.
     * This method is triggered on the last day of the current month.
     */
    public void autoGenerate_SubscribersStatusReport() {
        System.out.println("Generating empty subscribers status report for the next month...");

        String reportType = "SubscribersStatus";
        String nextMonth = LocalDate.now().plusMonths(1).withDayOfMonth(1).toString(); // First day of next month

        // Insert an empty report for the next month
        try {
            // Create a list of data of the requested report to send to the server
            ArrayList<String> dataOfReport = new ArrayList<>();	
            dataOfReport.add(reportType);
            dataOfReport.add(nextMonth);
            // send message to server to get report id
            ClientUI.chat.accept(new MessageType("127",dataOfReport));   
            System.out.println("Empty subscribers status report created for the next month.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to generate the next month's subscribers status report.");
        }
    }

    /**
     * Retrieves a BorrowingReport for a given month and year.
     *
     * @param month The month of the report.
     * @param year The year of the report.
     * @return A BorrowingReport object, or null if not found.
     */
    public BorrowingReport getBorrowingReport(String month, String year) {
        System.out.println("Fetching borrowing report for: " + month + " " + year);
        // Create a list of data of the requested report to send to the server
        ArrayList<String> dataOfReport = new ArrayList<>();	
        dataOfReport.add("BorrowingReport");
        dataOfReport.add(month);
        dataOfReport.add(year);
        // send message to server to get report id
        ClientUI.chat.accept(new MessageType("125",dataOfReport));
        int reportNum = ChatClient.reportID;
        //if report id not found response is -1
        if (reportNum == -1) {
            System.out.println("No subscriber status report found for: " + month + " " + year);
            return null;
        }
        
     // send message to server to get report id
        ClientUI.chat.accept(new MessageType("126",dataOfReport));
        //get response from server for blob data
        byte[] blobData = ChatClient.blobData;
        if (blobData == null) {
            System.out.println("No borrowing report found for: " + month + " " + year);
            return null;
        }

        List<String[]> reportData = BlobUtil.convertBlobToList(blobData);
        return new BorrowingReport(reportNum, month, year, new Date(), mapReportData(reportData));
    }

    /**
     * Retrieves a SubscriberStatusReport for a given month and year.
     *
     * @param month The month of the report.
     * @param year The year of the report.
     * @return A SubscriberStatusReport object, or null if not found.
     */
    public SubscriberStatusReport getSubscriberStatusReport(String month, String year) {
        System.out.println("Fetching subscriber status report for: " + month + " " + year);
        // Create a list of data of the requested report to send to the server
        ArrayList<String> dataOfReport = new ArrayList<>();	
        dataOfReport.add("SubscribersStatus");
        dataOfReport.add(month);
        dataOfReport.add(year);
        // send message to server to get report id
        ClientUI.chat.accept(new MessageType("125",dataOfReport));
        int reportNum = ChatClient.reportID;
        //if report id not found response is -1
        if (reportNum == -1) {
            System.out.println("No subscriber status report found for: " + month + " " + year);
            return null;
        }
        // send message to server to get report id
        ClientUI.chat.accept(new MessageType("126",dataOfReport));
        //get response from server for blob data
        byte[] blobData = ChatClient.blobData;

        if (blobData == null) {
            System.out.println("No subscriber status report found for: " + month + " " + year);
            return null;
        }

        List<String[]> reportData = BlobUtil.convertBlobToList(blobData);
        int[] dailyActive = extractDailyValues(reportData, 0);
        int[] dailyFreeze = extractDailyValues(reportData, 1);

        return new SubscriberStatusReport(reportNum, month, year, new Date(), dailyActive, dailyFreeze);
    }

    /**
     * Maps report data from List<String[]> to a Map<String, List<String>>.
     */
    private Map<String, List<String>> mapReportData(List<String[]> reportData) {
        Map<String, List<String>> mappedData = new HashMap<>();
        for (String[] row : reportData) {
            if (row.length >= 3) {
                mappedData.put(row[0], Arrays.asList(row[1], row[2]));
            }
        }
        return mappedData;
    }

    /**
     * Extracts daily values from report data.
     *
     * @param reportData The report data as a List<String[]>.
     * @param columnIndex The column index to extract values from.
     * @return An array of integers representing daily values.
     */
    private int[] extractDailyValues(List<String[]> reportData, int columnIndex) {
        // Skip the header row and initialize the array for daily values
        int[] dailyValues = new int[reportData.size() - 1]; // Exclude header row
        for (int i = 1; i < reportData.size(); i++) { // Start from the second row
            try {
                dailyValues[i - 1] = Integer.parseInt(reportData.get(i)[columnIndex]);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                dailyValues[i - 1] = 0; // Set to 0 if parsing fails or index is invalid
                System.out.println("Error parsing value at row " + i + ", column " + columnIndex + ": " + e.getMessage());
            }
        }
        return dailyValues;
    }




}
