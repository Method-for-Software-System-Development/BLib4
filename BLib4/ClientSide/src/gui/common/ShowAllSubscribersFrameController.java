package gui.common;

import java.util.List;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import entities.user.Subscriber;


public class ShowAllSubscribersFrameController
{
    private ObservableList<Subscriber> SubscribersList = FXCollections.observableArrayList();
    private Property<ObservableList<Subscriber>> SubscribersListProperty = new SimpleObjectProperty<>(SubscribersList);

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

    /**
     * This method is used to initialize the window and set up the table.
     */
    @FXML
    private void initialize()
    {
        tableSubscribers.itemsProperty().bind(SubscribersListProperty);

        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colHistory.setCellValueFactory(new PropertyValueFactory<>("subscriptionHistory"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    /**
     * This method is used to update the subscribers list when new data arrives.
     *
     * @param subscribers A list of Subscriber objects to be displayed in the table.
     */
    public void updateSubscribersInTableView(List<Subscriber> subscribers)
    {
        if (subscribers != null && !subscribers.isEmpty())
        {
            SubscribersList.clear();  // Clear existing data
            SubscribersList.addAll(subscribers);  // Add the new subscribers to the list
            tableSubscribers.refresh();  // Manually refresh the table to update the UI
        }
    }

    /**
     * This method is used to close the window and return to the main menu.
     */
    @FXML
    private void handleClose()
    {
        // Close the window
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();

        // open the main menu
        MainWindowFrameController.showMainWindow();
    }
}