package gui;

import client.ChatClient;
import client.ClientController;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.MessageType;
import ocsf.client.AbstractClient;

import java.io.IOException;

public class MainWindowFrameController {
	

    @FXML
    private Button btnShowAll;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnExit;

    @FXML
    private TextField txtStudentId;

    @FXML
    private void handleShowAll(ActionEvent event) throws IOException {
        // Handle the Show All button action
		FXMLLoader loader = new FXMLLoader();
		
		ClientUI.chat.accept(new MessageType("100",null));

    	((Node) event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Pane root = loader.load(getClass().getResource("/gui/ShowAllSubscribers.fxml").openStream());    	
    	ShowAllSubscribersFrameController showAllSubscribersFrameController = loader.getController();		
    	showAllSubscribersFrameController.updateSubscribersInTableView(ChatClient.subscribers);
    	
    	Scene scene = new Scene(root);
        Stage primaryStage = new Stage();
    	primaryStage.setScene(scene);
    	primaryStage.show();		
		primaryStage.setTitle("All Subscribers");
    }

    @FXML
    private void handleSearch(ActionEvent event) throws IOException {
        // Handle the Search button action
		FXMLLoader loader = new FXMLLoader();
    	String id=txtStudentId.getText();
    	if (id.trim().isEmpty()) {
            System.out.println("You must enter an id number");

        } else {
        	ClientUI.chat.accept(new MessageType("101",id));
        	if(ChatClient.subscribers.isEmpty())
			{
				System.out.println("Student ID Not Found");
				
			}
        	else {
        		((Node) event.getSource()).getScene().getWindow().hide(); //hiding primary window
        		Pane root = loader.load(getClass().getResource("/gui/EditSubscriber.fxml").openStream());    	
        	
        		EditSubscriberFrameController editSubscriberFrameController = loader.getController();		
        		editSubscriberFrameController.showSubscriberDetails(ChatClient.subscribers.get(0));
        		
        		Scene scene = new Scene(root);
        		Stage primaryStage = new Stage();
        		primaryStage.setScene(scene);
        		primaryStage.show();
        		primaryStage.setTitle("Subscriber Details");
        	}
        }
    }

    @FXML
    private void handleExit(ActionEvent event) {
        // Handle the Exit button action
        System.out.println("exit Main Windows");

        // Call quit method from client to close the connection
        ClientUI.chat.getClient().quit();

        System.exit(0);
    }

    public void start(Stage primaryStage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/gui/MainWindow.fxml"));

        Scene scene = new Scene(root);
        //scene.getStylesheets().add(getClass().getResource("/gui/AcademicFrame.css").toExternalForm());
        primaryStage.setTitle("Academic Managment Tool");
        primaryStage.setScene(scene);

        primaryStage.show();	// Start the main window
    }
}