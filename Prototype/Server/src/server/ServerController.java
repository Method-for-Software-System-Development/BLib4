// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com
package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


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

    public ServerController(int port)
    {
        super(port);
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

        //ToDo: handle the message from the client

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
