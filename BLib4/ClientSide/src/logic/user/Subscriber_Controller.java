package logic.user;

import java.io.IOException;
import java.util.ArrayList;

import entities.logic.MessageType;
import gui.common.MainWindowFrameController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
    private Button btnSignUp;

    //*********handler to read new subscriber data and call addNewSubscriber**********
     
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
}
