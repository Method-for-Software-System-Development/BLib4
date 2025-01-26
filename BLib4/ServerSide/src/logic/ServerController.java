package logic;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import gui.ServerMonitorFrameController;
import entities.book.Book;
import entities.logic.MessageType;
import entities.user.Subscriber;
import ocsf.server.*;

/**
 * The ServerController class is responsible for handling all the server's logic.
 * It receives messages from the client, processes them, and sends a response back.
 */
public class ServerController extends AbstractServer
{
    private final ServerMonitorFrameController monitorController;

    //Class variables *************************************************
    private static DbController dbController;
    private final DocumentationController documentationController;
    private static Map<String, ConnectionToClient> activeSubscribers;
    private static Map<String, ConnectionToClient> activeLibrarians;
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

        documentationController = DocumentationController.getInstance();
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
        MessageType responseMsg;

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
                    System.out.println("Error disconnecting client");
                }
                return;

            case "100":
                // Client wants to log in (subscriber/librarian)
                responseMsg = handleLoginRequest(receiveMsg, client);
                break;

            case "101":
                //subscriber wants to log in by card
                responseMsg = new MessageType("201", dbController.handleLogInSubscriberByCard((String) receiveMsg.data));

                // add the subscriber to the connected subscribers list
                if (responseMsg.data != null)
                {
                    activeSubscribers.put((String) receiveMsg.data, client);
                }
                else
                {
                    // failed to log in
                    responseMsg = new MessageType("203", null);
                }

                break;

            case "1002":
                // Client wants to log out
                handleLogoutRequest((List<String>) receiveMsg.data);
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
                    List<String[]> newHistoryList = documentationController.documentOnReaderCard("104", historyList, null, null);
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

                // If a query to sign a new subscriber returns true - document on reader card
                if ((boolean) responseMsg.getData())
                {
                    // Getting a book by copyID for DB
                    Book book = dbController.getBookByCopyId(((List<String>) receiveMsg.getData()).get(1));
                    // Getting the history file of the subscriber by his id
                    List<String[]> historyList = dbController.getHistoryFileBySubscriberId(((List<String>) receiveMsg.getData()).get(0));
                    // get the due date
                    String futureDateString = ((List<String>) receiveMsg.getData()).get(2);

                    // Document book borrow on subscriber card
                    List<String[]> newHistoryList = documentationController.documentOnReaderCard("107", historyList, book.getTitle(), futureDateString);

                    // Updating a subscriber history file in DB
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
                    List<String[]> newHistoryList = documentationController.documentOnReaderCard("108", historyList, book.getTitle(), "");
                    // Updating a subscriber history file in DB
                    dbController.handleUpdateHistoryFileBySubscriberId(((List<String>) receiveMsg.getData()).get(0), newHistoryList);
                }
                break;

            case "109":
                // Request to return a borrowed book
                responseMsg = new MessageType("209", dbController.handleReturnBorrowedBook((String) receiveMsg.data));

                if (((List<Boolean>) responseMsg.getData()).get(0))
                {
                    // Getting book name by borrow ID
                    String bookName = (dbController.getBookDetailsByBorrowId((String) receiveMsg.data)).getTitle();

                    // Getting subscriber ID from DB using borrow ID
                    String subscriberId = dbController.getSubscriberIdFromBorrowId((String) receiveMsg.data);

                    // Getting a history file of subscriber
                    List<String[]> historyList = dbController.getHistoryFileBySubscriberId(subscriberId);

                    // Document return of a book
                    List<String[]> newHistoryList = documentationController.documentOnReaderCard("109", historyList, bookName, null);

                    // Updating a subscriber history file in DB
                    dbController.handleUpdateHistoryFileBySubscriberId(subscriberId, newHistoryList);
                }

                // account is frozen
                if (((List<Boolean>) responseMsg.getData()).get(1))
                {
                    // Getting book name by borrow ID
                    String bookName = (dbController.getBookDetailsByBorrowId((String) receiveMsg.data)).getTitle();

                    // Getting subscriber ID from DB using borrow ID
                    String subscriberId = dbController.getSubscriberIdFromBorrowId((String) receiveMsg.data);

                    // Getting a history file of subscriber
                    List<String[]> historyList = dbController.getHistoryFileBySubscriberId(subscriberId);

                    // Document return of a book
                    List<String[]> newHistoryList = documentationController.documentOnReaderCard("109-1", historyList, bookName, null);

                    // Updating a subscriber history file in DB
                    dbController.handleUpdateHistoryFileBySubscriberId(subscriberId, newHistoryList);
                }

                break;

            case "110":
                // Request to return all the active subscriber borrowed books
                responseMsg = new MessageType("210", dbController.handleGetSubscriberBorrowList((String) receiveMsg.data));
                break;

            case "111":
                // Request by the subscriber to extend book borrow
                responseMsg = new MessageType("211", dbController.handleSubscriberExtendBorrow((List<String>) receiveMsg.data));
                // Casting receiveMsg into List<String> type
                List<String> dataList = (List<String>) receiveMsg.data;
                // Getting borrow id
                String borrowId = dataList.get(0);
                // Getting book name by borrow ID
                String bookName = (dbController.getBookDetailsByBorrowId(borrowId)).getTitle();

                // Getting subscriber ID by borrow ID
                String subscriberId = dbController.getSubscriberIdFromBorrowId(borrowId);
                // Getting a history file of subscriber
                List<String[]> historyList = dbController.getHistoryFileBySubscriberId(subscriberId);

                if ((boolean) responseMsg.getData())
                {
                    // Document extend granted
                    List<String[]> newHistoryList = documentationController.documentOnReaderCard("111-1", historyList, bookName, ((List<String>) receiveMsg.getData()).get(1));
                    // Updating a subscriber history file in DB
                    dbController.handleUpdateHistoryFileBySubscriberId(subscriberId, newHistoryList);

                    // get subscriber name by id
                    String subscriberName = dbController.GetSubscriberNameById(subscriberId);

                    // send notification to the librarian
                    HandleSendExtensionNotificationToLibrarian("Subscriber " + subscriberName + " requested to extend borrow of book " + bookName);
                }
                else
                {
                    // Updating a subscriber history file
                    List<String[]> newHistoryList = documentationController.documentOnReaderCard("111-2", historyList, bookName, null);
                    // Updating a subscriber history file in DB
                    dbController.handleUpdateHistoryFileBySubscriberId(subscriberId, newHistoryList);
                }
                break;

            case "112":
                // Request to get all the subscriber history
                // Old subscriber details
                responseMsg = new MessageType("212", dbController.handleReturnSubscriberHistory((String) receiveMsg.data));
                break;

            case "113":
                // Request to update subscriber email and phone number
                Subscriber oldSubscriber = dbController.getSubscriberBySubscriberId(((Subscriber) receiveMsg.data).getId());

                responseMsg = new MessageType("213", dbController.handleUpdateSubscriberDetails((Subscriber) receiveMsg.data));

                if ((boolean) responseMsg.getData())
                {
                    // Fetch subscriber details after update in DB
                    Subscriber updatedSubscriber = dbController.getSubscriberBySubscriberId(oldSubscriber.getId());
                    // Getting a history file of subscriber
                    historyList = dbController.getHistoryFileBySubscriberId(updatedSubscriber.getId());

                    // Old and new details that can be changed
                    String newPhone = updatedSubscriber.getPhone();
                    String newMail = updatedSubscriber.getEmail();
                    String oldPhone = oldSubscriber.getPhone();
                    String oldMail = oldSubscriber.getEmail();

                    // Setting the new list to the old list in-case there will be no changes
                    List<String[]> newHistoryList = historyList;

                    // Case for phone number and mail change
                    if (!Objects.equals(newMail, oldMail) && !Objects.equals(newPhone, oldPhone))
                    {
                        // Document phone number and mail change
                        newHistoryList = documentationController.documentOnReaderCard("113-3", historyList, newPhone, newMail);
                    }
                    // Case for only mail change
                    else if (!Objects.equals(newMail, oldMail))
                    {
                        // Document mail change
                        newHistoryList = documentationController.documentOnReaderCard("113-2", historyList, newMail, null);
                    }
                    // Case for only phone number change
                    else if (!Objects.equals(newPhone, oldPhone))
                    {
                        // Document phone number change
                        newHistoryList = documentationController.documentOnReaderCard("113-1", historyList, newPhone, null);
                    }
                    // Updating a subscriber history file in DB
                    dbController.handleUpdateHistoryFileBySubscriberId(updatedSubscriber.getId(), newHistoryList);
                }
                break;

            case "114":
                // Request to update subscriber password
                responseMsg = new MessageType("214", dbController.handleUpdateSubscriberPassword((List<String>) receiveMsg.data));
                if ((boolean) responseMsg.getData())
                {
                    // Getting the history file of the subscriber by his id
                    historyList = dbController.getHistoryFileBySubscriberId(((List<String>) receiveMsg.getData()).get(0));
                    // Document updated password
                    List<String[]> newHistoryList = documentationController.documentOnReaderCard("114", historyList, null, null);
                    // Updating a subscriber history file in DB
                    dbController.handleUpdateHistoryFileBySubscriberId(((List<String>) receiveMsg.getData()).get(0), newHistoryList);
                }
                break;

            case "115":
                // Request to get all the subscribers in the DB
                responseMsg = new MessageType("215", dbController.handleGetAllSubscribers());
                break;

            case "116":
                // Request to check if the subscriber in the DB
                responseMsg = new MessageType("216", dbController.handleCheckSubscriberStatus((String) receiveMsg.data));
                break;

            case "117":
                // Request by the librarian to manually extend book borrow
                responseMsg = new MessageType("217", dbController.handleLibrarianExtendBorrow((List<String>) receiveMsg.data));

                if ((boolean) responseMsg.getData())
                {
                    // Getting subscriber ID using borrow ID
                    subscriberId = dbController.getSubscriberIdFromBorrowId(((List<String>) receiveMsg.getData()).get(0));
                    // Getting book name by borrow ID
                    bookName = (dbController.getBookDetailsByBorrowId(((List<String>) receiveMsg.data).get(0)).getTitle());
                    // Getting a history file of subscriber
                    historyList = dbController.getHistoryFileBySubscriberId(subscriberId);
                    // Document manual extension
                    List<String[]> newHistoryList = documentationController.documentOnReaderCard("117", historyList, bookName, null);
                    // Updating a subscriber history file in DB
                    dbController.handleUpdateHistoryFileBySubscriberId(subscriberId, newHistoryList);
                }
                break;

            case "118":
                // Request to update copy of the book to lost status
                responseMsg = new MessageType("218", dbController.handleUpdateBookCopyToLost((String) receiveMsg.data));

                if ((boolean) responseMsg.getData())
                {
                    // Getting subscriber ID using borrow ID
                    subscriberId = dbController.getSubscriberIdFromBorrowId((String) receiveMsg.data);

                    // Getting book name by borrow ID
                    bookName = (dbController.getBookDetailsByBorrowId((String) receiveMsg.data)).getTitle();

                    // Getting a history file of subscriber
                    historyList = dbController.getHistoryFileBySubscriberId(subscriberId);

                    // Document lost book
                    List<String[]> newHistoryList = documentationController.documentOnReaderCard("118", historyList, bookName, null);
                    // Updating a subscriber history file in DB
                    dbController.handleUpdateHistoryFileBySubscriberId(subscriberId, newHistoryList);
                }
                break;
            case "119":
                // Request to get the list of active orders of a subscriber
                responseMsg = new MessageType("219", dbController.handleGetSubscriberActiveOrders((String) receiveMsg.data));
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
                //check if report is ready
                responseMsg = new MessageType("227", dbController.checkIfReportIsReady((List<String>) receiveMsg.data));
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
        dbController = DbController.getInstance();

        // start the schedulers
        SchedulerController.getInstance().startSchedulers();
    }

    /**
     * This method overrides the one in the superclass.  Called
     * when the server stops listening for connections.
     */
    protected void serverStopped()
    {
        System.out.println("Server has stopped listening for connections.");
        SchedulerController.getInstance().stopSchedulers();
    }

    /**
     * This method is called when the server is connected to a client.
     *
     * @param client the connection connected to the client.
     */
    @Override
    protected void clientConnected(ConnectionToClient client)
    {
        monitorController.clientConnected(client);
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
     * This method handles the received "1002" message and updates the active subscribers/librarians list
     *
     * @param receiveMsg - received message from the client
     */
    private void handleLogoutRequest(List<String> receiveMsg)
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
        MessageType responseMsg;
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

    /**
     * The method handle to send sms to subscriber
     *
     * @param subscriberId - the subscriber id
     * @param message      - the message to send
     */
    public static void HandleSendSmsToSubscriber(String subscriberId, String message)
    {
        // check if the subscriber is connected
        if (activeSubscribers.containsKey(subscriberId))
        {
            try
            {
                activeSubscribers.get(subscriberId).sendToClient(new MessageType("222", message));
            }
            catch (IOException e)
            {
                System.out.println("Error sending message to subscriber");
            }
        }
        else
        {
            // add the subscriber message to the table in the DB
            dbController.handleSaveSubscriberMissedSms(subscriberId, message);
        }
    }

    /**
     * The method handle send notification to librarian in the system
     *
     * @param message - the message to send
     */
    private void HandleSendExtensionNotificationToLibrarian(String message)
    {
        // check if there is a connected librarian
        if (!activeLibrarians.isEmpty())
        {
            for (ConnectionToClient librarian : activeLibrarians.values())
            {
                try
                {
                    librarian.sendToClient(new MessageType("2111", message));
                }
                catch (IOException e)
                {
                    System.out.println("Error sending message to librarian");
                }
            }
        }

        // save the message in the DB
        dbController.handleSaveLibrarianMessage(message);
    }

}
