package gui;

import java.io.IOException;

import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.MessageType;
import logic.Subscriber;

public class EditSubscriberFrameController {

    @FXML
    private TextField txtID;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtHistory;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtEmail;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnClose;

    @FXML
    private void initialize() {
        // Initialize any required logic here
    }
    

    public void showSubscriberDetails(Subscriber subscriber) {
    // Populate the UI with the subscriber details
    	txtID.setText(""+subscriber.getId());
    	txtName.setText(subscriber.getName());
    	txtHistory.setText(""+subscriber.getSubscriptionHistory());
    	txtPhone.setText(subscriber.getPhoneNumber());
    	txtEmail.setText(subscriber.getEmail());
    }

    @FXML
    private void handleSave(ActionEvent event) {
        // Handle save logic here
        String id = txtID.getText();
        String name = txtName.getText();
        String history = txtHistory.getText();
        String phone = txtPhone.getText();
        String email = txtEmail.getText();
        
        // Update the subscriber details in db
    	ClientUI.chat.accept(new MessageType("102",new Subscriber(Integer.parseInt(id),name,Integer.parseInt(history),phone,email)));
    }

    @FXML
    private void handleClose(ActionEvent event) throws IOException {
        // Close the window
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();

        //ToDo: open the previous window
        ((Node) event.getSource()).getScene().getWindow().hide(); //hiding primary window
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/SubscriberDetails.fxml"));
    	Parent root = loader.load();
    	Scene scene = new Scene(root);
    	Stage primaryStage = new Stage();
    	primaryStage.setScene(scene);
    	primaryStage.show();
    	primaryStage.setTitle("Subscriber Details");    
    }
}