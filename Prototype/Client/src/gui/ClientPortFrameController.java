package gui;

import client.ClientUI;
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


public class ClientPortFrameController {

    String temp = "";

    @FXML
    private Button btnExit = null;
    @FXML
    private Button btnDone = null;
    @FXML
    private Label lbllist;

    @FXML
    private TextField portxt;

    @FXML
    private TextField iptxt;

    ObservableList<String> list;

    private String getport() {
        return portxt.getText();
    }

    private String getip() {
        return iptxt.getText();
    }

    public void DoneClient(ActionEvent event) throws Exception {
        String p;
        String ip;

        p = getport();
        ip = getip();

        if (p.trim().isEmpty()) {
            System.out.println("You must enter a port number");

        } else {
            ((Node) event.getSource()).getScene().getWindow().hide(); //hiding primary window
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            ClientUI.runClient(ip, p);
        }
    }

    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/gui/ClientPort.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/gui/ClientPort.css").toExternalForm());
        primaryStage.setTitle("Client");
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public void getExitBtn(ActionEvent event) throws Exception {
        System.out.println("exit Academic Tool");
        System.exit(0);
    }

}