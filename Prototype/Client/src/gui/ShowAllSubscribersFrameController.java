package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import logic.Subscriber;

public class ShowAllSubscribersFrameController {

    @FXML
    private TableView<Subscriber> tableSubscribers;

    @FXML
    private TableColumn<Subscriber, String> colID;

    @FXML
    private TableColumn<Subscriber, String> colName;

    @FXML
    private TableColumn<Subscriber, String> colHistory;

    @FXML
    private TableColumn<Subscriber, String> colPhone;

    @FXML
    private TableColumn<Subscriber, String> colEmail;

    @FXML
    private Button btnClose;

    @FXML
    private void initialize() {
        //ToDo: check if ok, if not copy from the server monitor (the table work there)

        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colHistory.setCellValueFactory(new PropertyValueFactory<>("history"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        // ToDo: Load data into the table (maybe create method that will be called after receive the message)
    }

    @FXML
    private void handleClose() {
        // Close the window
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
}