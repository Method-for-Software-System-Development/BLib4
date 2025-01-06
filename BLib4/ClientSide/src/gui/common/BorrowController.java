package gui.common;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import logic.communication.*;
import entities.logic.Borrow;
import entities.logic.MessageType;
import gui.user.DocumentationController;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

public class BorrowController {
	
	@FXML
    private TextField txtBookID; // Input field for Book ID (barcode)

	
	@FXML
    private TextField txtSubscriberID;

	@FXML
    private Button btnBorrow; // Declaration for the button with fx:id="btnBorrow"
	
    private Alert alert;

    /**
     * Handles the borrow button action.
     * Sends subscriber ID and book ID to the server for validation (Message Type 107).
     * Waits for server response (Message Type 207).
     */
    @FXML
    private void createNewBorrow(ActionEvent event) {
        System.out.println("Borrow button clicked!");
        try {
            String enteredSubscriberID = txtSubscriberID.getText().trim();
            String enteredBookID = txtBookID.getText().trim();
            
            // If button of borrow pressed when the Subscriber ID was not entered
            if (enteredSubscriberID.isEmpty()) {
            	alert = new Alert(Alert.AlertType.INFORMATION, "Please enter Subscriber ID");
                alert.setHeaderText("Error");
                alert.showAndWait();
                return;
            }
            // If button of borrow pressed when the Book ID was not entered
            if (enteredBookID.isEmpty()) {
            	alert = new Alert(Alert.AlertType.INFORMATION, "Please enter Book ID");
                alert.setHeaderText("Error");
                alert.showAndWait();
                return;
            }
            
            // Create a list to be sent in the message to the server
            ArrayList<String> detailsOfBorrow = new ArrayList<>();
            detailsOfBorrow.add(enteredSubscriberID);
            detailsOfBorrow.add(enteredBookID);
            
            // Sending the server subscriberID and bookID
            ClientUI.chat.accept(new MessageType("107", detailsOfBorrow));
            
            // Wait for message response - handled by ChatClient class
            if(ChatClient.serverResponse) {
            	// Sending the server a request for SchedularController in order to receive today's date
            	ClientUI.chat.accept(new MessageType("122", null));
            	Date today = (Date) ChatClient.todayDate;
            	
            	// Using the calendar to set the return time to be 14 days from today
            	Calendar calendar = Calendar.getInstance();
            	calendar.setTime(today);
            	calendar.add(Calendar.DATE, 14); // Add 14 days
            	Date dueDate = (Date) calendar.getTime(); // Get the updated date
            	
            	// Sending to the DocumentationController the borrow of the book
            	DocumentationController.receiveBorrow(new Borrow(today, dueDate, null, "Active", 0, null));
            }
        }catch(Exception e) {
        	System.out.println(e.toString());
        }
    }
    
}
