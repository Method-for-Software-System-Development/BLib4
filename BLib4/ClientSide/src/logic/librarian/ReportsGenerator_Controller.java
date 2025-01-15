package logic.librarian;

import entities.logic.MessageType;
import entities.report.BorrowingReport;
import entities.report.SubscriberStatusReport;
import logic.BlobUtil;
import logic.communication.ChatClient;
import logic.communication.ClientUI;

import java.util.*;

/**
 * Controller responsible for generating, saving, and displaying reports in the library system.
 */
public class ReportsGenerator_Controller {

    private static ReportsGenerator_Controller instance = null;
    public static ReportsGenerator_Controller getInstance()
    {
        if (instance == null)
        {
            instance = new ReportsGenerator_Controller();
        }
        return instance;
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

     // send message to server to get blob data of a report
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
        // send message to server to get blob data of a report
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
