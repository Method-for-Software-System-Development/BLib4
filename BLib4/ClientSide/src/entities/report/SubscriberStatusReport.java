package entities.report;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;

public class SubscriberStatusReport extends Report {
	private int[] dailyActive;
	private int[] dailyFreeze;


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
	public SubscriberStatusReport(int reportNum, String month, String year, Date issueDate,int[] dailyActive, int[] dailyFreeze)
	{
		super(reportNum, month, year, issueDate);
		this.dailyActive = dailyActive;
		this.dailyFreeze = dailyFreeze;
	}


	public int[] getDailyActive() {
		return dailyActive;
	}

	public int[] getDailyFreeze() {
		return dailyFreeze;
	}



}
