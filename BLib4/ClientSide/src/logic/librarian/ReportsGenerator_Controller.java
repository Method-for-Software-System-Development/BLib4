package logic.librarian;

import entities.logic.MessageType;
import entities.report.BorrowingReport;
import entities.report.SubscriberStatusReport;
import logic.communication.ChatClient;
import logic.communication.ClientUI;

import java.util.*;

/**
 * Controller responsible for generating, saving, and displaying reports in the library system.
 */
public class ReportsGenerator_Controller {

    private static ReportsGenerator_Controller instance = null;
    private String month;
	private String year;
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
     * @return A BorrowingReport object, or null if not found.
     */
    public BorrowingReport getBorrowingReport() {
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
        List<String[]> blobData = ChatClient.blobData;
        if (blobData == null) {
            System.out.println("No borrowing report found for: " + month + " " + year);
            return null;
        }
        return new BorrowingReport(reportNum, month, year, new Date(), mapBorrowReportData(blobData));
    }

    /**
     * Retrieves a SubscriberStatusReport for a given month and year.
     * @return A SubscriberStatusReport object, or null if not found.
     */
    public SubscriberStatusReport getSubscriberStatusReport() {
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
            System.out.println("No subscriber status report id found for: " + month + " " + year);
            return null;
        }
        // send message to server to get blob data of a report
        ClientUI.chat.accept(new MessageType("126",dataOfReport));
        //get response from server for blob data
        List<String[]> blobData = ChatClient.blobData;

        if (blobData == null) {
            System.out.println("No subscriber status report found for: " + month + " " + year);
            return null;
        }

        return new SubscriberStatusReport(reportNum, month, year, new Date(), blobData);
    }

    /**
     *  Maps report data from List<String[]> to a Map<String, List<String>>.
     *  
     * @param reportData report data as a List<String[]>
     * @return report data as Map<String, List<String>>
     */
    private Map<String, List<String>> mapBorrowReportData(List<String[]> reportData) {
        Map<String, List<String>> mappedData = new HashMap<>();
        for (String[] row : reportData) {
            if (row.length >= 3) {
                mappedData.put(row[0], Arrays.asList(row[1], row[2]));
            }
        }
        return mappedData;
    }

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
}
