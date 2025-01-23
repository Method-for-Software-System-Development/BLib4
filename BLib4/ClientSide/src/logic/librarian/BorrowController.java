package logic.librarian;

import entities.logic.MessageType;
import logic.communication.ChatClient;
import logic.communication.ClientUI;
import logic.user.BooksController;

import java.util.ArrayList;

public class BorrowController {
    private static volatile BorrowController instance = null;
    private static BooksController BookController = BooksController.getInstance();

    /*
     * Private constructor
     */
    private BorrowController() {
    }

    /**
     * get the instance of the BorrowController for singleton
     *
     * @return the instance of BorrowController
     */
    public static BorrowController getInstance() {
        if (instance == null) {
            synchronized (BorrowController.class) {
                if (instance == null) {
                    instance = new BorrowController();
                }
            }
        }
        return instance;
    }

    /**
     * Creating a new borrow for subscriber
     *
     * @param enteredSubscriberID Subscriber ID input via the librarian in GUI
     * @param enteredCopyBookID   Copy ID input via librarian in GUI for the book to borrow
     * @return Return value return from query in DB
     */
    public int createNewBorrow(String enteredSubscriberID, String enteredCopyBookID) {
        try {
            // Create a list to be sent in the message to the server
            ArrayList<String> detailsOfBorrow = new ArrayList<>();
            detailsOfBorrow.add(enteredSubscriberID);
            detailsOfBorrow.add(enteredCopyBookID);

            // Sending the server subscriberID and bookID
            ClientUI.chat.accept(new MessageType("107", detailsOfBorrow));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        // Returning query return value
        return ChatClient.borrowHandle;
    }

    /**
     * Checking if Subscriber ID is in the list of ordered books
     *
     * @param SubscriberID Subscriber ID entered by librarian
     * @return Return true if the subscriber is on the wait list
     */
    public boolean isSubscriberInWaitList(String SubscriberID) {
        for (String Subid : BookController.getSubscriberIdWithOrder()) {
            if (SubscriberID.equals(Subid)) {
                return true;
            }
        }
        return false;
    }

    public boolean extendBorrow(String Borrow_id, String selectedDate) {
        // Building an ArrayList in order to send a MessageType Object in order to send to server
        ArrayList<String> listToSend = new ArrayList<>();
        listToSend.add(Borrow_id);
        listToSend.add(selectedDate);
        listToSend.add(ChatClient.librarian.getId());
        try {
            // Sending to the server MessageType 117
            ClientUI.chat.accept(new MessageType("117", listToSend));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return ChatClient.serverResponse;
    }

    public int returnBorrow(String Borrow_id) {
        ArrayList<Boolean> returnOutcome;
        try {
            // Sending to the server MessageType 109
            ClientUI.chat.accept(new MessageType("109", Borrow_id));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        returnOutcome = ChatClient.returnOutcome;
        if (returnOutcome.get(0) == true && returnOutcome.get(1) == false) { // if the book is returned successfully, no more than 1 week late
            return 1;
        } else if (returnOutcome.get(0) == true && returnOutcome.get(1) == true) { // if the book is returned successfully, more than 1 week late > subscriber is frozen
            return 2;
        } else if (returnOutcome.get(0) == false) { // if the book is not returned successfully (Error)
            return 3;
        }
        return -1; // must do it for the compiler
    }
}