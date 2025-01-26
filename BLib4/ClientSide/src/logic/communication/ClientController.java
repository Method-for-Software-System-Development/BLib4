package logic.communication;


import entities.logic.MessageType;

import java.io.IOException;

public class ClientController implements ChatIF
{
    //Class variables *************************************************

    /**
     * The default port to connect on.
     */
    public static int DEFAULT_PORT;

    //Instance variables **********************************************

    public ChatClient getClient()
    {
        return client;
    }

    /**
     * The instance of the client that created this ConsoleChat.
     */
    ChatClient client;

    //Constructors ****************************************************

    /**
     * Constructs an instance of the ClientConsole UI.
     *
     * @param host The host to connect to.
     * @param port The port to connect on.
     */
    public ClientController(String host, int port)
    {
        try
        {
            client = new ChatClient(host, port, this);
        }
        catch (IOException exception)
        {
            System.out.println("Error: Can't setup connection!" + " Terminating client.");
            System.exit(1);
        }
    }


    //Instance methods ************************************************

    /**
     * This method waits for input from the console.  Once it is
     * received, it sends it to the client's message handler.
     * @param str - the message to be sent
     */
    public void accept(MessageType str)
    {
        client.handleMessageFromClientUI(str);
    }

    /**
     * This method overrides the method in the ChatIF interface.  It
     * displays a message onto the screen.
     *
     * @param message The string to be displayed.
     */
    public void display(String message)
    {
        System.out.println("> " + message);
    }
}
//End of ConsoleChat class
