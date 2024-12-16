package gui;

import javafx.scene.layout.Pane;
import server.ServerUI;
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

public class ServerPortFrameController  {

	
	String temp="";
	
	@FXML
	private Button btnExit = null;
	@FXML
	private Button btnDone = null;
	@FXML
	private Label lbllist;
	
	@FXML
	private TextField portxt;
	ObservableList<String> list;
	
	private String getport() {
		return portxt.getText();			
	}
	
	public void Done(ActionEvent event) throws Exception {
		String p;
		
		p=getport();
		if(p.trim().isEmpty()) {
			System.out.println("You must enter a port number");
					
		}
		else
		{
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			ServerUI.runServer(p);

			//ToDo: open the monitor window
			Pane root = loader.load(getClass().getResource("/gui/ServerMonitor.fxml").openStream());
			ServerMonitorFrameController serverMonitorFrameController = loader.getController();

			Scene scene = new Scene(root);
			//?scene.getStylesheets().add(getClass().getResource("/gui/ServerMonitor.css").toExternalForm());
			primaryStage.setTitle("Server Monitor");

			primaryStage.setScene(scene);
			primaryStage.show();

			serverMonitorFrameController.addRow("localhost", "test", "test");
			serverMonitorFrameController.addRow("localhost", "test1", "test");
			serverMonitorFrameController.addRow("localhost", "test2", "test");
		}
	}

	public void start(Stage primaryStage) throws Exception {	
		Parent root = FXMLLoader.load(getClass().getResource("/gui/ServerPort.fxml"));
				
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/ServerPort.css").toExternalForm());
		primaryStage.setTitle("Client");
		primaryStage.setScene(scene);
		
		primaryStage.show();		
	}
	
	public void getExitBtn(ActionEvent event) throws Exception {
		System.out.println("exit Academic Tool");
		System.exit(0);			
	}

}