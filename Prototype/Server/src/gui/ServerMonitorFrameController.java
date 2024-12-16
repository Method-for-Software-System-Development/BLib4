package gui;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import logic.Subscriber;

import java.util.ArrayList;
import java.util.List;

public class ServerMonitorFrameController {
    private static int index = 1;
    private ObservableList<List<String>> monitorList = FXCollections.observableArrayList();
    private Property<ObservableList<List<String>>> monitorListProperty = new SimpleObjectProperty<>(monitorList);

    @FXML
    private TableView<List<String>> monitorTable;

    @FXML
    private TableColumn<?, String> column1;

    @FXML
    private TableColumn<?, String> column2;

    @FXML
    private TableColumn<?, String> column3;

    @FXML
    private TableColumn<?, String> column4;
    @FXML
    private Button monitorButton;

    @FXML
    private void initialize() {
        // Initialize your components here if needed
        monitorTable.itemsProperty().bind(monitorListProperty);
    }


    public void getExitBtn(ActionEvent event) throws Exception {
        System.out.println("exit Academic Tool");
        System.exit(0);
    }

    //ToDo: add handle for update the table

    public void addRow(String host, String ip, String status) {
        List<String> list = new ArrayList<>();
        list.add(String.valueOf(index++));
        list.add(host);
        list.add(ip);
        list.add(status);

        this.monitorList.add(list);

        //monitorTable.getItems().add(list);


        //ToDo: use ObservableList to update the table
    }

}