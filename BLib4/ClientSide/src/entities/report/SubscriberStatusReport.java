package entities.report;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

public class SubscriberStatusReport extends Report {
	private Map<String, int[]> usersActivityStatus;


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
	public SubscriberStatusReport(int reportNum, String month, String year, Date issueDate,Map<String, int[]> usersActivityStatus)
	{
		super(reportNum, month, year, issueDate);
		this.usersActivityStatus = usersActivityStatus;
	}


	public Map<String, int[]> getUsersActivityStatus() {
		return usersActivityStatus;
	}
}
