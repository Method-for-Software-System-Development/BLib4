package gui.common.clientConnection;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import logic.communication.ClientUI;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Controller class for managing the client connection UI.
 * Handles input validation, setting default values, and initiating the connection process.
 */
public class ClientConnection_Controller
{

    @FXML
    private TextField ipTextField;

    @FXML
    private TextField portTextField;

    /**
     * Sets the IP address field to the machine's local IP address.
     * If unable to fetch the IP, displays an error message in the prompt text.
     */
    @FXML
    private void handleSetMyIP()
    {
        try
        {
            String myIp = InetAddress.getLocalHost().getHostAddress();
            ipTextField.setText(myIp);
        }
        catch (UnknownHostException e)
        {
            ipTextField.setPromptText("Error fetching IP");
            ipTextField.getStyleClass().add("error-text-field");
        }
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
     * If validation passes, try to initiate the connection.
     * Displays an error alert in case of failure.
     */
    @FXML
    public void validate_connect_form()
    {
        // Reset styles before validation
        ipTextField.getStyleClass().remove("error-text-field");
        portTextField.getStyleClass().remove("error-text-field");

        boolean isValid = true;

        // Store values in variables
        String ipAddress = ipTextField.getText();
        String port = portTextField.getText();

        // Validate IP address field
        if (ipAddress == null || ipAddress.trim().isEmpty())
        {
            ipTextField.setPromptText("IP address is required");
            ipTextField.getStyleClass().add("error-text-field");
            isValid = false;
        }

        // Validate port field
        if (port == null || port.trim().isEmpty())
        {
            portTextField.setPromptText("Server port number is required");
            portTextField.getStyleClass().add("error-text-field");
            isValid = false;
        }

        if (isValid)
        {
            try
            {
                ClientUI.runClient(ipAddress, port);
            }
            catch (Exception e)
            {
                showErrorAlert("Connection Error", "Failed to connect to the server. Please try again.");
            }
        }
    }

    /**
     * Displays an error alert dialog with the provided title and message.
     *
     * @param title   the title of the alert
     * @param message the content message of the alert
     */
    private void showErrorAlert(String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}