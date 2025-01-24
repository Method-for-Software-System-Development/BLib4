package logic;

import entities.logic.MessageType;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportsGenerator_Controller {

    private static volatile ReportsGenerator_Controller instance=null;


    private final DbController dbController;

    public ReportsGenerator_Controller() {
        this.dbController = DbController.getInstance();
    }
    public static ReportsGenerator_Controller getInstance()
    {
    	synchronized (ReportsGenerator_Controller.class)
        {
            if (instance == null)
            {
                instance = new ReportsGenerator_Controller();
            }
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
        int currentDate = LocalDate.now().getDayOfMonth(); // Get current day date as string
        updateAndFetchMonthlySubscribersStatusReport(Integer.toString(currentDate), activeCount, frozenCount); // Update the monthly report
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
            bookData.add(totalBorrowTimeMap.get(bookTitle));
            bookData.add(lateBorrowTimeMap.getOrDefault(bookTitle, "0"));
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
     * Generates an empty borrowing time report for the previous month.
     * This method is triggered on the first day of the month.
     * Generates a borrowing report based on data from the database and saves it to monthly_reports.
     */
    public void autoGenerate_BorrowingReport() {
    	//create new empty report for the previous month
        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();
        //check if the year number changed
        if(month==1) {
        	month=1;
        	year--;
        }
    	generateEmptyReportForTheMonth("BorrowingReport", month, year);   	
    	
        System.out.println("Generating borrowing report...");

        // Fetch borrowing report data
        Map<String, List<String>> borrowingReportData = fetchBorrowingReportData();

        // Convert the report data to List<String[]> format for BlobUtil
        List<String[]> reportData = new ArrayList<>();

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
        try {
            dbController.saveMonthlyReport("BorrowingReport", reportBlob);
            System.out.println("Borrowing report generated and saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save borrowing report to the database");
        }
    }

    /**
     * Generates an empty subscriber status report for the coming month.
     * This method is triggered on the first day of the month.
     */
    public void autoGenerate_SubscribersStatusReport() {
        int currentMonth = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();
        generateEmptyReportForTheMonth("SubscribersStatus",currentMonth,year);
    }
    
    /**
     * requests to add a new empty report the given month, year and report type.
     * @param reportType the type of the generated report
     * @param month month of the empty report
     * @param year year of the empty report
     */
    private void generateEmptyReportForTheMonth(String reportType, int month, int year) {
    	System.out.println("Generating "+reportType+" status report for the coming month...");

        //format the year and month to string
        String MonthStr = String.format("%02d", month); 
        String YearStr = Integer.toString(year);
     
        // Insert an empty report for the coming month
        try {
            // Create a list of data of the requested report to send to the server
            ArrayList<String> dataOfReport = new ArrayList<>();
            dataOfReport.add(reportType);
            dataOfReport.add(MonthStr);
            dataOfReport.add(YearStr);
            // send message to server to get report id
            dbController.insertEmptyMonthlyReport((List<String>) dataOfReport);
            System.out.println("Empty "+reportType+" report created for the coming month.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to generate the coming month's "+reportType+" report.");
        }
    }
}