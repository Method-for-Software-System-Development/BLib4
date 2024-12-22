package gui;

import client.ClientUI;
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

public class ClientPortFrameController
{

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

    private String getport()
    {
        return portxt.getText();
    }

    private String getip()
    {
        return iptxt.getText();
    }

    /**
     * This method is used to handle the Done button action and open the Client main window.
     *
     * @param event
     * @throws Exception
     */
    public void DoneClient(ActionEvent event) throws Exception
    {
        String p;
        String ip;

        p = getport();
        ip = getip();

        if (p.trim().isEmpty())
        {
            System.out.println("You must enter a port number");
        }
        else
        {
            ((Node) event.getSource()).getScene().getWindow().hide(); //hiding primary window
            ClientUI.runClient(ip, p);
        }
    }

    /**
     * The method is used to start the ClientPort window.
     *
     * @param primaryStage
     * @throws Exception
     */
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("/gui/ClientPort.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/gui/ClientPort.css").toExternalForm());
        primaryStage.setTitle("Client");
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    /**
     * This method is used to handle the Exit button action.
     *
     * @param event
     * @throws Exception
     */
    public void getExitBtn(ActionEvent event) throws Exception
    {
        System.out.println("exit Academic Tool");
        System.exit(0);
    }

}