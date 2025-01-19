package gui.testing;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import logic.communication.SceneManager;
import logic.librarian.ReportsGenerator_Controller;

public class generateReport_controller {
	ReportsGenerator_Controller reportsGeneratorController =ReportsGenerator_Controller.getInstance();
	@FXML
	private TextField txtReportTypeID;	
	@FXML
	private TextField txtMonthID;	
	@FXML
	private TextField txtYearID;
	
	@FXML
    private Button btnGenerateReport;

	@FXML
	private void handleGenerateReport() {
	    String reportType = txtReportTypeID.getText();
	    String month = txtMonthID.getText();
	    String year = txtYearID.getText();
	    if (reportType.isEmpty()) {
	        Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a report type.");
	        alert.setHeaderText("Warning");
	        alert.showAndWait();
	        return;
	    }
	    if (month.isEmpty()) {
	        Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a month.");
	        alert.setHeaderText("Warning");
	        alert.showAndWait();
	        return;
	    }
	    if (year.isEmpty()) {
	        Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a year.");
	        alert.setHeaderText("Warning");
	        alert.showAndWait();
	        return;
	    }
	    reportsGeneratorController.setMonth(month);
	    reportsGeneratorController.setYear(year);
	    if(reportType.equals("subscriberStatus"))
	    	SceneManager.switchScene("/gui/librarian/LibraryReports_UI/SubscriberStatusReport_UI.fxml","Subscribers Status Report");
	    else SceneManager.switchScene("/gui/librarian/LibraryReports_UI/BorrowTimeReport_UI.fxml","Borrow Time Report");

	}
}
