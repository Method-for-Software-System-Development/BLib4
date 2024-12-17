package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

    @FXML
    private void handleSave() {
        // Handle save logic here
        String id = txtID.getText();
        String name = txtName.getText();
        String history = txtHistory.getText();
        String phone = txtPhone.getText();
        String email = txtEmail.getText();

        // ToDo: Add logic to save the subscriber details
    }

    @FXML
    private void handleClose() {
        // Close the window
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();

        //ToDo: open the previous window
    }
}