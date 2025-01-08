package logic.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import entities.book.Book;
import entities.logic.MessageType;
import logic.dbController;
import logic.communication.ChatClient;
import logic.communication.ClientUI;

public class BooksController {
	 /**
     * Checks the availability of a book copy by its ID.
     *
     * @param copyID The ID of the book copy to check.
     * @return true if the book copy is available, false otherwise.
     */
	public boolean checkAvailability(String copyID) {
		if (copyID == null || copyID.isEmpty()) {
            System.out.println("Copy ID is missing.");
            return false;
        }
	    try {
	        // Send the copyID to the server to check availability of the copyID
	        ClientUI.chat.accept(new MessageType("106", copyID));

	        // Wait for the server's response
	        if (ChatClient.serverResponse) {
	            // If the server indicates the copy is available
	            return true;
	        } else {
	            // If the server indicates the copy is not available
	            return false;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
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
    	// Validate the input data
    	if (bookID == null || subscriberID == null || orderDateTime == null) {
    	       System.out.println("Invalid input data for adding to waitlist.");
    	       return;
    	}

    	// Create a list to be sent in the message to the server
        ArrayList<String> detailsOfOrder = new ArrayList<>();
        detailsOfOrder.add(subscriberID);
        detailsOfOrder.add(bookID);
        
        // Sending the server subscriberID and bookID
        ClientUI.chat.accept(new MessageType("108", detailsOfOrder));
        
     // Wait for server response (assuming the server sends a boolean indicating success).
        if (ChatClient.serverResponse) {
            System.out.println("Subscriber " + subscriberID + " has been added to the waitlist for book " + bookID);
        } else {
            System.out.println("Failed to add subscriber " + subscriberID + " to the waitlist for book " + bookID);
        }
    } catch (Exception e) {
        System.out.println("Error occurred while adding to the waitlist: " + e.getMessage());
        e.printStackTrace();
    }
    }
    
    
}










