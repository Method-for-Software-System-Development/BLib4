// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com
package server;

import java.io.IOException;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

import java.io.*;

import gui.ServerMonitorFrameController;
import logic.MessageType;
import logic.Subscriber;
import logic.dbController;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract
 * superclass in order to give more functionality to the server.
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
     * @param port The port number to connect on.
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
        MessageType sendMsg = null;
        switch (receiveMsg.getId())
        {
            case "100":
                // request to send all the subscribers in db, no data in the message
                sendMsg = new MessageType("200", dbController.getAllSubscribers());
                break;

            case "101":
                // request to send subscriber with specific id, in format String (the id)
                sendMsg = new MessageType("201", dbController.getSubscriberById((String) receiveMsg.getData()));
                break;

            case "102":
                // request to update subscriber in db, in format Subscriber
                sendMsg = new MessageType("202", dbController.updateSubscriber((Subscriber) receiveMsg.getData()));
                break;

            case "DISCONNECT":
                try {
                    monitorController.clientDisconnected(client);
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;

            default:
                System.out.println("Invalid message type");
                return;
        }

        // sent the message to the client
        sendMessageToClient(client, sendMsg);
    }

    /**
     * The method send message to specific client
     *
     * @param client - the client to send the message
     * @param msg    - the message to send
     */
    private void sendMessageToClient(ConnectionToClient client, Object msg)
    {
        try
        {
        	System.out.println("?????????");
            client.sendToClient(msg);
        }
        catch (Exception e)
        {
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

    // ToDo: not working, check why
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
}

//End of EchoServer class
