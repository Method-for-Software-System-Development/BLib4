package logic.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import entities.logic.MessageType;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import logic.communication.ChatClient;
import logic.communication.ClientUI;


public class BooksController {
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
	
	
	 /**
     * Checks the availability of a book copy by its ID.
     *
     * @param copyID The ID of the book copy to check.
     * @return true if the book copy is available, false otherwise.
     */
	public boolean checkAvailability(String copyID) {
	    try {
	        // Send the copyID to the server to check availability of the copyID
	        ClientUI.chat.accept(new MessageType("106", copyID));

	        // Wait for the server's response
	        if (ChatClient.serverResponse) {
	            // If the server indicates the copy is available
	        	Alert alert = new Alert(Alert.AlertType.INFORMATION, "A book copy "+copyID+ " availabile");
	            alert.setHeaderText("Success");
	            alert.showAndWait();
	            return true;
	        } else {
	            // If the server indicates the copy is not available
	        	Alert alert = new Alert(Alert.AlertType.ERROR, "A book copy "+copyID+ " unavailabile");
	            alert.setHeaderText("Error");
	            alert.showAndWait();
	            return false;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
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

	    checkAvailability(copyID);
	}
	
	

	
	/**
     * Adds a subscriber to the waitlist for a book if the book is unavailable.
     *
     * @param bookID       The ID of the book to add to the waitlist.
     * @param subscriberID The ID of the subscriber requesting the book.
     * @param orderDateTime The date and time the order was placed.
     */
    public void addToWaitlist(String bookID, String subscriberID, LocalDateTime orderDateTime) {
       try {
    	// Create a list to be sent in the message to the server
        ArrayList<String> detailsOfOrder = new ArrayList<>();
        detailsOfOrder.add(subscriberID);
        detailsOfOrder.add(bookID);
        
        // Sending the server subscriberID and bookID
        ClientUI.chat.accept(new MessageType("108", detailsOfOrder));
        
        // Wait for server response
        if (ChatClient.serverResponse) {
        	Alert alert = new Alert(Alert.AlertType.INFORMATION, "Subscriber " + subscriberID + " has been added to the waitlist for book " + bookID);
            alert.setHeaderText("Success");
            alert.showAndWait();
        } else {
        	Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to add subscriber " + subscriberID + " to the waitlist for book " + bookID);
            alert.setHeaderText("Error");
            alert.showAndWait();
        }
    } catch (Exception e) {
        e.printStackTrace();
        }
    }
    
	//Add to Waitlist Section
    @FXML
    private void handleAddToWaitlist() {
        String bookID = txtBookID.getText();
        String subscriberID = txtSubscriberID.getText();
        
        if (bookID.isEmpty() || subscriberID.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill in both Book ID and Subscriber ID");
            alert.setHeaderText("Input Error");
            alert.showAndWait();
            return;
        }
        
        // Call the addToWaitlist method
        addToWaitlist(bookID, subscriberID, LocalDateTime.now());
    }
    
    
}










