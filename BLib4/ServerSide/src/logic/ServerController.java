// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com
package logic;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.protocol.Message;
import gui.ServerMonitorFrameController;
import entities.logic.MessageType;
import entities.user.Subscriber;
import logic.dbController;
import ocsf.server.*;

/**
 * This class overrides some methods in the abstract
 * superclass to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */

public class ServerController extends AbstractServer
{
    private ServerMonitorFrameController monitorController;

    //Class variables *************************************************
    private dbController dbController;
    //Constructors ****************************************************

    /**
     * Constructs an instance of the echo server.
     *
     * @param port              The port number to connect on.
     * @param monitorController the monitor controller
     */
    public ServerController(int port, ServerMonitorFrameController monitorController)
    {
        super(port);
        this.monitorController = monitorController;
    }

    //Instance methods ************************************************

    /**
     * This method handles any messages received from the client.
     *
     * @param msg    The message received from the client.
     * @param client The connection from which the message originated.
     */
    public void handleMessageFromClient(Object msg, ConnectionToClient client)
    {
        MessageType receiveMsg = (MessageType) msg;
        MessageType responseMsg = null;
        switch (receiveMsg.getId())
        {
            case "99":
                // Client wants to disconnect
                try
                {
                    monitorController.clientDisconnected(client);
                    client.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                return;

            case "100":
                // Client wants to log in (subscriber/librarian)
                responseMsg = handleLoginRequest(receiveMsg);
                break;

            case "104":
                // Sign up request to add new subscriber
                responseMsg = new MessageType("204", dbController.handleSubscriberSignUp((List<String>) receiveMsg.data));
                break;

            case "105":
                // Request to a search book by name or category or free text
                responseMsg = handleBookSearchRequest(receiveMsg);
                break;

            case "106":
                // Borrow book request - find copy of the book
                responseMsg = new MessageType("206", dbController.handleCheckBorrowedBookAvailability((String) receiveMsg.data));
                break;

            case "107":
                // Borrow book request - create new borrow in the system
                responseMsg = new MessageType("207", dbController.handleBorrowBook((List<String>) receiveMsg.data));
                break;

            case "108":
                // Client request to order a book
                responseMsg = new MessageType("208", dbController.handleOrderBook((List<String>) receiveMsg.data));
                break;

            case "109":
                // Request to return a borrowed book
                responseMsg = new MessageType("209", dbController.handleReturnBorrowedBook((String) receiveMsg.data));
                break;

            case "110":
                // Request to return all the active subscriber borrowed books
                responseMsg = new MessageType("210", dbController.handleGetSubscriberBorrowList((String) receiveMsg.data));
                break;

            case "111":
                // Request by the subscriber to extend book borrow
                //ToDo: implement
                break;

            case "112":
                // Request to get all the subscriber history
                //? return json, how to do it?
                //ToDo: implement
                break;

            case "113":
                // Request to update subscriber email and phone number
                responseMsg = new MessageType("213", dbController.handleUpdateSubscriberDetails((Subscriber) receiveMsg.data));
                break;

            case "114":
                // Request to update subscriber password
                responseMsg = new MessageType("214", dbController.handleUpdateSubscriberPassword((List<String>) receiveMsg.data));
                break;

            case "115":
                // Request to get all the subscribers in the DB
                responseMsg = new MessageType("215", dbController.handleGetAllSubscribers());
                break;

            case "116":
                // Request by the librarian to get subscriber membership card
                //ToDo: implement
                break;

            case "117":
                // Request by the librarian to manually extend book borrow
                //ToDo: implement
                break;

            case "118":
                // Request by the librarian to get book borrow time report
                //? what data need to return
                //ToDo: implement
                break;

            case "119":
                // Request by the librarian to get subscriber status report
                //? what data need to return
                //ToDo: implement
                break;

            case "120":
                // Request from the client to get 5 newest books in the library
                responseMsg = new MessageType("220", dbController.handleGetFiveNewestBooks());
                break;

            case "121":
                // Request from the client to get 5 most borrowed books in the library
                responseMsg = new MessageType("221", dbController.handleGetFiveMostPopularBooks());
                break;

            default:
                System.out.println("Invalid message type");
                return;
        }

        // sent the message to the client
        sendMessageToClient(client, responseMsg);
    }

    /**
     * The method sends a message to a specific client
     *
     * @param client - the client to send the message
     * @param msg    - the message to send
     */
    private void sendMessageToClient(ConnectionToClient client, Object msg)
    {
        try
        {
            client.sendToClient(msg);
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
            System.out.println("Error sending message to client");
        }
    }

    /**
     * This method overrides the one in the superclass.  Called
     * when the server starts listening for connections.
     */
    protected void serverStarted()
    {
        System.out.println("Server listening for connections on port " + getPort());

        // get connection to the db
        this.dbController = logic.dbController.getInstance();
    }

    /**
     * This method overrides the one in the superclass.  Called
     * when the server stops listening for connections.
     */
    protected void serverStopped()
    {
        System.out.println("Server has stopped listening for connections.");
    }


    /**
     * This method is called when the server is connected to a client.
     *
     * @param client the connection connected to the client.
     */
    @Override
    protected void clientConnected(ConnectionToClient client)
    {
        System.out.println("Client connected");

        monitorController.clientConnected(client);
    }

    /**
     * This method is called when the server is disconnected from a client.
     *
     * @param client the connection with the client.
     */
    @Override
    protected synchronized void clientDisconnected(ConnectionToClient client)
    {
        System.out.println("Client disconnected!");
    }

    /**
     * This method handles the received "100" message and return response message to send to the client
     *
     * @param receiveMsg - received message from the client
     * @return response message to send the client
     */
    private MessageType handleLoginRequest(MessageType receiveMsg)
    {
        MessageType responseMsg = null;
        List<String> data = (List<String>) receiveMsg.data;

        switch (data.get(2))
        {
            case "subscriber":
                responseMsg = new MessageType("201", dbController.handleSubscriberLogin(data.get(0), data.get(1)));
                break;

            case "librarian":
                responseMsg = new MessageType("202", dbController.handleLibrarianLogin(data.get(0), data.get(1)));
                break;

            default:
                System.out.println("Error! invalid login type");
                break;
        }

        // check if the login failed
        if (responseMsg == null || responseMsg.data == null)
        {
            responseMsg = new MessageType("203", null);
        }

        return responseMsg;
    }

    public MessageType handleBookSearchRequest(MessageType receiveMsg)
    {
        MessageType responseMsg = null;
        List<String> data = (List<String>) receiveMsg.data;

        switch (data.get(0))
        {
            case "name":
                responseMsg = new MessageType("205", dbController.handleBookSearchByName(data.get(1)));
                break;

            case "category":
                responseMsg = new MessageType("205", dbController.handleBookSearchByCategory(data.get(1)));
                break;

            case "freeText":
                responseMsg = new MessageType("205", dbController.handleBookSearchByFreeText(data.get(1)));
                break;

            default:
                System.out.println("Error! invalid search type"); // ToDo: check if we need to send MessageType with null
                break;
        }

        return responseMsg;
    }
}

//End of EchoServer class
