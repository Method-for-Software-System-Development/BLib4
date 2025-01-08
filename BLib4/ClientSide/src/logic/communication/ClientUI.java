package logic.communication;

import gui.common.*;
import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ClientUI extends Application
{
    public static ClientController chat; //only one instance

    /**
     * This method is the entry point when the program is run as an application.
     *
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception
    {
        launch(args);
    } // end main

    // ********** OLD START METHOD - USE IT FOR TESTING YOUR CODE ("COMMENT" THE NEW START METHOD) **********

//    /**
//     * This method is called when the program is run as an application.
//     *
//     * @param primaryStage
//     * @throws Exception
//     */
//    @Override
//    public void start(Stage primaryStage) throws Exception
//    {
//        ClientPortFrameController aFrame = new ClientPortFrameController(); // create StudentFrame
//
//        aFrame.start(primaryStage);
//    }

    @Override
    public void start(Stage primaryStage) {
        // Loading Montserrat fonts
        Font.loadFont(getClass().getResourceAsStream("/gui/assets/fonts/Montserrat/Montserrat-Black.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/gui/assets/fonts/Montserrat/Montserrat-BlackItalic.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/gui/assets/fonts/Montserrat/Montserrat-Bold.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/gui/assets/fonts/Montserrat/Montserrat-BoldItalic.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/gui/assets/fonts/Montserrat/Montserrat-ExtraBold.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/gui/assets/fonts/Montserrat/Montserrat-ExtraBoldItalic.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/gui/assets/fonts/Montserrat/Montserrat-ExtraLight.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/gui/assets/fonts/Montserrat/Montserrat-ExtraLightItalic.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/gui/assets/fonts/Montserrat/Montserrat-Italic.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/gui/assets/fonts/Montserrat/Montserrat-Light.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/gui/assets/fonts/Montserrat/Montserrat-LightItalic.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/gui/assets/fonts/Montserrat/Montserrat-Medium.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/gui/assets/fonts/Montserrat/Montserrat-MediumItalic.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/gui/assets/fonts/Montserrat/Montserrat-Regular.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/gui/assets/fonts/Montserrat/Montserrat-SemiBold.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/gui/assets/fonts/Montserrat/Montserrat-SemiBoldItalic.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/gui/assets/fonts/Montserrat/Montserrat-Thin.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/gui/assets/fonts/Montserrat/Montserrat-ThinItalic.ttf"), 14);

        // Loading Roboto fonts
        Font.loadFont(getClass().getResourceAsStream("/gui/assets/fonts/Roboto/Roboto-Black.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/gui/assets/fonts/Roboto/Roboto-BlackItalic.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/gui/assets/fonts/Roboto/Roboto-Bold.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/gui/assets/fonts/Roboto/Roboto-BoldItalic.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/gui/assets/fonts/Roboto/Roboto-Light.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/gui/assets/fonts/Roboto/Roboto-LightItalic.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/gui/assets/fonts/Roboto/Roboto-Medium.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/gui/assets/fonts/Roboto/Roboto-MediumItalic.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/gui/assets/fonts/Roboto/Roboto-Regular.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/gui/assets/fonts/Roboto/Roboto-Thin.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/gui/assets/fonts/Roboto/Roboto-ThinItalic.ttf"), 14);

        // Set the primary stage
        SceneManager.setStage(primaryStage);

        // Load and set the window icon
        primaryStage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/gui/assets/icons/logo_icon.png")));

        // Set initial scene
        SceneManager.switchScene("/gui/common/homePage/HomePage_UI.fxml", "BLib.4 - Braude Library Management");

        // Set size constraints
        primaryStage.setMinWidth(1280);  // Minimum width
        primaryStage.setMinHeight(720); // Minimum height

        // Set initial size
        primaryStage.setWidth(1760);  // Initial width
        primaryStage.setHeight(990);  // Initial height

        // Variables to store window size
        final double[] lastWidth = {primaryStage.getWidth()};
        final double[] lastHeight = {primaryStage.getHeight()};

        // Add listener to track changes in full-screen state
        primaryStage.fullScreenProperty().addListener((observable, oldValue, isFullScreen) -> {
            if (!isFullScreen) {
                // Restore previous size when exiting full-screen
                primaryStage.setWidth(lastWidth[0]);
                primaryStage.setHeight(lastHeight[0]);
            } else {
                // Save the size before entering full-screen
                lastWidth[0] = primaryStage.getWidth();
                lastHeight[0] = primaryStage.getHeight();
            }
        });

        // Set full screen initially
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("Press ESC to exit full-screen mode");

        // Add listener to toggle full screen when maximized
        primaryStage.maximizedProperty().addListener((observable, oldValue, isMaximized) -> {
            if (isMaximized) {
                primaryStage.setFullScreen(true);
            }
        });

        // Show the stage
        primaryStage.show();
    }

    /**
     * This method is called to start the connection with the server.
     *
     * @param serverIp
     * @param portStr
     * @throws Exception
     */
    public static void runClient(String serverIp, String portStr) throws Exception
    {
        int port = 0; //Port to listen on
        try
        {
            port = Integer.parseInt(portStr);

        }
        catch (Throwable t)
        {
            System.out.println("ERROR - Could not connect!");
        }

        try
        {
            chat = new ClientController(serverIp, port);

        }
        catch (Exception ex)
        {
            System.out.println("ERROR - Could not connect to server!");
        }

        MainWindowFrameController aFrame = new MainWindowFrameController(); // create StudentFrame

        Stage primaryStage = new Stage();
        aFrame.start(primaryStage);
    }

}
