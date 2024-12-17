package server;

import gui.ServerMonitorFrameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.Subscriber;

import java.io.IOException;
import java.util.Vector;
import gui.ServerPortFrameController;

public class ServerUI extends Application {
    final public static int DEFAULT_PORT = 5555;
    public static Vector<Subscriber> subscribers =new Vector<Subscriber>();


    public static void main( String args[] ) throws Exception
    {
        launch(args);
    } // end main

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        ServerPortFrameController aFrame = new ServerPortFrameController(); // create StudentFrame

        aFrame.start(primaryStage);
    }

    public static void runServer(String p) throws IOException {
        int port = 0; //Port to listen on

        try
        {
            port = Integer.parseInt(p); //Set port to 5555

        }
        catch(Throwable t)
        {
            System.out.println("ERROR - Could not connect!");
        }

        Stage primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader();

        Pane root = loader.load(ServerUI.class.getResource("/gui/ServerMonitor.fxml").openStream());
        ServerMonitorFrameController serverMonitorFrameController = loader.getController();

        Scene scene = new Scene(root);
        //?scene.getStylesheets().add(getClass().getResource("/gui/ServerMonitor.css").toExternalForm());
        primaryStage.setTitle("Server Monitor");

        primaryStage.setScene(scene);
        primaryStage.show();

        ServerController sv = new ServerController(port,serverMonitorFrameController);

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
