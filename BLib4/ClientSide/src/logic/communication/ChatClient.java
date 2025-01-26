package logic.communication;

import entities.book.Book;
import gui.librarian.librarianUI.LibrarianUI_Controller;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import entities.logic.*;
import entities.user.*;
import ocsf.client.AbstractClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatClient extends AbstractClient
{
    //Instance variables **********************************************

    private boolean isLogIn = false;
    private String loginID;
    private String loginType;

    /**
     * The interface type variable.  It allows the implementation of
     * the display method in the client.
     */
    ChatIF clientUI;
    public static List<Subscriber> subscribers = new ArrayList<Subscriber>();
    public static List<Book> books = new ArrayList<>();
    public static List<String> availability = new ArrayList<>();
    public static boolean serverResponse = false;
    public static boolean awaitResponse = false;
    public static Librarian librarian;
    public static ArrayList<Boolean> returnOutcome;
    public static ArrayList<ArrayList<String>> listOfOrders;
    public static ArrayList<ArrayList<String>> listOfBorrows;
    public static ArrayList<ArrayList<String>> listOfActivities;
    public static ArrayList<ArrayList<String>> listOfMessages;
    public static ArrayList<ArrayList<String>> listOfSubscribersForLibrarian;
    public static int reportID;
    public static List<String[]> blobData;
    public static List<String> smsData;
    public static int subscriberStatus;
    public static String messageData;

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
                this.isLogIn = true;
                this.loginID = ((Subscriber) receiveMsg.getData()).getId();
                this.loginType = "subscriber";


                serverResponse = true;
                subscriber = (Subscriber) receiveMsg.getData();
                subscribers.clear();
                if (subscriber != null)
                {
                    subscribers.add(subscriber);
                }
                break;

            case "202":
                // log in succeed, save librarian details
                this.isLogIn = true;
                this.loginID = ((Librarian) receiveMsg.getData()).getId();
                this.loginType = "librarian";

                serverResponse = true;
                librarian = (Librarian) receiveMsg.getData();
                break;

            case "2002":
                // response for log out request
                this.isLogIn = false;
                this.loginID = null;
                this.loginType = null;
                break;

            case "203":
                // log in failed
                serverResponse = false;
                break;

            case "204":
                // status of sign up in the db, in format boolean
                serverResponse = (boolean) receiveMsg.getData();
                break;

            case "205":
                // list of books that matches the search of a book
                books = (List<Book>) receiveMsg.data;
                break;

            case "206":
                //copy of a book availability and orders
                availability = (List<String>) receiveMsg.getData();
                break;

            case "207":
                //response for a new borrow
                serverResponse = (boolean) receiveMsg.getData();
                break;

            case "208":
                //response for book order
            case "209":
                //response for book return
                returnOutcome = (ArrayList<Boolean>) receiveMsg.getData();
                break;

            case "210":
                //list of borrows of subscriber
                listOfBorrows = (ArrayList<ArrayList<String>>) receiveMsg.getData();
                break;

            case "211":
                //response from server for borrow extension
                serverResponse = (boolean) receiveMsg.getData();
                break;

            case "2111":
                //notification for librarian about borrow extension
                Platform.runLater(() ->
                {
                    messageData = (String) receiveMsg.data;
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("New Message From Subscriber");
                    alert.setHeaderText("");
                    alert.setContentText(messageData);
                    alert.initOwner(SceneManager.getStage());
                    alert.showAndWait();
                });

                Object controller = SceneManager.getCurrentController();

                if (controller instanceof LibrarianUI_Controller)
                {
                    Platform.runLater(() -> ((LibrarianUI_Controller) controller).refreshMessagesTable());
                }
                break;

            case "212":
                // Convert List<String[]> to ArrayList<ArrayList<String>> with reordered indices
                List<String[]> rawData = (List<String[]>) receiveMsg.getData();
                listOfActivities = new ArrayList<>();

                for (String[] array : rawData)
                {
                    // Create a new ArrayList and reorder elements
                    ArrayList<String> activity = new ArrayList<>();
                    activity.add(array[2]); // Move index 2 to position 0
                    activity.add(array[1]); // Move index 1 to position 1
                    activity.add(array[0]); // Move index 0 to position 2
                    // Add the reordered activity to the list
                    listOfActivities.add(activity);
                }
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
                // all subscriber lists
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
                // status of a subscriber
                subscriberStatus = (int) receiveMsg.getData();
                break;

            case "217":
                // status of update in the db, in format boolean
                serverResponse = (boolean) receiveMsg.getData();
                break;

            case "218":
                // status of update in the db, in format boolean
                serverResponse = (boolean) receiveMsg.getData();
                break;

            case "219":
                // list of active orders of a book
                listOfOrders = (ArrayList<ArrayList<String>>) receiveMsg.data;
                break;

            case "220":
                // list of 5 newest books in a library
            case "221":
                // list of 5 most borrowed books in a library
                books = (List<Book>) receiveMsg.data;
                break;

            case "222":
                // SMS message to a subscriber
                smsData = (List<String>) receiveMsg.data;
                StringBuilder smsText = new StringBuilder();
                for (int i = 0; i < smsData.size(); i++)
                {
                    if (i == 0)
                    {
                        smsText.append(smsData.get(i));
                    }
                    else
                    {
                        smsText.append("\n\n").append(smsData.get(i));
                    }
                }

                if (smsData.size() != 0)
                {
                    Platform.runLater(() ->
                    {
                        Alert SMSalert = new Alert(Alert.AlertType.INFORMATION);
                        SMSalert.setTitle("SMS Simulation");
                        SMSalert.setHeaderText(null);
                        SMSalert.setContentText(smsText.toString());
                        SMSalert.initOwner(SceneManager.getStage());
                        SMSalert.showAndWait();
                    });
                }
                break;

            case "223":
                // book location or availability
                availability = (List<String>) receiveMsg.data;
                break;

            case "224":
                // response about book availability for order
                serverResponse = (boolean) receiveMsg.getData();
                break;

            case "225":
                //response for report id request
                reportID = (int) receiveMsg.data;
                break;

            case "226":
                //response for blob data of report request
                blobData = (List<String[]>) receiveMsg.data;
                break;

            case "227":
                //response for report status
                serverResponse = (boolean) receiveMsg.getData();
                break;

            case "228":
                // list of unread librarian messages
                listOfMessages = (ArrayList<ArrayList<String>>) receiveMsg.getData();
                break;

            case "229":
                //response from server for message confirmation
                serverResponse = (boolean) receiveMsg.getData();
                break;

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
            openConnection();//to send more than one message
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
     * It sends a log-out request to the server if the client is logged in.
     * Then it sends a disconnect request to the server and closes the connection.
     */
    public void quit()
    {
        try
        {
            // check if the client is logged in
            if (isLogIn)
            {
                // send log-out request
                sendLogOutRequest();
            }

            sendDisconnectRequest();
            closeConnection();
        }
        catch (IOException e)
        {
        }
        System.exit(0);
    }

    /**
     * This method sends a disconnect request to the server.
     */
    private void sendDisconnectRequest()
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

    /**
     * This method sends a log-out request to the server.
     */
    private void sendLogOutRequest()
    {
        try
        {
            openConnection();
            List<String> data = new ArrayList<>();
            data.add(loginType);
            data.add(loginID);

            MessageType logOutMessage = new MessageType("1002", data);
            ClientUI.chat.accept(logOutMessage);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            clientUI.display("Could not send log out request to server: " + e);
        }
    }
}
//End of ChatClient class