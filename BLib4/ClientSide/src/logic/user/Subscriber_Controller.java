package logic.user;

import java.sql.Date;
import java.util.ArrayList;
import entities.logic.MessageType;
import entities.user.Librarian;
import entities.user.Subscriber;
import javafx.scene.control.Alert;
import logic.communication.ChatClient;
import logic.communication.ClientUI;

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
	public void addNewSubscriber(String userID, String firstName, String lastName, String phoneNumber, String email,String password) {
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
        // check server response and show alert
        if (ChatClient.serverResponse)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Subscriber signed up successfully");
            alert.setHeaderText("Success");
            alert.showAndWait();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to sign up the subscriber");
            alert.setHeaderText("Error");
            alert.showAndWait();
        }   
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
    		return ChatClient.subscribers.get(0);
    	}
    	return null;    }

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
    	// Create a list of data of the new user to send to the server
        ArrayList<String> dataOfLogIn = new ArrayList<>();
        dataOfLogIn.add(userID);
        dataOfLogIn.add(password);
        dataOfLogIn.add(type);
        ClientUI.chat.accept(new MessageType("100",dataOfLogIn));
    }
    
    /**
     * This method sends a request to the server to update the given subscriber
     * 
     * @param userID ID of the subscriber.
     * @param firstName	first name of the subscriber.
     * @param lastName	last name of the subscriber.
     * @param phoneNumber phone number of the subscriber.
     * @param email email of the subscriber.
     * @param status status of the subscriber user.
     * @return return the updated subscriber if update succeed and null if not.
     */
    public Subscriber updateSubscriberDetails(String userID, String firstName, String lastName, String phoneNumber, String email, Boolean status) {
    	//create a new subscriber with the updates and send to server
    	Subscriber subscriber= new Subscriber(userID,firstName,lastName,phoneNumber,email,status);
        ClientUI.chat.accept(new MessageType("113",subscriber));
        //update succeed
        if(ChatClient.serverResponse) {
        	Alert alert = new Alert(Alert.AlertType.INFORMATION, "Subscriber updated successfully");
            alert.setHeaderText("Success");
            alert.showAndWait();
        	return subscriber;
        }
        //update failed
        Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to update the subscriber");
        alert.setHeaderText("Error");
        alert.showAndWait();
		return null;    	
    }
    /**
     * This method send a request to server to extend a borrow by subscriber
     * 
     * @param subscriberID ID of subscriber.
     * @param copyID ID of the copy of book.
     * @param newReturnDate the return date after extend.
     * @return returns if extension succeed or not.
     */
    public boolean extendBorrowBySubscriber(String subscriberID,String copyID,Date newReturnDate) {
    	//Get the borrow ID from server
    	String borrowID=null;
    	ArrayList<ArrayList<String>>listOfBorrows=ChatClient.listOfBorrows;
        ClientUI.chat.accept(new MessageType("110",subscriberID));
        //Search for the borrow ID in the list of borrows of subscriber
        for (int i = 0; i < listOfBorrows.size(); i++) {
        	if(listOfBorrows.get(i).get(1).equals(copyID)) {
        		borrowID=listOfBorrows.get(i).get(0);
        		break;
        	}
        }
        //copy ID not found in list of borrows
        if(borrowID==null) {
        	Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to extend the borrow-copy is not borrowed");
            alert.setHeaderText("Error");
            alert.showAndWait();
            return false;
        }	
      	// Create a list of data of the borrow extension and send to server
        ArrayList<String> dataOfExtension = new ArrayList<>();
        dataOfExtension.add(borrowID);
        dataOfExtension.add(newReturnDate.toString());
        ClientUI.chat.accept(new MessageType("111",dataOfExtension));
        // return server response
        return ChatClient.serverResponse;
    }
}

