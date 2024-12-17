// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com
package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


import gui.ServerMonitorFrameController;
import logic.MessageType;
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

    /**
     * The default port to listen on.
     */
    //final public static int DEFAULT_PORT = 5555;

    //Constructors ****************************************************

    /**
     * Constructs an instance of the echo server.
     *
     * @param port The port number to connect on.
     *
     */
    private Connection conn;

    public ServerController(int port, ServerMonitorFrameController monitorController)
    {
        super(port);
        this.monitorController = monitorController;
    }

    //Instance methods ************************************************

    /**
     * This method handles any messages received from the client.
     *
     * @param msg The message received from the client.
     * @param client The connection from which the message originated.
     * @param
     */
    public void handleMessageFromClient  (Object msg, ConnectionToClient client)
    {

        MessageType reciveMsg = (MessageType) msg;

        switch (reciveMsg.getId())
        {
            case "100":
                // request to send all the subscribers in db, no data in the message
                break;

            case "101":
                // request to send subscriber with specific id, in format String (the id)
                break;

            case "102":
                // request to update subscriber in db, in format Subscriber
                break;

            default:
                System.out.println("Invalid message type");
                break;
        }



        //ToDo: handle the message from the client
        // Type of messages: Request all students, update student in DB, get student by ID
        /*
        if (msg instanceof Student)
        {
            // Update the student in the DB
            Student s = (Student) msg;
            // update db
            updateStudentInDB(s);

            this.sendToAllClients("valid");
        }
        else if(msg instanceof String )  {
            // Send the student to the client
            int flag = 0;
            for (int i = 0; i < 4; i++) {
                if (students[i].getId().equals(msg)) {
                    System.out.println("Server Found");
                    this.sendToAllClients(students[i].toString());
                    flag = 1;
                }

            }
            if (flag != 1) {
                System.out.println("Not Found");
                this.sendToAllClients("Error");
            }
        }*/
    }

    /**
     * This method overrides the one in the superclass.  Called
     * when the server starts listening for connections.
     */
    protected void serverStarted()
    {
        System.out.println ("Server listening for connections on port " + getPort());

        connectToDb();

    }
    /**
     * This method overrides the one in the superclass.  Called
     * when the server stops listening for connections.
     */
    protected void serverStopped()  {
        System.out.println ("Server has stopped listening for connections.");
    }


    @Override
    protected void clientConnected(ConnectionToClient client) {
        System.out.println("Client connected");

        monitorController.clientConnected(client);
    }


    @Override
    protected synchronized void clientDisconnected(ConnectionToClient client) {
        System.out.println("Client disconnected");

        monitorController.clientDisconnected(client);
    }

    private void connectToDb()
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            System.out.println("Driver definition succeed");
        } catch (Exception ex) {
            /* handle the error*/
            System.out.println("Driver definition failed");
        }

        try
        {
            this.conn = DriverManager.getConnection("jdbc:mysql://localhost/blib4?serverTimezone=IST","root","Aa123456");
            System.out.println("SQL connection succeed");

            //getFacilityFromDB(conn);
            //getStudentsFromDB(conn);

        } catch (SQLException ex)
        {/* handle any errors*/
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    //ToDo: read data from DB


    //ToDo: update data in DB
}


//End of EchoServer class
