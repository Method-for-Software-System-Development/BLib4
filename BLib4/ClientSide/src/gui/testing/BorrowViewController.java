package gui.testing;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import logic.librarian.BorrowController;

public class BorrowViewController {
	@FXML
	private TextField txtBookID; // Input field for Book ID Barcode
	@FXML
	private TextField txtSubscriberID; // Input of Subscriber ID
	@FXML
	private Button btnBorrow; // Declaration for the button to borrow a book
	@FXML
	private TextField txtBorrowID; // Declaration of text field to return the book in the return UI
	@FXML
	private Button btnReturn; // Declaration of return button in return UI
	@FXML
	private Button btnExtend; // Declaration on extend button
	@FXML
	private DatePicker datePicker; // Declaration on date picker field

	// Use the Singleton instance
	private BorrowController instance = BorrowController.getInstance();

	/*
	 * Handles the borrow button action. 
	 */
	@FXML
	private void handlecreateNewBorrow() {
		String subscriberID = txtSubscriberID.getText();
		String bookID = txtBorrowID.getText();
	    if (bookID.isEmpty()) {
	        Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a book ID.");
	        alert.setHeaderText("Warning");
	        alert.showAndWait();
	        return;
	    }
	    if (subscriberID.isEmpty()) {
	        Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a subscriber ID.");
	        alert.setHeaderText("Warning");
	        alert.showAndWait();
	        return;
	    }

	    instance.createNewBorrow(subscriberID,bookID);
	}
	

	/*
	 * Method to extend the due date of a borrow. 
	 */
	@FXML
	private void handleManualextendBorrow() {
		String borrowID = txtBorrowID.getText();
	    if (borrowID.isEmpty()) {
	        Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a borrow ID.");
	        alert.setHeaderText("Warning");
	        alert.showAndWait();
	        return;
	    }

	    instance.extendBorrow(borrowID,datePicker.toString());
	}
	

	/*
	 * Method to return a borrowed item.
	 */
	@FXML
	private void handlereturnBorrow() {
		String borrowID = txtBorrowID.getText();
	    if (borrowID.isEmpty()) {
	        Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a borrow ID.");
	        alert.setHeaderText("Warning");
	        alert.showAndWait();
	        return;
	    }

	    instance.returnBorrow(borrowID);
	}
}
