package gui;

import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import logic.ClientInfo;
import ocsf.server.ConnectionToClient;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;

public class ServerMonitorFrameController
{
    private static int index = 1;
    private ObservableList<ClientInfo> monitorList = FXCollections.observableArrayList();
    private Property<ObservableList<ClientInfo>> monitorListProperty = new SimpleObjectProperty<>(monitorList);

    private Map<ConnectionToClient, Integer> clientMap = new HashMap<>();

    @FXML
    private TableView<ClientInfo> monitorTable;

    @FXML
    private TableColumn<ClientInfo, String> column1;

    @FXML
    private TableColumn<ClientInfo, String> column2;

    @FXML
    private TableColumn<ClientInfo, String> column3;

    @FXML
    private TableColumn<ClientInfo, String> column4;

    @FXML
    private Text ipLbl;

    @FXML
    private TextArea console;
    private PrintStream ps;

    /**
     * This method is called to initialize the table of active clients
     */
    @FXML
    private void initialize()
    {
        ps = new PrintStream(new Console(console));

        System.setOut(ps);
        System.setErr(ps);
        console.setEditable(false);

        monitorTable.itemsProperty().bind(monitorListProperty);
        //monitorTable.setItems(monitorList);

        column1.setCellValueFactory(cellData -> cellData.getValue().indexProperty());
        column2.setCellValueFactory(cellData -> cellData.getValue().ipProperty());
        column3.setCellValueFactory(cellData -> cellData.getValue().hostProperty());
        column4.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        try (final DatagramSocket socket = new DatagramSocket())
        {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ipLbl.setText("Server ip: " + socket.getLocalAddress().getHostAddress());
        }
        catch (Exception ignored)
        {
        }
    }

    /**
     * This class is used to redirect the output to the console
     */
    public class Console extends OutputStream
    {
        private TextArea console;

        public Console(TextArea console)
        {
            this.console = console;
        }

        public void appendText(String valueOf)
        {
            Platform.runLater(() -> console.appendText(valueOf));
        }

        public void write(int b) throws IOException
        {
            appendText(String.valueOf((char) b));
        }
    }

    /**
     * This method is called when the user clicks the "Exit" button
     *
     * @param event the event that triggered this method
     * @throws Exception if an error occurs
     */
    public void getExitBtn(ActionEvent event) throws Exception
    {
        System.out.println("exit Server Monitor Tool");
        System.exit(0);
    }

    /**
     * The method is called when there are new clients connected to the server
     * to add him to the table
     *
     * @param host the host name of the client
     * @param ip   the ip address of the client
     */
    private void addRow(String host, String ip)
    {
        ClientInfo clientInfo = new ClientInfo(String.valueOf(index++), ip, host, "Connected");
        monitorList.add(clientInfo);
        monitorTable.refresh();
    }

    /**
     * The method is called when there are clients connecting from the server to handle the table
     *
     * @param client the client that connected
     */
    public void clientConnected(ConnectionToClient client)
    {
        Platform.runLater(() ->
        {
            clientMap.put(client, index);
            addRow(Objects.requireNonNull(client.getInetAddress()).getHostName(), client.getInetAddress().getHostAddress());
            monitorTable.refresh();
            System.out.println("Client connected: " + client);
        });
    }

    /**
     * The method is called when there are clients disconnected from the server to handle the table
     *
     * @param client the client that disconnected
     */
    public void clientDisconnected(ConnectionToClient client)
    {
        System.out.println("Client disconnected: " + client);
        Platform.runLater(() ->
        {
            int index = clientMap.get(client);

            // remove the client from the table
            for (ClientInfo clientInfo : monitorList)
            {
                if (clientInfo.getIndex().equals(String.valueOf(index)))
                {
                    monitorList.remove(clientInfo);
                    monitorTable.refresh();
                    break;
                }
            }
            clientMap.remove(client);
        });
    }
}