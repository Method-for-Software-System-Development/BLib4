// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com
package logic;

import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gui.ServerMonitorFrameController;
import entities.book.Book;
import entities.logic.MessageType;
import entities.user.Subscriber;
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
    private DbController dbController;
    private DocumentationController documantaionController;
    private Map<String, ConnectionToClient> activeSubscribers;
    private Map<String, ConnectionToClient> activeLibrarians;
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

        documantaionController = DocumentationController.getInstance();
        activeSubscribers = new HashMap<>();
        activeLibrarians = new HashMap<>();
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
                responseMsg = handleLoginRequest(receiveMsg, client);
                break;

            case "101":
                //subscriber wants to log in by card
                responseMsg = new MessageType("201", dbController.handleLogInSubscriberByCard((String) receiveMsg.data));
                break;

            case "1002":
                // Client wants to log out
                handleLogoutRequest((List<String>) receiveMsg.data, client);
                responseMsg = new MessageType("2002", null);
                break;

            case "104":
                // Sign up request to add new subscriber
                responseMsg = new MessageType("204", dbController.handleSubscriberSignUp((List<String>) receiveMsg.data));
                // If a query to sign a new subscriber returns true - document on reader card
                if ((boolean) responseMsg.getData())
                {
                    // Getting the history file of the subscriber by his id
                    List<String[]> historyList = dbController.getHistoryFileBySubscriberId(((List<String>) receiveMsg.getData()).get(0));
                    // Document sign up on subscriber card
                    List<String[]> newHistoryList = documantaionController.documentOnReaderCard("104", historyList, null, null);
                    // Updating a subscriber history file in DB
                    dbController.handleUpdateHistoryFileBySubscriberId(((List<String>) receiveMsg.getData()).get(0), newHistoryList);
                }
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
                // If query to sign a new subscriber returns true - document on reader card
                if ((boolean) responseMsg.getData())
                {
                    // Getting book by copyID for DB
                    Book book = dbController.getBookByCopyId(((List<String>) receiveMsg.getData()).get(1));
                    // Getting the history file of the subscriber by his id
                    List<String[]> historyList = dbController.getHistoryFileBySubscriberId(((List<String>) receiveMsg.getData()).get(0));
                    // Get the current date
                    Date today = new Date(System.currentTimeMillis());
                    // Use Calendar to add 14 days
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(today);
                    // Add 14 days
                    calendar.add(Calendar.DATE, 14);
                    // Get the new date
                    Date futureDate = (Date) calendar.getTime();
                    // Convert to String
                    String futureDateString = futureDate.toString();
                    // Document book borrow on subscriber card
                    List<String[]> newHistoryList = documantaionController.documentOnReaderCard("107", historyList, book.getTitle(), futureDateString);
                    // Updating subscriber history file in DB
                    dbController.handleUpdateHistoryFileBySubscriberId(((List<String>) receiveMsg.getData()).get(0), newHistoryList);
                }
                break;

            case "108":
                // Client request to order a book
                responseMsg = new MessageType("208", dbController.handleOrderBook((List<String>) receiveMsg.data));
                if (((List<Boolean>) responseMsg.getData()).get(0))
                {
                    // Getting book by bookID for DB
                    Book book = dbController.getBookByBookId(((List<String>) receiveMsg.getData()).get(1));
                    // Getting the history file of the subscriber by his id
                    List<String[]> historyList = dbController.getHistoryFileBySubscriberId(((List<String>) receiveMsg.getData()).get(0));
                    // Document book order on subscriber card
                    List<String[]> newHistoryList = documantaionController.documentOnReaderCard("108", historyList, book.getTitle(), "");
                    // Updating subscriber history file in DB
                    dbController.handleUpdateHistoryFileBySubscriberId(((List<String>) receiveMsg.getData()).get(0), newHistoryList);
                }
                break;

            case "109":
                // Request to return a borrowed book
                responseMsg = new MessageType("209", dbController.handleReturnBorrowedBook((String) receiveMsg.data));
                if (((List<Boolean>) responseMsg).get(0))
                {
                    // Getting book name by borrow ID
                    String bookName = (dbController.getBookDetailsByBorrowId((String) receiveMsg.data)).getTitle();
                    // Getting subscriber ID from DB using borrow ID
                    String subscriberID = dbController.getSubscriberIdFromBorrowId((String) receiveMsg.data);
                    // Getting history file of subscriber
                    List<String[]> historyList = dbController.getHistoryFileBySubscriberId(subscriberID);
                    // Document return of a book
                    List<String[]> newHistoryList = documantaionController.documentOnReaderCard("109", historyList, bookName, null);
                    // Updating subscriber history file in DB
                    dbController.handleUpdateHistoryFileBySubscriberId(((List<String>) receiveMsg.getData()).get(0), newHistoryList);
                }
                break;

            case "110":
                // Request to return all the active subscriber borrowed books
                responseMsg = new MessageType("210", dbController.handleGetSubscriberBorrowList((String) receiveMsg.data));
                break;

            case "111":
                // Request by the subscriber to extend book borrow
                responseMsg = new MessageType("211", dbController.handleSubscriberExtendBorrow((List<String>) receiveMsg.data));
                // Getting book name by borrow ID
                String bookName = (dbController.getBookDetailsByBorrowId((String) receiveMsg.data)).getTitle();
                if ((boolean) responseMsg.getData())
                {
                    // Getting subscriber ID by borrow ID
                    String subscriberID = dbController.getSubscriberIdFromBorrowId((String) receiveMsg.data);
                    // Getting history file of subscriber
                    List<String[]> historyList = dbController.getHistoryFileBySubscriberId(subscriberID);
                    // Document extend granted
                    List<String[]> newHistoryList = documantaionController.documentOnReaderCard("111-1", historyList, bookName, ((List<String>) receiveMsg.getData()).get(1));
                    // Updating subscriber history file in DB
                    dbController.handleUpdateHistoryFileBySubscriberId(((List<String>) receiveMsg.getData()).get(0), newHistoryList);
                }
                else
                {
                    // Getting subscriber ID by borrow ID
                    String subscriberID = dbController.getSubscriberIdFromBorrowId((String) receiveMsg.data);
                    // Getting history file of subscriber
                    List<String[]> historyList = dbController.getHistoryFileBySubscriberId(subscriberID);
                    // TODO change first null to book name
                    List<String[]> newHistoryList = documantaionController.documentOnReaderCard("111-2", historyList, bookName, null);
                    // Updating subscriber history file in DB
                    dbController.handleUpdateHistoryFileBySubscriberId(((List<String>) receiveMsg.getData()).get(0), newHistoryList);
                }
                break;

            case "112":
                // Request to get all the subscriber history
                responseMsg = new MessageType("212", dbController.handleReturnSubscriberHistory((String) receiveMsg.data));
                break;

            case "113":
                // Request to update subscriber email and phone number
                responseMsg = new MessageType("213", dbController.handleUpdateSubscriberDetails((Subscriber) receiveMsg.data));
                if ((boolean) responseMsg.getData())
                {
                    Subscriber subscriber = dbController.getSubscriberBySubscriberId(((Subscriber) receiveMsg.data).getId());
                    // Getting history file of subscriber
                    List<String[]> historyList = dbController.getHistoryFileBySubscriberId(subscriber.getId());
                    // Setting the new list to the old list in-case there will be no changes
                    List<String[]> newHistoryList = historyList;
                    // Case for phone number and mail change
                    if (subscriber.getEmail() != ((Subscriber) receiveMsg.data).getEmail() && subscriber.getPhone() != ((Subscriber) receiveMsg.data).getPhone())
                    {
                        // Document phone number and mail change
                        newHistoryList = documantaionController.documentOnReaderCard("113-3", historyList, ((Subscriber) receiveMsg.data).getPhone(), ((Subscriber) receiveMsg.data).getEmail());
                    }
                    // Case for only mail change
                    else if (subscriber.getEmail() != ((Subscriber) receiveMsg.data).getEmail())
                    {
                        // Document mail change
                        newHistoryList = documantaionController.documentOnReaderCard("113-2", historyList, ((Subscriber) receiveMsg.data).getEmail(), null);
                    }
                    // Case for only phone number change
                    else if (subscriber.getPhone() != ((Subscriber) receiveMsg.data).getPhone())
                    {
                        // Document phone number change
                        newHistoryList = documantaionController.documentOnReaderCard("113-1", historyList, ((Subscriber) receiveMsg.data).getPhone(), null);
                    }
                    // Updating subscriber history file in DB
                    dbController.handleUpdateHistoryFileBySubscriberId(subscriber.getId(), newHistoryList);
                }
                break;

            case "114":
                // Request to update subscriber password
                responseMsg = new MessageType("214", dbController.handleUpdateSubscriberPassword((List<String>) receiveMsg.data));
                if ((boolean) responseMsg.getData())
                {
                    // Getting the history file of the subscriber by his id
                    List<String[]> historyList = dbController.getHistoryFileBySubscriberId(((List<String>) receiveMsg.getData()).get(0));
                    // Document updated password
                    List<String[]> newHistoryList = documantaionController.documentOnReaderCard("114", historyList, null, null);
                    // Updating subscriber history file in DB
                    dbController.handleUpdateHistoryFileBySubscriberId(((List<String>) receiveMsg.getData()).get(0), newHistoryList);
                }
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
                responseMsg = new MessageType("217", dbController.handleLibrarianExtendBorrow((List<String>) receiveMsg.data));

                if ((boolean) responseMsg.getData())
                {
                    // Getting subscriber ID using borrow ID
                    String subscriberID = dbController.getSubscriberIdFromBorrowId(((List<String>) receiveMsg.getData()).get(0));
                    // Getting book name by borrow ID
                    String bookname = (dbController.getBookDetailsByBorrowId((String) receiveMsg.data)).getTitle();
                    // Getting history file of subscriber
                    List<String[]> historyList = dbController.getHistoryFileBySubscriberId(subscriberID);
                    // Document manual extension
                    List<String[]> newHistoryList = documantaionController.documentOnReaderCard("117", historyList, bookname, null);
                    // Updating subscriber history file in DB
                    dbController.handleUpdateHistoryFileBySubscriberId(subscriberID, newHistoryList);
                }
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

            case "122":
                // Request from the client to get all the missed sms of the subscriber
                responseMsg = new MessageType("222", dbController.handleGetSubscriberMissedSms((String) receiveMsg.data));
                break;

            case "123":
                // Request to get book location in the library
                responseMsg = new MessageType("223", dbController.handleGetBookLocation((String) receiveMsg.data));
                break;

            case "124":
                // check if the book is available for order
                responseMsg = new MessageType("224", dbController.handleCheckIfBookIsAvailableForOrder((String) receiveMsg.data));
                break;

            case "125":
                //Request to get the if of a report
                responseMsg = new MessageType("225", dbController.fetchReportId((List<String>) receiveMsg.data));
                break;

            case "126":
                //Request to get blob data of a report
                byte[] blobData = dbController.fetchReportBlob((List<String>) receiveMsg.data);
                responseMsg = new MessageType("226", BlobUtil.convertBlobToList(blobData));
                break;

            case "127":
                //request to create new empty report for next month
                dbController.insertEmptyMonthlyReport((List<String>) receiveMsg.data);
                break;

            case "128":
                // Response to get all librarian unread messages
                responseMsg = new MessageType("228", dbController.handleGetUnreadLibrarianMessages());
                break;

            case "129":
                // Response to update mark librarian message as read
                responseMsg = new MessageType("229", dbController.handleMarkLibrarianNotificationAsRead((String) receiveMsg.data));
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
        this.dbController = DbController.getInstance();
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
     * @param client     - the client connection
     * @return response message to send the client
     */
    private MessageType handleLoginRequest(MessageType receiveMsg, ConnectionToClient client)
    {
        MessageType responseMsg = null;
        List<String> data = (List<String>) receiveMsg.data;

        switch (data.get(2))
        {
            case "subscriber":
                responseMsg = new MessageType("201", dbController.handleSubscriberLogin(data.get(0), data.get(1)));

                if (responseMsg.data != null)
                {
                    activeSubscribers.put(data.get(0), client);
                }

                break;

            case "librarian":
                responseMsg = new MessageType("202", dbController.handleLibrarianLogin(data.get(0), data.get(1)));

                if (responseMsg.data != null)
                {
                    activeLibrarians.put(data.get(0), client);
                }

                //Send notification to the librarian
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

    /**
     * This method handles the received "101" message and updates the active subscribers/librarians list
     *
     * @param receiveMsg - received message from the client
     * @param client     - the client connection
     */
    private void handleLogoutRequest(List<String> receiveMsg, ConnectionToClient client)
    {
        switch (receiveMsg.get(0))
        {
            case "librarian":
                activeLibrarians.remove(receiveMsg.get(1));
                break;
            case "subscriber":
                activeSubscribers.remove(receiveMsg.get(1));
                break;
            default:
                System.out.println("Error! invalid logout type");
                break;
        }

    }

    /**
     * This method handles the received "105" message and return response message to send to the client
     *
     * @param receiveMsg - received message from the client
     * @return - response message to send the client
     */
    private MessageType handleBookSearchRequest(MessageType receiveMsg)
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
                System.out.println("Error! invalid search type");
                responseMsg = new MessageType("205", null);
                break;
        }

        return responseMsg;
    }

}
