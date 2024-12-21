package client;

import gui.MainWindowFrameController;
import gui.ClientPortFrameController;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientUI extends Application {
	public static ClientController chat; //only one instance

	public static void main( String args[] ) throws Exception
	   { 
		    launch(args);  
	   } // end main
	 
	@Override
	public void start(Stage primaryStage) throws Exception {
		ClientPortFrameController aFrame = new ClientPortFrameController(); // create StudentFrame
		 
		aFrame.start(primaryStage);
	}

	public static void runClient(String ip, String p) throws Exception {
		int port = 0; //Port to listen on
		try
		{
			port = Integer.parseInt(p);

		}
		catch(Throwable t)
		{
			System.out.println("ERROR - Could not connect!");
		}

		try {
			chat= new ClientController(ip, port);

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
