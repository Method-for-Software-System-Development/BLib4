package gui;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ocsf.server.ConnectionToClient;

import java.util.*;

public class ServerMonitorFrameController {
    private static int index = 1;
    private ObservableList<List<String>> monitorList = FXCollections.observableArrayList();
    private Property<ObservableList<List<String>>> monitorListProperty = new SimpleObjectProperty<>(monitorList);
    private Map<ConnectionToClient, Integer> clientMap = new HashMap<>();


    @FXML
    private TableView<List<String>> monitorTable;

    @FXML
    private TableColumn<List<String>, String> column1;

    @FXML
    private TableColumn<List<String>, String> column2;

    @FXML
    private TableColumn<List<String>, String> column3;

    @FXML
    private TableColumn<List<String>, String> column4;
    @FXML
    private Button monitorButton;

    @FXML
    private void initialize() {
        // Initialize your components here if needed
        monitorTable.itemsProperty().bind(monitorListProperty);


        column1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
        column2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
        column3.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));
        column4.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(3)));
    }


    public void getExitBtn(ActionEvent event) throws Exception {
        System.out.println("exit Academic Tool");
        System.exit(0);
    }

    //ToDo: add handle for update the table

    private void addRow(String host, String ip) {
        List<String> list = new ArrayList<>();
        list.add(String.valueOf(index++));
        list.add(host);
        list.add(ip);
        list.add("Connected");

        this.monitorList.add(list);

        System.out.println(this.monitorList);
    }


    public void clientConnected(ConnectionToClient client) {
        clientMap.put(client, index);
        addRow(Objects.requireNonNull(client.getInetAddress()).getHostName(), client.getInetAddress().getHostAddress());
    }

    public void clientDisconnected(ConnectionToClient client) {
        //System.out.println("Client Disconnected");
        int index = clientMap.get(client);
        this.monitorList.remove(index);
        clientMap.remove(client);
    }


}