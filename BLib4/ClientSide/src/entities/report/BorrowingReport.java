package entities.report;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class BorrowingReport extends Report{
	private Map<String, List<String>> borrowingData;
	
	
	/**
	 * Constructor to initialize the SubscriberStatusReport
	 *
	 * @param reportNum   			The report number
	 * @param Month      			The report's month
	 * @param Year        			The report's year
	 * @param issueDate   			The date the report is issued
	 * @param borrowingData			The borrowing data
	 */
	public BorrowingReport(int reportNum, String Month, String Year, Date issueDate, Map<String, List<String>>borrowingData)
	{
		super(reportNum, Month, Year, issueDate);
		this.borrowingData=borrowingData;
	}


	/**
	 * Retrieves the borrowing data for a specific book.
	 *
	 * @param bookTitle The title of the book.
	 * @return A list of strings containing the borrow data for the book.
	 */
	public List<String> getBorrowingDataForBook(String bookTitle) {
		return borrowingData.getOrDefault(bookTitle, new ArrayList<>());
	}

	
}
