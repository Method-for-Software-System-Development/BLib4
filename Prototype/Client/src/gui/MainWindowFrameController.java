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
import ocsf.client.AbstractClient;

import java.io.IOException;

public class MainWindowFrameController {

    private ChatClient chatClient;

    public void setChatClient(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

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
    	((Node) event.getSource()).getScene().getWindow().hide(); //hiding primary window
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ShowAllSubscribers.fxml"));
    	Parent root = loader.load();
    	Scene scene = new Scene(root);
        Stage primaryStage = new Stage();
    	primaryStage.setScene(scene);
    	primaryStage.show();
        primaryStage.setTitle("All Subscribers");

        //ToDo: open new window with all students (table)
    }

    @FXML
    private void handleSearch(ActionEvent event) throws IOException {
        // Handle the Search button action
        //ToDo: open new window with student details
    	String id=txtStudentId.getText();
    	if (id.trim().isEmpty()) {
            System.out.println("You must enter an id number");

        } else {
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