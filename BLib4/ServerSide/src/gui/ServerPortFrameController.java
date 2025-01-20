package gui;

import javafx.scene.control.Alert;
import logic.ServerUI;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ServerPortFrameController
{
    String temp = "";

    @FXML
    private TextField portTextField;

    @FXML
    ObservableList<String> list;

    private String getport()
    {
        return portTextField.getText();
    }


    /**
     * This method is called to start the port window
     *
     * @param primaryStage
     * @throws Exception
     */
    public void start(Stage primaryStage) throws Exception
    {
        // ToDo: fix open the server port window
        Parent root = FXMLLoader.load(getClass().getResource("/gui/ServerPort.fxml"));

        Scene scene = new Scene(root);

        primaryStage.setTitle("Server");
        primaryStage.setScene(scene);

        primaryStage.show();

        // Set the action to be performed when the user tries to close the window
        primaryStage.setOnCloseRequest(e ->
        {
            e.consume();
            getExitBtn(new ActionEvent());
        });

    }

    /**
     * This method is called when the user clicks the "Exit" button
     *
     * @param event
     */
    public void getExitBtn(ActionEvent event)
    {
        System.out.println("exit Server port");
        System.exit(0);
    }

    /**
     * Sets the port field to the default port value (5555).
     */
    @FXML
    private void handleSetDefaultPort()
    {
        portTextField.setText("5555");
    }

    /**
     * Validates the IP address and port fields.
     * Ensures both fields are not empty before attempting to connect.
     * If validation passes, tries to initiate the connection.
     * Displays an error alert in case of failure.
     */
    @FXML
    public void validate_connect_form(ActionEvent event)
    {
        // Reset styles before validation
        portTextField.getStyleClass().remove("error-text-field");

        boolean isValid = true;

        // Store values in variables
        String port = portTextField.getText();

        // Validate port field
        if (port == null || port.trim().isEmpty())
        {
            portTextField.setPromptText("Server port number is required");
            portTextField.getStyleClass().add("error-text-field");
            isValid = false;
        }

        // start the server
        if (isValid)
        {
            try
            {
                ServerUI.runServer(port);

                // hide port window
                //((Node) event.getSource()).getScene().getWindow().hide();
            }
            catch (Exception e)
            {
                showErrorAlert("Connection Error", "Failed to start the server. Please try again.");
            }
        }
    }


    /**
     * Displays an error alert dialog with the provided title and message.
     *
     * @param title   the title of the alert
     * @param message the content message of the alert
     */
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}