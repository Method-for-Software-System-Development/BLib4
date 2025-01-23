// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package logic.communication;

import entities.book.Book;
import javafx.scene.control.Alert;
import logic.communication.ChatIF;
import gui.common.*;
import entities.logic.*;
import entities.user.*;
import ocsf.client.AbstractClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
public class ChatClient extends AbstractClient
{
    //Instance variables **********************************************

    /**
     * The interface type variable.  It allows the implementation of
     * the display method in the client.
     */
    ChatIF clientUI;
    private ShowAllSubscribersFrameController showAllSubscribersController;
    private EditSubscriberFrameController editSubscriberFrameController;
    public static List<Subscriber> subscribers = new ArrayList<Subscriber>();
    public static List<Book> books = new ArrayList<>();
    public static List<String> availability = new ArrayList<>();
    public static boolean serverResponse = false;
    public static boolean awaitResponse = false;
    public static Date todayDate;
    public static Librarian librarian;
    public static ArrayList<Boolean> returnOutcome;
    public static ArrayList<ArrayList<String>> listOfBorrows;
    public static ArrayList<ArrayList<String>> listOfActivities;
    public static ArrayList<ArrayList<String>> listOfMessages;
    public static ArrayList<ArrayList<String>> listOfSubscribersForLibrarian;
    public static int reportID;
    public static List<String[]> blobData;
    public static List<String> smsData;
    public static int borrowHandle;

    //Constructors ****************************************************

    /**
     * Constructs an instance of the chat client.
     *
     * @param host     The server to connect to.
     * @param port     The port number to connect on.
     * @param clientUI The interface type variable.
     */

