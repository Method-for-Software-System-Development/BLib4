package logic.user;

import java.io.IOException;
import java.util.ArrayList;

import entities.logic.MessageType;
import entities.user.Librarian;
import entities.user.Subscriber;
import gui.common.MainWindowFrameController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.communication.ChatClient;
import logic.communication.ClientUI;

public class Subscriber_Controller {
	@FXML
    private TextField txtID;

    @FXML
    private TextField txtFirstName;

    @FXML
    private TextField txtLastName;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtPassword;
    
    @FXML
    private TextField txtType;
    
    @FXML
    private Button btnSignUp;
    
    @FXML
    private Button btnLogIn;
    
    private Stage currentStage;

    //handler to read new subscriber data and call addNewSubscriber
    @FXML
    private void handleSignUp(ActionEvent event) {
    	String userID = txtID.getText();
        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String phoneNumber = txtPhone.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        addNewSubscriber(userID,firstName,lastName,phoneNumber,email,password);
    }
    
    //sending new subscriber data to server
	private void addNewSubscriber(String userID, String firstName, String lastName, String phoneNumber, String email,String password) {
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
	
	 //handler to log in subscriber or librarian
    @FXML
    private void handleLogIn(ActionEvent event) throws IOException {
        currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    	String userID = txtID.getText();
        String password = txtPassword.getText();
        String type = txtType.getText();
        validate_login_Form(userID,password,type);
    }
    
    //validate log in details of subscriber or librarian
    private void validate_login_Form(String userID, String password, String type) throws IOException {
    	// Create a list of data of the new subscriber to send to the server
        ArrayList<String> dataOfLogIn = new ArrayList<>();	
        dataOfLogIn.add(userID);
        dataOfLogIn.add(password);
        dataOfLogIn.add(type);
        ClientUI.chat.accept(new MessageType("100",dataOfLogIn));
        //check user verification from db by type
        if(ChatClient.serverResponse) {
        	switch (type)
        	{
        	case "subscriber":
        		logInSubscriber(ChatClient.subscribers.get(0));
        		break;
        		
        	case "librarian":
        		logInLibrarian(ChatClient.librarian);
        		break;
        		
        	default:
        		Alert alert = new Alert(Alert.AlertType.ERROR, "User type is not valid");
        		alert.setHeaderText("Error");
        		alert.showAndWait();
        	}
        }
        else {
        	Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to log in the "+type);
			alert.setHeaderText("Error");
			alert.showAndWait();
        }
    }
    
    //*****************need to put the correct windows of subscriber and librarian after log in*************************
    
    //open window of subscriber after successful log in
	private void logInSubscriber(Subscriber subscriber) throws IOException {
		//open first window as subscriber
		currentStage.close();//hiding primary window
		Parent root = FXMLLoader.load(getClass().getResource("/gui/testing/SuccessSubscriberLogInTestingWindow.fxml"));
		
		Scene scene = new Scene(root);
		Stage primaryStage = new Stage();
	    primaryStage.setTitle("Subscriber Sign Up");
        primaryStage.setScene(scene);
        primaryStage.show();
	}
	
    //open window of librarian after successful log in
	private void logInLibrarian(Librarian librarian) throws IOException {
        //open first window as librarian
		currentStage.close();//hiding primary window
	    Parent root = FXMLLoader.load(getClass().getResource("/gui/testing/SuccessLibrarianLogInTestingWindow.fxml"));

	    Scene scene = new Scene(root);
	    Stage primaryStage = new Stage();
	    primaryStage.setTitle("Subscriber Sign Up");
        primaryStage.setScene(scene);
        primaryStage.show();
	}
}
