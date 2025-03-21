package logic.librarian;

import entities.logic.MessageType;
import entities.report.BorrowingReport;
import entities.report.SubscriberStatusReport;
import logic.communication.ChatClient;
import logic.communication.ClientUI;

import java.util.*;

public class ReportsGenerator_Controller
{

    // Singleton instance of ReportsGenerator_Controller
    private static volatile ReportsGenerator_Controller instance = null;
    private String month;
    private String year;

    /**
     * Get the instance of the ReportsGenerator_Controller for singleton.
     *
     * @return The instance of the ReportsGenerator_Controller.
     */
    public static ReportsGenerator_Controller getInstance()
    {
        if (instance == null)
        {
            synchronized (ReportsGenerator_Controller.class)
            {
                if (instance == null)
                {
                    instance = new ReportsGenerator_Controller();
                }
            }
        }
        return instance;
    }

    /**
     * Retrieves a BorrowingReport for a given month and year.
     *
     * @return A BorrowingReport object, or null if not found.
     */
    public BorrowingReport getBorrowingReport()
    {
        // Create a list of data of the requested report to send to the server
        ArrayList<String> dataOfReport = new ArrayList<>();
        dataOfReport.add("BorrowingReport");
        dataOfReport.add(month);
        dataOfReport.add(year);
        // send message to server to get a report id
        ClientUI.chat.accept(new MessageType("125", dataOfReport));
        int reportNum = ChatClient.reportID;
        //if report id not found response is -1
        if (reportNum == -1)
        {
            System.out.println("No borrowing report report found for: " + month + " " + year);
            return null;
        }

        // send a message to server to get blob data of a report
        ClientUI.chat.accept(new MessageType("126", dataOfReport));
        //get response from server for blob data
        List<String[]> blobData = ChatClient.blobData;
        if (blobData == null)
        {
            System.out.println("No borrowing report found for: " + month + " " + year);
            return null;
        }
        return new BorrowingReport(reportNum, month, year, new Date(), mapBorrowReportData(blobData));
    }

    /**
     * Retrieves a SubscriberStatusReport for a given month and year.
     *
     * @return A SubscriberStatusReport object, or null if not found.
     */
    public SubscriberStatusReport getSubscriberStatusReport()
    {
        // Create a list of data of the requested report to send to the server
        ArrayList<String> dataOfReport = new ArrayList<>();
        dataOfReport.add("SubscribersStatus");
        dataOfReport.add(month);
        dataOfReport.add(year);
        // send message to server to get report id
        ClientUI.chat.accept(new MessageType("125", dataOfReport));
        int reportNum = ChatClient.reportID;
        //if report id not found response is -1
        if (reportNum == -1)
        {
            System.out.println("No subscriber status report id found for: " + month + " " + year);
            return null;
        }
        // send a message to server to get blob data of a report
        ClientUI.chat.accept(new MessageType("126", dataOfReport));
        //get response from server for blob data
        List<String[]> blobData = ChatClient.blobData;

        if (blobData == null)
        {
            System.out.println("No subscriber status report found for: " + month + " " + year);
            return null;
        }

        return new SubscriberStatusReport(reportNum, month, year, new Date(), blobData);
    }

    /**
     * Maps report data from List<String[]> to a Map<String, List<String>>.
     *
     * @param reportData Report data as a List<String[]>.
     * @return Report data as Map<String, List<String>>.
     */
    private Map<String, List<String>> mapBorrowReportData(List<String[]> reportData)
    {
        Map<String, List<String>> mappedData = new HashMap<>();
        for (String[] row : reportData)
        {
            if (row.length >= 3)
            {
                mappedData.put(row[0], Arrays.asList(row[1], row[2]));
            }
        }
        return mappedData;
    }

    /**
     * This method checks if a specific report is ready for use.
     *
     * @param reportType The type of the requested report.
     * @return The boolean response of the server if the report is ready.
     */
    public boolean checkIfReportIsReady(String reportType)
    {
        // Create a list of data of the requested report to send to the server
        ArrayList<String> dataOfReport = new ArrayList<>();
        dataOfReport.add(reportType);
        dataOfReport.add(month);
        dataOfReport.add(year);
        //checks if the report is ready
        ClientUI.chat.accept(new MessageType("127", dataOfReport));
        return ChatClient.serverResponse;
    }

    /**
     * The method gets the month and year of the report.
     *
     * @return The month of the report.
     */
    public String getMonth()
    {
        return month;
    }

    /**
     * The method sets the month and year of the report.
     *
     * @param month the month of the report.
     */
    public void setMonth(String month)
    {
        this.month = month;
    }

    /**
     * The method gets the year of the report.
     *
     * @return The year of the report.
     */
    public String getYear()
    {
        return year;
    }

    /**
     * The method sets the year of the report.
     *
     * @param year the year of the report.
     */
    public void setYear(String year)
    {
        this.year = year;
    }
}