    public ChatClient(String host, int port, ChatIF clientUI)
            throws IOException
    {
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
    public void handleMessageFromServer(Object msg)
    {
        MessageType receiveMsg = (MessageType) msg;
        awaitResponse = false;
        Subscriber subscriber;
        switch (receiveMsg.getId())
        {
            case "201":
            	// log in succeed, save subscriber details
            	serverResponse=true;
                subscriber = (Subscriber) receiveMsg.getData();
                subscribers.clear();
                if (subscriber != null)
                {
                    subscribers.add(subscriber);
                }
                break;

            case "202":
            	// log in succeed, save librarian details
            	serverResponse=true;
                librarian= (Librarian) receiveMsg.getData();
                break;

            case "2002":
                // response for log out request
                break;

            case "203":
            	// log in failed 
            	serverResponse=false;
            	break;

            case "204":
            	// status of sign up in the db, in format boolean
                serverResponse = (boolean) receiveMsg.getData();
                break;

            case "205":
                books = (List<Book>)receiveMsg.data;
                break;

            case "206":
            	availability = (List<String>) receiveMsg.getData();
                break;

            case "207":
                // State of borrow 
            	borrowHandle = (int) receiveMsg.getData();
                break;

            case "208":
            	availability = (List<String>) receiveMsg.getData();
            	break;

            case "209":
            	returnOutcome = (ArrayList<Boolean>) receiveMsg.getData();
                break;

            case "210":
            	//list of borrows of subscriber
            	listOfBorrows=(ArrayList<ArrayList<String>>) receiveMsg.getData(); 
            	break;

            case "211":
                //response from server for borrow extension
                serverResponse = (boolean) receiveMsg.getData();
                break;

            case "212":
                // Convert List<String[]> to ArrayList<ArrayList<String>> with reordered indices
                List<String[]> rawData = (List<String[]>) receiveMsg.getData();
                System.out.println("Raw data: " + rawData);
                listOfActivities = new ArrayList<>();

                for (String[] array : rawData) {
                    // Create a new ArrayList and reorder elements
                    System.out.println("Array: " + array);
                    ArrayList<String> activity = new ArrayList<>();
                    activity.add(array[2]); // Move index 2 to position 0
                    activity.add(array[1]); // Move index 1 to position 1
                    activity.add(array[0]); // Move index 0 to position 2
                    // Add the reordered activity to the list
                    listOfActivities.add(activity);
                }
                System.out.println("List of activities: " + listOfActivities);
                break;

            case "213":
            	// status of update in the db, in format boolean
                serverResponse = (boolean) receiveMsg.getData();
                break;

            case "214":
                // status of update in the db, in format boolean
                serverResponse = (boolean) receiveMsg.getData();
                break;

            case "215":
                subscribers = (List<Subscriber>) receiveMsg.getData();
                listOfSubscribersForLibrarian = new ArrayList<>();
                for (Subscriber sub : subscribers)
                {
                    ArrayList<String> subscriberData = new ArrayList<>();
                    subscriberData.add(sub.getId());
                    subscriberData.add(sub.getFirstName());
                    subscriberData.add(sub.getLastName());
                    if (sub.getStatus())
                    {
                        subscriberData.add("Active");
                    }
                    else
                    {
                        subscriberData.add("Frozen");
                    }
                    listOfSubscribersForLibrarian.add(subscriberData);
                }
                break;

            case "216":
                //ToDo: implement
                break;

            case "217":
                // status of update in the db, in format boolean
                serverResponse = (boolean) receiveMsg.getData();
                break;

            case "218":
                //ToDo: implement
                break;

            case "219":
                //ToDo: implement
                break;
            case "220":
            case "221":
                books = (List<Book>)receiveMsg.data;
                break;
            case "222":
                smsData = (List<String>)receiveMsg.data;
                for (int i = 0; i < smsData.size(); i++)
                {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("SMS Simulation");
                    alert.setHeaderText(null);
                    alert.setContentText(smsData.get(i));
                    alert.initOwner(SceneManager.getStage());
                    alert.showAndWait();
                }
                break;
            case "223":
                availability = (List<String>)receiveMsg.data;
                break;
            case "224":
                serverResponse = (boolean) receiveMsg.getData();
                break;
            case "225":
            	//response for report id request
            	reportID=(int) receiveMsg.data;
            case "226":
            	//response for blob data of report request
            	blobData=(List<String[]>) receiveMsg.data;
            case "228":
                // list of unread librarian messages
                listOfMessages=(ArrayList<ArrayList<String>>) receiveMsg.getData();
                break;
            case "229":
                //response from server for message confirmation
                serverResponse = (boolean) receiveMsg.getData();
                break;
            	
            //!/ remove this /////////////////////////////////
            case "0200":
                // list of all the subscribers in db, in format List<Subscriber>
                subscribers.clear();
                subscribers = (List<Subscriber>) receiveMsg.getData();
                break;

//            case "0201":
//                // subscriber details, in format Subscriber
//                Subscriber subscriber = (Subscriber) receiveMsg.getData();
//                subscribers.clear();
//                if (subscriber != null)
//                {
//                    subscribers.add(subscriber);
//                }
//                break;

            case "0202":
                // status of update in the db, in format boolean
                serverResponse = (boolean) receiveMsg.getData();
                break;
            //!/ end of remove this //////////////////////////////////

            default:
                System.out.println("Invalid message received from server");
                break;
        }

    }

    /**
     * This method handles all data coming from the UI
     *
     * @param message The message from the UI.
     */
    public void handleMessageFromClientUI(Object message)
    {
        try
        {
            openConnection();//in order to send more than one message
            awaitResponse = true;
            sendToServer(message);
            // wait for response
            while (awaitResponse)
            {
                try
                {
                    Thread.sleep(100);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            clientUI.display("Could not send message to server: Terminating client." + e);
            quit();
        }
    }

    /**
     * This method terminates the client.
     */
    public void quit()
    {
        try
        {
            sendDisconnectRequest();
            closeConnection();
        }
        catch (IOException e)
        {
        }
        System.exit(0);
    }

    public void sendDisconnectRequest()
    {
        try
        {
            openConnection();
            MessageType disconnectMessage = new MessageType("99", null);
            sendToServer(disconnectMessage);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            clientUI.display("Could not send disconnect request to server: " + e);
        }
    }

    public EditSubscriberFrameController getEditSubscriberFrameController()
    {
        return editSubscriberFrameController;
    }

    public void setEditSubscriberFrameController(EditSubscriberFrameController editSubscriberFrameController)
    {
        this.editSubscriberFrameController = editSubscriberFrameController;
    }

    public ShowAllSubscribersFrameController getShowAllSubscribersController()
    {
        return showAllSubscribersController;
    }

    public void setShowAllSubscribersController(ShowAllSubscribersFrameController showAllSubscribersController)
    {
        this.showAllSubscribersController = showAllSubscribersController;
    }
}
//End of ChatClient class
