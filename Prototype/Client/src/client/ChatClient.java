// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import client.ChatIF;
import gui.EditSubscriberFrameController;
import gui.ShowAllSubscribersFrameController;
import logic.MessageType;
import logic.Subscriber;
import ocsf.client.AbstractClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient {
    //Instance variables **********************************************

    /**
     * The interface type variable.  It allows the implementation of
     * the display method in the client.
     */
    ChatIF clientUI;
    private ShowAllSubscribersFrameController showAllSubscribersController;
    private EditSubscriberFrameController editSubscriberFrameController;
    public static List<Subscriber> subscribers = new ArrayList<Subscriber>();
    public static boolean serverResponse = false;
    public static boolean awaitResponse = false;

    //Constructors ****************************************************

    /**
     * Constructs an instance of the chat client.
     *
     * @param host     The server to connect to.
     * @param port     The port number to connect on.
     * @param clientUI The interface type variable.
     */

    public ChatClient(String host, int port, ChatIF clientUI)
            throws IOException {
        super(host, port); //Call the superclass constructor
        this.clientUI = clientUI;
        openConnection();
    }

    //Instance methods ************************************************

    /**
     * This method handles all data that comes in from the server.
     *
     * @param msg The message from the server.
     */
    public void handleMessageFromServer(Object msg) {
        MessageType receiveMsg = (MessageType) msg;
        awaitResponse=false;
        switch (receiveMsg.getId()) {
            case "200":
                // list of all the subscribers in db, in format List<Subscriber>
                subscribers = (List<Subscriber>) receiveMsg.getData();
                break;

            case "201":
                // subscriber details, in format Subscriber
                Subscriber subscriber=(Subscriber)receiveMsg.getData();
                subscribers.clear();
                if(subscriber!=null)
                	subscribers.add(subscriber);
                break;

            case "202":
                // status of update in the db, in format boolean
                serverResponse = (boolean) receiveMsg.getData();
                break;

            default:
                System.out.println("Invalid message received from server");
                break;
        }

    }
    


    //ToDo: under that command I didnt touch (need to check if we need to change something)
    /**
     * This method handles all data coming from the UI
     *
     * @param message The message from the UI.
     */
    public void handleMessageFromClientUI(Object message) {
        try {
            openConnection();//in order to send more than one message
            awaitResponse = true;
            sendToServer(message);
            // wait for response
            while (awaitResponse) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            clientUI.display("Could not send message to server: Terminating client." + e);
            quit();
        }
    }

    public void handleUpdateMessageFromClientUI(Object message) {
        try {
            openConnection();//in order to send more than one message
            sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
            clientUI.display("Could not send message to server: Terminating client." + e);
            quit();
        }
    }

    /**
     * This method terminates the client.
     */
    public void quit() {
        try {
            sendDisconnectRequest();
            closeConnection();
        } catch (IOException e) {
        }
        System.exit(0);
    }

    public void sendDisconnectRequest() {
        try {
            openConnection();
            MessageType disconnectMessage = new MessageType("DISCONNECT", null);
            sendToServer(disconnectMessage);
        } catch (IOException e) {
            e.printStackTrace();
            clientUI.display("Could not send disconnect request to server: " + e);
        }
    }

	public EditSubscriberFrameController getEditSubscriberFrameController() {
		return editSubscriberFrameController;
	}

	public void setEditSubscriberFrameController(EditSubscriberFrameController editSubscriberFrameController) {
		this.editSubscriberFrameController = editSubscriberFrameController;
	}

	public ShowAllSubscribersFrameController getShowAllSubscribersController() {
		return showAllSubscribersController;
	}

	public void setShowAllSubscribersController(ShowAllSubscribersFrameController showAllSubscribersController) {
		this.showAllSubscribersController = showAllSubscribersController;
	}
}
//End of ChatClient class
