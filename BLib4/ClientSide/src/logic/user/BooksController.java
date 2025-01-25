package logic.user;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import entities.logic.MessageType;
import logic.communication.ChatClient;
import logic.communication.ClientUI;


public class BooksController {
	// Singleton instance of BooksController
    private static volatile BooksController instance = null;

    // List of subscriber IDs with a specific book order
    public List<String> subscriberIdWithOrder = new ArrayList<>();

    /**
     * Private constructor for BooksController to implement the Singleton pattern.
     */
    private BooksController() {
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
     * Gets the list of subscriber IDs associated with an order.
     *
     * @return A list of subscriber IDs.
     */
    public List<String> getSubscriberIdWithOrder() {
        return subscriberIdWithOrder;
    }
	

    /**
     * Checks the availability of a book copy based on its unique ID.
     * The method communicates with the server to check if the book copy is available.
     *
     * @param copyID The unique ID of the book copy to check.
     * @return 0 if the book copy is available, 
     *         1 if unavailable with a waitlist,
     *         2 if the copy id isn't in DB,
     *        -1 if an error occurs.
     */
	public int checkAvailability(String copyID) {
		if (copyID == null || copyID.isEmpty()) {
	        System.err.println("Invalid copyID provided");
	        return -1;
	    }
	    try {
	    	// Clear previous subscriber IDs from the list
	    	subscriberIdWithOrder.clear();
	        // Send the copyID to the server to check availability of the copyID
	        ClientUI.chat.accept(new MessageType("106", copyID));
	        
	        // Wait for the server's response
	        
	        // check if the copy of book is available
	        if (ChatClient.availability.get(0).equals("1")) {
	        //available for everyone
	        	return 0;	        		
	        //book has orders so it available for wait list only
	        } else if(ChatClient.availability.get(0).equals("2")){
	        	// Add IDs of subscribers who are waiting for the book
	        	subscriberIdWithOrder.addAll(ChatClient.availability.subList(1, ChatClient.availability.size()));
	        	return 1;
	        //copy is borrowed
	        } else if (ChatClient.availability.get(0).equals("3")) {
	        	return 2;
	        }
	        // Book is not in DB
	        else return 3;
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        return -1;
	    }
	}

	/**
     * Adds a subscriber to the waitlist for a book that is currently unavailable.
     * Communicates with the server to perform the action.
     *
     * @param bookID        The unique ID of the book to add to the waitlist.
     * @param subscriberID  The unique ID of the subscriber requesting the book.
     * @return 1 if the subscriber was successfully added to the waitlist,
     *         2 if the book has too many reservations,
     *         3 if the subscriber's account is frozen,
     *         0 if an unexpected error occurs.
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
        if (ChatClient.returnOutcome.get(0)) {
        	// Subscriber successfully added to the waitlist
        	return 1;
        } // if the book cannot be ordered due to a high number of reservations - return 2
        else if (!ChatClient.returnOutcome.get(0) && !ChatClient.returnOutcome.get(1) && !ChatClient.returnOutcome.get(2)) {
        	// Too many reservations, cannot add subscriber to waitlist
        	return 2;
        }
		else if (!ChatClient.returnOutcome.get(0) && !ChatClient.returnOutcome.get(1) && ChatClient.returnOutcome.get(2)) {
			return 3;
		}
       // Account is frozen, cannot add subscriber to waitlist
        	return 4;
    } catch (Exception e) {
        e.printStackTrace();
        return 0;
        }
    } 
}










