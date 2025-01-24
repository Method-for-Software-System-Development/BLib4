package logic.user;

import java.sql.Date;
import java.util.ArrayList;
import entities.logic.MessageType;
import entities.user.Librarian;
import entities.user.Subscriber;
import javafx.scene.control.Alert;
import logic.communication.ChatClient;
import logic.communication.ClientUI;
import logic.communication.SceneManager;

public class Subscriber_Controller {   

    private static Subscriber_Controller instance = null;
    public static Subscriber loggedSubscriber=null;
    public static Librarian loggedLibrarian=null;
    
	public static Subscriber_Controller getInstance() {
	    if(instance == null) {
	    	instance = new Subscriber_Controller();
	    	return instance;
	    }
	    return instance;
	}

    public Subscriber getLoggedSubscriber() {
        return loggedSubscriber;
    }

    public Librarian getLoggedLibrarian() {
        return loggedLibrarian;
    }

    /**
     * This method send a request to the server to sign up a new subscriber.
     *
     * @param userID ID of the user.
     * @param firstName	first name of the user.
     * @param lastName	last name of the user.
     * @param phoneNumber phone number of the user.
     * @param email email of the user.
     * @param password	password of the user.
     */
	public boolean addNewSubscriber(String userID, String firstName, String lastName, String phoneNumber, String email,String password) {
        // Create a list of data of the new subscriber to send to the server
        ArrayList<String> dataOfNewSubscriber = new ArrayList<>();	
        dataOfNewSubscriber.add(userID);
        dataOfNewSubscriber.add(firstName);
        dataOfNewSubscriber.add(lastName);
        dataOfNewSubscriber.add(phoneNumber);
        dataOfNewSubscriber.add(email);
        dataOfNewSubscriber.add(password);
        // send message to server to get all subscribers
        ClientUI.chat.accept(new MessageType("104",dataOfNewSubscriber));
        // Return server response
        return ChatClient.serverResponse;
	}

	/**
     * This method creates a log in request to the server of subscriber
     * 
     * @param userID ID of the user.
     * @param password	password of the user.
	 * @return	the subscriber if log in succeed and null if not.
	 */
    public Subscriber attemptLoginAsSubscriber(String userID, String password)
    {
    	//send a request to the server to log in the subscriber
    	attemptLogin(userID,password,"subscriber");
        // check server response
    	if(ChatClient.serverResponse) {
    		loggedSubscriber=ChatClient.subscribers.get(0);
            ClientUI.chat.accept(new MessageType("122",loggedSubscriber.getId()));
    		return ChatClient.subscribers.get(0);
    	}
    	return null;
    }

    /**
     * This method creates a log in request to the server of librarian.
     * 
     * @param userID ID of the user.
     * @param password	password of the user.
     * @return the librarian if log in succeed and null if not.
     */
    public Librarian attemptLoginAsLibrarian(String userID, String password)
    {
    	//send a request to the server to log in the librarian
    	attemptLogin(userID,password,"librarian");
        // check server response
    	if(ChatClient.serverResponse) {
    		loggedLibrarian=ChatClient.librarian;
    		return ChatClient.librarian;
    	}
    	return null;
    }
    
    /**
     * This method send a request to the server to log in a user of the given type.
     *
     * @param userID ID of the user.
     * @param password	password of the user.
     * @param type	type of the user (librarian/subscriber).
     */
    private void attemptLogin(String userID, String password, String type){
    	// Create a list of data of the user to send to the server
        ArrayList<String> dataOfLogIn = new ArrayList<>();
        dataOfLogIn.add(userID);
        dataOfLogIn.add(password);
        dataOfLogIn.add(type);
        ClientUI.chat.accept(new MessageType("100",dataOfLogIn));
    }
    
