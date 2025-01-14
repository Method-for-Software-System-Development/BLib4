//package gui.testing;
//
//import java.time.LocalDateTime;
//
//import javafx.fxml.FXML;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Button;
//import javafx.scene.control.TextField;
//import logic.user.BooksController;
//
//public class BooksView_controller {
//	//Check Availability Section
//		@FXML
//	    private TextField txtCopyID;
//		@FXML
//	    private Button btnAvailable;
//
//		//Add to Waitlist Section
//		@FXML
//		private TextField txtBookID;
//		@FXML
//		private TextField txtSubscriberID;
//		@FXML
//	    private Button btnAddToWaitlist;
//
//		private BooksController bk= new BooksController();  // Initialize the BooksController here
//
//		//Check Availability Section
//		@FXML
//		private void handleCheckAvailability() {
//		    String copyID = txtCopyID.getText();
//		    if (copyID.isEmpty()) {
//		        Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a copy ID.");
//		        alert.setHeaderText("Warning");
//		        alert.showAndWait();
//		        return;
//		    }
//
//		    bk.checkAvailability(copyID);
//		}
//
//		//Add to Waitlist Section
//	    @FXML
//	    private void handleAddToWaitlist() {
//	        String bookID = txtBookID.getText();
//	        String subscriberID = txtSubscriberID.getText();
//
//	        if (bookID.isEmpty() || subscriberID.isEmpty()) {
//	            Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill in both Book ID and Subscriber ID");
//	            alert.setHeaderText("Input Error");
//	            alert.showAndWait();
//	            return;
//	        }
//
//	        // Call the addToWaitlist method
//	        bk.addToWaitlist(bookID, subscriberID);
//	    }
//
//}

import java.time.LocalDateTime;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import logic.user.BooksController;

public class BooksView_controller {
	//Check Availability Section
		@FXML
	    private TextField txtCopyID;
		@FXML
	    private Button btnAvailable;
		
		//Add to Waitlist Section
		@FXML
		private TextField txtBookID;
		@FXML
		private TextField txtSubscriberID;
		@FXML
	    private Button btnAddToWaitlist;
		
		private BooksController bk = BooksController.getInstance(); // Use the Singleton instance
		
		//Check Availability Section
		@FXML
		private void handleCheckAvailability() {
		    String copyID = txtCopyID.getText();
		    if (copyID.isEmpty()) {
		        Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a copy ID.");
		        alert.setHeaderText("Warning");
		        alert.showAndWait();
		        return;
		    }

		    bk.checkAvailability(copyID);
		}
		
		//Add to Waitlist Section
	    @FXML
	    private int handleAddToWaitlist() {
	        String bookID = txtBookID.getText();
	        String subscriberID = txtSubscriberID.getText();
	        
	        if (bookID.isEmpty() || subscriberID.isEmpty()) {
	            Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill in both Book ID and Subscriber ID");
	            alert.setHeaderText("Input Error");
	            alert.showAndWait();
	        }
	        
	        // Call the addToWaitlist method
	        return bk.addToWaitlist(bookID, subscriberID);
	    }
	   
}
