package gui.librarian.LibraryReports_UI;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import logic.communication.SceneManager;
import logic.librarian.ReportsGenerator_Controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.List;
import java.util.Map;

import entities.report.BorrowingReport;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class BorrowTimeReport_Controller {
	@FXML
	private BarChart<String, Number> borrowingReportChart;
	
	@FXML
	private Button btnClose;
	
	@FXML
    private Label reportInfo;
	
    private ReportsGenerator_Controller reportsGeneratorController;

	@FXML
	private void initialize() {
		// Get the singleton instance of ReportsGenerator_Controller
		reportsGeneratorController = ReportsGenerator_Controller.getInstance();
		// Initialize chart with data
		borrowingReportChart.getData().setAll(generateBorrowingReportChart().getData());
		reportInfo.setText(reportsGeneratorController.getMonth()+"/"+reportsGeneratorController.getYear());
	}
	
	/**
	 * Generates BarChart for borrowing reports.
	 *
	 * @return BarChart instance with the data.
	 */
	public BarChart<String, Number> generateBorrowingReportChart() {
		// Axes
		CategoryAxis xAxis = new CategoryAxis();
		xAxis.setLabel("Book Title");
		
		NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Time (days)");
		
		// Chart
		BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
		barChart.setTitle("Borrowing Report");
		
		// Series
		XYChart.Series<String, Number> totalBorrowTimeSeries = new XYChart.Series<>();
		totalBorrowTimeSeries.setName("Total Borrow Time");
		
		XYChart.Series<String, Number> lateReturnTimeSeries = new XYChart.Series<>();
		lateReturnTimeSeries.setName("Late Return Time");
		
		// load data
		//get the correct report
		BorrowingReport borrowingReport=reportsGeneratorController.getBorrowingReport();
		//add to graph the data for every book in the report
		Map<String, List<String>> borrowingData=borrowingReport.getBorrowingData();
		for (Map.Entry<String, List<String>> entry : borrowingData.entrySet()) {
			String bookTitle = entry.getKey();
			List<String> dataOfBook = entry.getValue();  
			totalBorrowTimeSeries.getData().add(new XYChart.Data<>(bookTitle, Integer.parseInt(dataOfBook.get(0))));
			lateReturnTimeSeries.getData().add(new XYChart.Data<>(bookTitle, Integer.parseInt(dataOfBook.get(1))));
		 }		
		// Add data to the chart
		barChart.getData().addAll(totalBorrowTimeSeries, lateReturnTimeSeries);
		
		return barChart;
	}
	@FXML
	private void handleClose(ActionEvent event){
		//switch to previous window
		//*********gui of the previous window not ready, when ready need to enter the path!!********
        SceneManager.switchScene("/gui/testing/generateReport.fxml","Reports Generator");
	}
}