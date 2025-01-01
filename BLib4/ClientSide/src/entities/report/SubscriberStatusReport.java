package entities.report;

import java.io.IOException;
import java.util.Date;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SubscriberStatusReport extends Report {
	private int[] dailyActive;
	private int[] dailyFreeze;
	private boolean ArraysWasSet = true;

	/**
	 * Constructor to initialize the SubscriberStatusReport
	 *
	 * @param reportNum   The report number
	 * @param month       The report's month
	 * @param year        The report's year
	 * @param issueDate   The date the report is issued
	 * @param dailyActive Array of daily active subscribers
	 * @param dailyFreeze Array of daily frozen subscribers
	 */
	public SubscriberStatusReport(int reportNum, String month, String year, Date issueDate,
								  int[] dailyActive, int[] dailyFreeze) {
		super(reportNum, month, year, issueDate);
		this.dailyActive = initializeArray(dailyActive,month);
		this.dailyFreeze = initializeArray(dailyFreeze,month);
	}

	//Private method to initialize the array in the right length according t the month in the year 
	private int[] initializeArray(int[] array, String month) {
		int daysInMonth = getDaysInMonth(validateMonth(month)); //Getting the amount of days in the month
		if(array!=null && daysInMonth!=0) { //Checking the array given is not null and the month is valid
			int[] newArray = new int[daysInMonth];
			for (int i=0; i<Math.min(daysInMonth, array.length); i++) {
				newArray[i] = array[i];
			}
			return newArray;
		}
		ArraysWasSet = ArraysWasSet && false; //Setting the flag of both arrays to be false
		return null;
	}

	private int getDaysInMonth(String month) {
	    switch (month.toLowerCase()) {
	        case "january": case "march": case "may": case "july":
	        case "august": case "october": case "december":
	            return 31;
	        case "april": case "june": case "september": case "november":
	            return 30;
	        case "february":
	            return 29;
	        default:
	            return 0;
	    }
	}
	
	//Getter to tell if the arrays was set correctly
	public boolean getArraysWasSet() {
		return ArraysWasSet;
	}
	
	public int[] getDailyActive() {
		return dailyActive;
	}

	public int[] getDailyFreeze() {
		return dailyFreeze;
	}

	@Override
	public void generateContent() {
		try {
			//loading FXML file
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Implement"));
			Parent root = loader.load();
			
			// Create a new stage for the new screen
	        Stage newStage = new Stage();
	        newStage.setTitle("Subscriber Status Report");
	        newStage.setScene(new Scene(root));
	        
	        //Show new screen
	        newStage.show();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

}
