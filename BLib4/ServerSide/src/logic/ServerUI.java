package logic;

import gui.ServerMonitorFrameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import entities.user.Subscriber;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Vector;

import gui.ServerPortFrameController;

public class ServerUI extends Application
{
    public static Vector<Subscriber> subscribers = new Vector<Subscriber>();


    public static void main(String args[]) throws Exception
    {
        launch(args);
    } // end main

    /**
     * This method starts the application
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        // TODO Auto-generated method stub
        ServerPortFrameController aFrame = new ServerPortFrameController(); // create StudentFrame

        aFrame.start(primaryStage);
    }

    /**
     * The method open the server monitor window and start the server
     *
     * @param p the port number
     * @throws IOException
     */
    public static void runServer(String p) throws IOException
    {
        int port = 0; //Port to listen on

        try
        {
            port = Integer.parseInt(p); //Set port to 5555

        }
        catch (Throwable t)
        {
            System.out.println("ERROR - Could not connect!");
        }

        // Open the server monitor window
        Stage primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader();

        Pane root = loader.load(ServerUI.class.getResource("/gui/ServerMonitor.fxml").openStream());
        ServerMonitorFrameController serverMonitorFrameController = loader.getController();

        Scene scene = new Scene(root);
        primaryStage.setTitle("Server Monitor");

        primaryStage.setScene(scene);
        primaryStage.show();

        // Set the action to be performed when the user tries to close the window
        primaryStage.setOnCloseRequest(e ->
        {
            e.consume();
            System.exit(0);
        });

        // Create a new ServerController to start the server
        ServerController sv = new ServerController(port, serverMonitorFrameController);

        try
        {
            sv.listen(); //Start listening for connections
        }
        catch (Exception ex)
        {
            System.out.println("ERROR - Could not listen for clients!");
        }
    }
}
