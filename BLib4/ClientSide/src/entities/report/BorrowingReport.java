package entities.report;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class BorrowingReport extends Report{
	private HashMap<Integer,ArrayList<Integer>> bookLateReturnNum = new HashMap<>();
	private HashMap<Integer,Integer> bookAverageBorrowing = new HashMap<>();
	
	
	/**
	 * Constructor to initialize the SubscriberStatusReport
	 *
	 * @param reportNum   			The report number
	 * @param month      			The report's month
	 * @param year        			The report's year
	 * @param issueDate   			The date the report is issued
	 * @param bookLateReturnNum 	The Late return map for each book
	 * @param bookAverageBorrowing  The average time for a book map for each book
	 */
	public BorrowingReport(int reportNum, String Month, String Year, Date issueDate,
						   HashMap<Integer, ArrayList<Integer>> bookLateReturnNum, HashMap<Integer, Integer> bookAverageBorrowing) {
		super(reportNum, Month, Year, issueDate);
		this.bookLateReturnNum = bookLateReturnNum;
		this.bookAverageBorrowing = bookAverageBorrowing;
	}

	@Override
	public void generateContent() {
		try {
			//loading FXML file
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Implement"));		//ToDo: implement the FXML file
			Parent root = loader.load();
			
			// Create a new stage for the new screen
	        Stage newStage = new Stage();
	        newStage.setTitle("Borrowing Report");
	        newStage.setScene(new Scene(root));
	        
	        //Show new screen
	        newStage.show();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	public HashMap<Integer, ArrayList<Integer>> getBookLateReturnNum() {
		return bookLateReturnNum;
	}

	public HashMap<Integer, Integer> getBookAverageBorrowing() {
		return bookAverageBorrowing;
	}
	
	public void addLateReturn(int bookid, int lateTime) {
		//Check if the book already exists in the map
		if(!bookLateReturnNum.containsKey(bookid)) {
			//If not creating a new list for the book
			bookLateReturnNum.put(bookid, new ArrayList<Integer>());
		}
		//Add the new late return time to the book's list
		bookLateReturnNum.get(bookid).add(lateTime);
	}
	
}
