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

public class ServerMonitorFrameController
{
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

    /**
     * This method is called when the user clicks the "Exit" button
     */
    @FXML
    private void initialize()
    {
        // Initialize your components here if needed
        monitorTable.itemsProperty().bind(monitorListProperty);


        column1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
        column2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
        column3.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));
        column4.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(3)));
    }

    /**
     * This method is called when the user clicks the "Exit" button
     * @param event
     * @throws Exception
     */
    public void getExitBtn(ActionEvent event) throws Exception
    {
        System.out.println("exit Academic Tool");
        System.exit(0);
    }

    /**
     * The method is called when there are new clients connected to the server
     * @param host
     * @param ip
     */
    private void addRow(String host, String ip)
    {
        List<String> list = new ArrayList<>();
        list.add(String.valueOf(index++));
        list.add(ip);
        list.add(host);
        list.add("Connected");

        this.monitorList.add(list);
    }

    /**
     * The method is called when there are clients connecting from the server
     * @param client
     */
    public void clientConnected(ConnectionToClient client)
    {
        clientMap.put(client, index);
        addRow(Objects.requireNonNull(client.getInetAddress()).getHostName(), client.getInetAddress().getHostAddress());
    }

    /**
     * The method is called when there are clients disconnected from the server
     * @param client
     */
    public void clientDisconnected(ConnectionToClient client)
    {
        int index = clientMap.get(client);

        // remove the client from the table
        for (List<String> list : monitorList)
        {
            if (list.get(0).equals(String.valueOf(index)))
            {
                monitorList.remove(list);
                break;
            }
        }

        clientMap.remove(client);
    }

}