package logic.librarian;

import logic.communication.*;
import entities.logic.MessageType;
import java.util.ArrayList;
import logic.user.*;

import logic.user.*;

public class BorrowController {
	private static volatile BorrowController instance = null;
	private static BooksController  BookController = BooksController.getInstance();

    /*
     * Private constructor 
     */
    private BorrowController() {
    }
 
    /**
     * get the instance of the BorrowController for singleton
     * 
     * @return the instance of BorrowController
     */
    public static BorrowController getInstance() {
		if(instance == null) 
		{
			 synchronized (BorrowController.class)
	            {
	                if (instance == null)
	                {
	                    instance = new BorrowController();
	                }
	            }
		}
		return instance;
    }
    
    /**
     * Creating a new borrow for subscriber
     * 
     * @param enteredSubscriberID		Subscriber ID input via the librarian in GUI
     * @param enteredCopyBookID			Copy ID input via librarian in GUI for the book to borrow
     * @return							Return value return from query in DB
     */
    public int createNewBorrow(String enteredSubscriberID, String enteredCopyBookID ) {
        try {
            // Create a list to be sent in the message to the server
            ArrayList<String> detailsOfBorrow = new ArrayList<>();
            detailsOfBorrow.add(enteredSubscriberID);
            detailsOfBorrow.add(enteredCopyBookID);

			// Sending the server subscriberID and bookID
			ClientUI.chat.accept(new MessageType("107", detailsOfBorrow));
        }catch(Exception e) {
        	System.out.println(e.toString());
        }
        // Returning query return value
        return ChatClient.borrowHandle;
    }
    
    /**
     * Checking if Subscriber ID is in the list of ordered books
     * 
     * @param SubscriberID			Subscriber ID entered by librarian
     * @return						Return true if the subscriber is on the wait list
     */
    public boolean isSubscriberInWaitList(String SubscriberID) {
    	for(String Subid : BookController.getSubscriberIdWithOrder()) {
    		if(SubscriberID.equals(Subid)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * Extend borrow via librarian - manual extension
     * 
     * @param Borrow_id				Borrow ID input via librarian in GUI
     * @param selectedDate			Selected date for new return date
     */
    public void extendBorrow(String Borrow_id, String selectedDate ) {
    	try {
    		
    		// Building an ArrayList in order to send a MessageType Object in order to send to server
    		ArrayList<String> listToSend = new ArrayList<>();
    		listToSend.add(Borrow_id);
    		listToSend.add(selectedDate);
    		listToSend.add(ChatClient.librarian.getId());
    		
    		// Sending to the server MessageType 117
    		ClientUI.chat.accept(new MessageType("117", listToSend));
    		
    	}catch(Exception e) {
    		System.out.println(e.toString());
    	}
    }
    
    /**
     * Return of a book by subscriber 
     * 
     * @param Borrow_id				Borrow ID of the returned book to the library
     */
    public void returnBorrow(String Borrow_id) {
    	try {
    		
    		// Sending to the server MessageType 109
    		ClientUI.chat.accept(new MessageType("109", Borrow_id));
    	}catch(Exception e) {
    		System.out.println(e.toString());
    	}
    }
}
