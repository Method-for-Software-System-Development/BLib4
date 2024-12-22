package gui;

import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.MessageType;

import java.io.IOException;

public class MainWindowFrameController
{
    public static Stage mainWindow = null;

    @FXML
    private Button btnShowAll;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnExit;

    @FXML
    private TextField txtStudentId;

    /**
     * This method is used to handle the Show All button action and open the ShowAllSubscribers window.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void handleShowAll(ActionEvent event) throws IOException
    {
        // Handle the Show All button action
        FXMLLoader loader = new FXMLLoader();

        // send message to server to get all subscribers
        ClientUI.chat.accept(new MessageType("100", null));

        // open the ShowAllSubscribers window
        ((Node) event.getSource()).getScene().getWindow().hide(); //hiding primary window
        Pane root = loader.load(getClass().getResource("/gui/ShowAllSubscribers.fxml").openStream());
        ShowAllSubscribersFrameController showAllSubscribersFrameController = loader.getController();
        showAllSubscribersFrameController.updateSubscribersInTableView(ChatClient.subscribers);

        Scene scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle("All Subscribers");

        // Set the action to be performed when the user tries to close the window
        primaryStage.setOnCloseRequest(e ->
        {
            e.consume();
            primaryStage.close();
            showMainWindow();
        });
    }

    /**
     * This method is used to handle the Search button action and open the EditSubscriber window.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void handleSearch(ActionEvent event) throws IOException
    {
        // Handle the Search button action
        FXMLLoader loader = new FXMLLoader();
        String id = txtStudentId.getText();
        if (id.trim().isEmpty())
        {
            System.out.println("You must enter an id number");
        }
        else
        {
            // send message to server to get subscriber details
            ClientUI.chat.accept(new MessageType("101", id));

            if (ChatClient.subscribers.isEmpty())
            {
                System.out.println("Student ID Not Found");

                Alert alert = new Alert(Alert.AlertType.ERROR, "Subscriber ID Not Found");
                alert.showAndWait();
            }
            else
            {
                ((Node) event.getSource()).getScene().getWindow().hide(); //hiding primary window
                Pane root = loader.load(getClass().getResource("/gui/EditSubscriber.fxml").openStream());

                EditSubscriberFrameController editSubscriberFrameController = loader.getController();
                editSubscriberFrameController.showSubscriberDetails(ChatClient.subscribers.get(0));

                Scene scene = new Scene(root);
                Stage primaryStage = new Stage();
                primaryStage.setScene(scene);
                primaryStage.show();
                primaryStage.setTitle("Subscriber Details");

                // Set the action to be performed when the user tries to close the window
                primaryStage.setOnCloseRequest(e ->
                {
                    e.consume();
                    primaryStage.close();
                    showMainWindow();
                });
            }
        }
    }

    /**
     * This method is used to handle the Exit button action and disconnect the client from the server.
     *
     * @param event
     */
    @FXML
    private void handleExit(ActionEvent event)
    {
        // Handle the Exit button action
        System.out.println("exit Main Windows");

        // Call quit method from client to close the connection
        ClientUI.chat.getClient().quit();

        System.exit(0);
    }

    /**
     * This method is used to start the main window.
     *
     * @param primaryStage
     * @throws IOException
     */
    public void start(Stage primaryStage) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("/gui/MainWindow.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setTitle("Main Window");
        primaryStage.setScene(scene);

        primaryStage.show();    // Start the main window

        mainWindow = primaryStage;

        // Set the action to be performed when the user tries to close the window
        primaryStage.setOnCloseRequest(e ->
        {
            e.consume();
            handleExit(new ActionEvent());
        });
    }


    /**
     * This method is used to show the main window.
     */
    public static void showMainWindow()
    {
        if (mainWindow != null)
        {
            mainWindow.show();
        }
    }
}