    /**
     * This method sends a request to the server to log in a subscriber by his reader card
     * @param userCode the code of the subscriber card
     * @return the subscriber if log in succeed and null if not
     */
    public Subscriber attemptSubscriberLogInByCard(String userCode) {
    	//send a request to the server to log in the subscriber
        ClientUI.chat.accept(new MessageType("101",userCode));
        // check server response
        if(ChatClient.serverResponse) {
    		loggedSubscriber=ChatClient.subscribers.get(0);
            ClientUI.chat.accept(new MessageType("122",loggedSubscriber.getId()));
    		return ChatClient.subscribers.get(0);
    	}
    	return null;
    }
    
    /**
     * The method sends a log out request from a user
     */
    public void attemptLogOut() {
    	String userType;
    	String userID;
    	//check user type, save the id and delete logged user
    	if (loggedSubscriber!=null) {
    		userType="subscriber";
    		userID=loggedSubscriber.getId();
    		loggedSubscriber=null;
    	}
    	else {
    		userType="librarian";
    		userID=loggedLibrarian.getId();
    		loggedLibrarian=null;
    	}
    	// Create a list of data of the user to send to the server
    	ArrayList<String> dataOfLogOut = new ArrayList<>();
        dataOfLogOut.add(userType);
        dataOfLogOut.add(userID);
        ClientUI.chat.accept(new MessageType("1002",dataOfLogOut));
        //switch to home page
        SceneManager.switchScene("/gui/common/homePage/HomePage_UI.fxml", "BLib.4 - Braude Library Management");
    }

    /**
     * Updates the details of a subscriber.
     *
     * @param userID      The unique identifier of the subscriber whose details are to be updated.
     * @param firstName   The updated first name of the subscriber.
     * @param lastName    The updated last name of the subscriber.
     * @param phoneNumber The updated phone number of the subscriber.
     * @param email       The updated email address of the subscriber.
     * @param status      The updated status of the subscriber (e.g., active or inactive).
     * @return {@code true} if the update was successful, {@code false} otherwise.
     *
     * <p>This method creates a new {@link Subscriber} object with the updated details and sends it to the server
     * for processing. The server response determines the success of the operation.</p>
     */
    public boolean updateSubscriberDetails(String userID, String firstName, String lastName, String phoneNumber, String email, Boolean status) {
        // Create a new subscriber with the updates and send to server
        Subscriber subscriber = new Subscriber(userID, firstName, lastName, phoneNumber, email, status);
        ClientUI.chat.accept(new MessageType("113", subscriber));
        // Return server response
        return ChatClient.serverResponse;
    }

    /**
     * Updates the password of a subscriber.
     *
     * @param userID   The unique identifier of the subscriber whose password is to be updated.
     * @param password The new password to be set for the subscriber.
     * @return {@code true} if the password update was successful, {@code false} otherwise.
     *
     * <p>This method creates a data structure containing the subscriber's ID and the new password,
     * then sends it to the server for processing. The server response determines the success of the operation.</p>
     */
    public boolean updateSubscriberPassword(String userID, String password) {
        // Create a list of data for the password update and send to the server
        ArrayList<String> dataOfPasswordUpdate = new ArrayList<>();
        dataOfPasswordUpdate.add(userID);
        dataOfPasswordUpdate.add(password);
        ClientUI.chat.accept(new MessageType("114", dataOfPasswordUpdate));
        // Return server response
        return ChatClient.serverResponse;
    }

    /**
     * Attempts to extend the return date of a borrow by the subscriber.
     *
     * @param borrowID      The ID of the borrow to be extended.
     * @param newReturnDate The new return date requested for the borrow.
     * @return true if the server approves the extension, false otherwise.
     */
    public boolean extendBorrowBySubscriber(String borrowID, Date newReturnDate) {
        // Create a list of data for the borrow extension and send to the server
        ArrayList<String> dataOfExtension = new ArrayList<>();
        dataOfExtension.add(borrowID);
        dataOfExtension.add(newReturnDate.toString());
        ClientUI.chat.accept(new MessageType("111", dataOfExtension));
        // Return server response
        return ChatClient.serverResponse;
    }
}

