package gui.common;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import logic.communication.*;
import entities.logic.MessageType;
import java.util.ArrayList;
import logic.user.*;

public class BorrowController {
	
	@FXML
    private TextField txtBookID; // Input field for Book ID (barcode)

	@FXML
    private TextField txtSubscriberID;

	@FXML
    private Button btnBorrow; // Declaration for the button to borrow a book
	
	@FXML
	private TextField txtBorrowID; // Declaration of text field to return the book in the return UI
	
	@FXML
	private Button btnReturn; // Declaration of return button in return UI
	
	@FXML
	private Button btnExtend; //Declaration on extend button
	
	@FXML
	private DatePicker datePicker; // Declaration on date picker field
	
    private Alert alert;    
    private static BorrowController instance = null;

    /*
     * Private constructor 
     */
    private BorrowController() {
    }
    
    /*
     * getInstance is  singleton for BorrowController
     */
    
    public BorrowController getInstance() {
    	if(instance == null) {
    		instance = new BorrowController();
    		return instance;
    	}
    	return instance;
    }
    
    /*
     * Handles the borrow button action.
     * Sends subscriber ID and book ID to the server for validation (Message Type 107).
     * Waits for server response (Message Type 207).
     */
    @FXML
    public void createNewBorrow(ActionEvent event) {
        try {
            String enteredSubscriberID = txtSubscriberID.getText().trim();
            String enteredCopyBookID = txtBookID.getText().trim();
            
            if(BooksController.getInstance().checkAvailability(enteredCopyBookID)) {
                // Create a list to be sent in the message to the server
                ArrayList<String> detailsOfBorrow = new ArrayList<>();
                detailsOfBorrow.add(enteredSubscriberID);
                detailsOfBorrow.add(enteredCopyBookID);
                
                // Sending the server subscriberID and bookID
                ClientUI.chat.accept(new MessageType("107", detailsOfBorrow));
                
                // Wait for message response - handled by ChatClient class
                if(!ChatClient.serverResponse) {
                	alert =  new Alert(Alert.AlertType.ERROR, "Failed to borrow the book");
                	alert.setHeaderText("Borrow failed");
                	alert.showAndWait();
                }
            }
        }catch(Exception e) {
        	System.out.println(e.toString());
        }
    }
    
    /*
     * Method to extend the due date of a borrow.
     * It retrieves the borrow ID and the new selected date from the UI,
     * builds an ArrayList to send to the server, and handles the server response.
     */
    @FXML
    public void extendBorrow(ActionEvent event) {
    	try {
    		// Get data from fields in UI
    		String Borrow_id = btnExtend.getText().trim();
    		String selectedDate = datePicker.getValue().toString(); 
    		
    		// Building an ArrayList in order to send a MessageType Object in order to send to server
    		ArrayList<String> listToSend = new ArrayList<>();
    		listToSend.add(Borrow_id);
    		listToSend.add(selectedDate);
    		
    		// Sending to the server MessageType 117
    		ClientUI.chat.accept(new MessageType("117", listToSend));
    		
    		// Server response to validate success
    		if(ChatClient.serverResponse) {
    			alert = new Alert(Alert.AlertType.CONFIRMATION, "Success Manual extention");
    			alert.setHeaderText("Manual Extend");
    			alert.showAndWait();
    		}
    		
    	}catch(Exception e) {
    		System.out.println(e.toString());
    	}
    }
    
    /*
     * Method to return a borrowed item.
     * It retrieves the borrow ID from the UI, sends it to the server,
     * and handles the response to confirm if the return was successful or failed.
     */
    @FXML
    public void returnBorrow(ActionEvent event) {
    	try {
    		String Borrow_id = txtBorrowID.getText().trim();
    		
    		// Sending to the server MessageType 109
    		ClientUI.chat.accept(new MessageType("109", Borrow_id));
    		
    		// Server response to validate success
    		if(ChatClient.array.get(0)) {
    			alert = new Alert(Alert.AlertType.CONFIRMATION, "Return book success");
    			alert.setHeaderText("Return");
    			alert.showAndWait();
    		}
    	}catch(Exception e) {
    		System.out.println(e.toString());
    	}
    }
    
}
