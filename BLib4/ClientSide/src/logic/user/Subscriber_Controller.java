package logic.user;

import java.util.ArrayList;
import entities.logic.MessageType;
import entities.user.Librarian;
import entities.user.Subscriber;
import javafx.scene.control.Alert;
import logic.communication.ChatClient;
import logic.communication.ClientUI;

public class Subscriber_Controller {   

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
    	if(ChatClient.serverResponse)
    		return ChatClient.subscribers.get(0);
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
    	if(ChatClient.serverResponse)
    		return ChatClient.librarian;
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
}
