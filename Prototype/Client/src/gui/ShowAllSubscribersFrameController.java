package gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import client.ChatClient;
import client.ClientUI;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import logic.MessageType;
import logic.Subscriber;
import ocsf.server.ConnectionToClient;

public class ShowAllSubscribersFrameController {
	private ObservableList<Subscriber> SubscribersList = FXCollections.observableArrayList();
	private Property<ObservableList<Subscriber>> SubscribersListProperty = new SimpleObjectProperty<>(SubscribersList);
    

    private ChatClient chatClient;
    
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
    	tableSubscribers.itemsProperty().bind(SubscribersListProperty);

        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colHistory.setCellValueFactory(new PropertyValueFactory<>("history"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        // ToDo: Load data into the table (maybe create method that will be called after receive the message)
    }
    
    /**
     * This method is used to update the subscribers list when new data arrives.
     * 
     * @param subscribers A list of Subscriber objects to be displayed in the table.
     */
    public void updateSubscribersInTableView(List<Subscriber> subscribers) {
        if (subscribers != null && !subscribers.isEmpty()) {
            SubscribersList.clear();  // Clear existing data
            SubscribersList.addAll(subscribers);  // Add the new subscribers to the list
            tableSubscribers.refresh();  // Manually refresh the table to update the UI
        }
    }

    @FXML
    private void handleClose() {
        // Close the window
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
}