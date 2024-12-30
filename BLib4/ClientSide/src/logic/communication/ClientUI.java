package logic.communication;

import gui.common.*;
import javafx.application.Application;
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

    /**
     * This method is called when the program is run as an application.
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        ClientPortFrameController aFrame = new ClientPortFrameController(); // create StudentFrame

        aFrame.start(primaryStage);
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
