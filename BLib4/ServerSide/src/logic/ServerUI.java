package logic;

import gui.ServerMonitorFrameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
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
    private static Stage primaryStage;

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

        // update primary stage


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
        FXMLLoader loader = new FXMLLoader();

        Pane root = loader.load(ServerUI.class.getResource("/gui/ServerMonitor.fxml").openStream());
        ServerMonitorFrameController serverMonitorFrameController = loader.getController();

        Scene scene = new Scene(root);
        primaryStage.setTitle("Server Monitor");

        primaryStage.setScene(scene);
        primaryStage.show();
        openHomePage(primaryStage);
        // Set the action to be performed when the user tries to close the window
        primaryStage.setOnCloseRequest(e ->
        {
            e.consume();
            System.exit(0);
        });


        // get the controller of the server monitor window
//        FXMLLoader loader = new FXMLLoader(ServerUI.class.getResource("/gui/ServerMonitor.fxml"));
//        Parent root = loader.load();
//        ServerMonitorFrameController serverMonitorFrameController = loader.getController();
//
//        openHomePage(SceneManager.getStage());

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

    /**
     * Initializes and displays the home page with necessary settings.
     *
     * @param primaryStage The primary stage of the application.
     */
    public static void openHomePage(Stage primaryStage) {
        // Set the scene for the home page
        //SceneManager.switchScene("/gui/ServerMonitor.fxml", "BLib.4 - Braude Library Management");



        // Allow resizing
        primaryStage.setResizable(true);

        // Set size constraints
        primaryStage.setMinWidth(1280);  // Minimum width
        primaryStage.setMinHeight(720); // Minimum height

        // Variables to store window size
//        final double[] lastWidth = {primaryStage.getWidth()};
//        final double[] lastHeight = {primaryStage.getHeight()};

//        // Add listener to track changes in full-screen state
//        primaryStage.fullScreenProperty().addListener((observable, oldValue, isFullScreen) -> {
//            if (!isFullScreen) {
//                // Restore previous size when exiting full-screen
//                primaryStage.setWidth(lastWidth[0]);
//                primaryStage.setHeight(lastHeight[0]);
//            } else {
//                // Save the size before entering full-screen
//                lastWidth[0] = primaryStage.getWidth();
//                lastHeight[0] = primaryStage.getHeight();
//            }
//        });


        // Set initial size
        primaryStage.setWidth(1760);  // Initial width
        primaryStage.setHeight(990);  // Initial height

        // Set full screen initially
//        primaryStage.setFullScreen(true);
//        primaryStage.setFullScreenExitHint("Press ESC to exit full-screen mode");

//        // Add listener to toggle full screen when maximized
//        primaryStage.maximizedProperty().addListener((observable, oldValue, isMaximized) -> {
//            if (isMaximized) {
//                primaryStage.setFullScreen(true);
//            }
//        });

    }
}
