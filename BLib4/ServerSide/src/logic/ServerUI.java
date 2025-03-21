package logic;

import gui.ServerMonitorFrameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class ServerUI extends Application
{
    private static Stage primaryStage;

    public static void main(String[] args) throws Exception
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
     */
    @Override
    public void start(Stage primaryStage)
    {
        // save the primary stage
        this.primaryStage = primaryStage;

        // Set the primary stage
        SceneManager.setStage(primaryStage);

        // Load and set the window icon
        primaryStage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/gui/assets/icons/logo_icon.png")));

        // Set initial scene
        SceneManager.switchScene("/gui/ServerPort.fxml", "BLib.4 - Connect to Server");

        // Set initial size
        primaryStage.setWidth(700);  // Initial width
        primaryStage.setHeight(600);  // Initial height

        // Disable resizing
        primaryStage.setResizable(false);

        // Show the stage
        primaryStage.show();
    }

    /**
     * The method open the server monitor window and start the server
     *
     * @param p the port number
     *
     * @throws IOException if an I/O error occurs when opening the server
     */
    public static void runServer(String p) throws IOException
    {
        int port = 0; //Port to listen on

        try
        {
            port = Integer.parseInt(p);

        }
        catch (Throwable t)
        {
            System.out.println("ERROR - Could not convert the port to int!");
        }

        // Open the server monitor window
        FXMLLoader loader = new FXMLLoader();

        Pane root = loader.load(ServerUI.class.getResource("/gui/ServerMonitor.fxml").openStream());
        ServerMonitorFrameController serverMonitorFrameController = loader.getController();

        Scene scene = new Scene(root);
        primaryStage.setTitle("Server Monitor");

        primaryStage.setScene(scene);
        primaryStage.show();


        //!/////////////////////////////////////// check this (scale the window)
//        Screen screen = Screen.getPrimary();
//        primaryStage.setWidth(screen.getBounds().getWidth());
//        primaryStage.setHeight(screen.getBounds().getHeight());
//        double scaleX = screen.getBounds().getWidth()/1920.0;
//        double scaleY = screen.getBounds().getHeight()/1080.0;
//        double scaleD = Math.min(scaleX, scaleY);
//        Scale scale = new Scale(scaleD, scaleD);
//        root.getTransforms().add(scale);
//        scale.xProperty().bind(primaryStage.widthProperty().divide(1920));
//        scale.yProperty().bind(primaryStage.heightProperty().divide(1080));

        //?/////////////////////////////////////////

        // Allow resizing
        primaryStage.setResizable(true);

        // Set initial size
        primaryStage.setWidth(1920);  // Initial width
        primaryStage.setHeight(1080);  // Initial height
        primaryStage.setMaximized(true);


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
