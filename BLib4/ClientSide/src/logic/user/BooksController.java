package logic.user;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import entities.logic.MessageType;
import logic.communication.ChatClient;
import logic.communication.ClientUI;


public class BooksController {
	private static volatile BooksController instance = null;

	/*
	 * Private constructor for BooksControllers
	 */
	public BooksController() {
	}
	
	 /**
     * get the instance of the BooksController for singleton
     *
     * @return the instance of the BooksController
     */
	public static BooksController getInstance() 
	{
		if(instance == null) 
		{
			 synchronized (BooksController.class)
	            {
	                if (instance == null)
	                {
	                    instance = new BooksController();
	                }
	            }
		}
		return instance;
	}
	
	
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
    public int addToWaitlist(String bookID, String subscriberID) {
       try {
    	// Create a list to be sent in the message to the server
        ArrayList<String> detailsOfOrder = new ArrayList<>();
        detailsOfOrder.add(subscriberID);
        detailsOfOrder.add(bookID);
        
        // Sending the server subscriberID and bookID
        ClientUI.chat.accept(new MessageType("108", detailsOfOrder));
        
        // Wait for server response
        // if the book has successfully ordered - return 1
        if (ChatClient.array.get(0)) {
        	return 1;
        } // if the book cannot be ordered due to a high number of reservations - return 2
        else if (!ChatClient.array.get(0) && !ChatClient.array.get(1)) {
        	return 2;
        }
     // if the book cannot be ordered due to a frozen account - return 3
        	return 3;
    } catch (Exception e) {
        e.printStackTrace();
        return 0;
        }
    }
    
    
}










