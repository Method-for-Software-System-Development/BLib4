package gui.librarian.LibraryReports_UI;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import logic.librarian.ReportsGenerator_Controller;
import java.util.Map;

import entities.report.SubscriberStatusReport;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class SubscriberStatusReport_Controller {
	@FXML
	private BarChart<String, Number> subscriberStatusReportChart;

	@FXML
	private Button btnClose;
	
    private ReportsGenerator_Controller reportsGeneratorController;
    private String month;
    private String year;
    
	@FXML
	private void initialize() {
		// Initialize chart with data
		subscriberStatusReportChart.getData().setAll(generateSubscriberStatusReportChart().getData());
	}
	
	/**
	 * Generates a sample BarChart for subscriber status reports.
	 *
	 * @return BarChart instance with example data.
	 */
	public BarChart<String, Number> generateSubscriberStatusReportChart() {
		// Axes
		CategoryAxis xAxis = new CategoryAxis();
		xAxis.setLabel("Day of the Month");
		
		NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Number of Subscribers");
		
		// Chart
		BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
		barChart.setTitle("Subscriber Status Report");
		
		// Series
		XYChart.Series<String, Number> activeSubscribersSeries = new XYChart.Series<>();
		activeSubscribersSeries.setName("Active Subscribers");
		
		XYChart.Series<String, Number> frozenSubscribersSeries = new XYChart.Series<>();
		frozenSubscribersSeries.setName("Frozen Subscribers");
		
		// load data
		//get the correct report
		SubscriberStatusReport subscriberStatusReport=reportsGeneratorController.getSubscriberStatusReport(month, year);
		//add to graph the data for every book in the report
		Map<String, int[]> dailyActiveData=subscriberStatusReport.getUsersActivityStatus();
		for (Map.Entry<String, int[]> entry : dailyActiveData.entrySet()) {
			String date = entry.getKey();
			int[] dataDate = entry.getValue();  
			activeSubscribersSeries.getData().add(new XYChart.Data<>(date, dataDate[0]));
			frozenSubscribersSeries.getData().add(new XYChart.Data<>(date, dataDate[1]));
		 }	
		
		// Add data to the chart
		barChart.getData().addAll(activeSubscribersSeries, frozenSubscribersSeries);
		
		return barChart;
	}
	
	@FXML
	private void handleClose(ActionEvent event){
		//switch to previous window
		//*********gui of the previous window not ready, when ready need to enter the path!!********
     //   SceneManager.switchScene("Enter path");
	}
}