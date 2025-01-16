package gui.testing;

import java.util.ArrayList;

import entities.logic.MessageType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import logic.communication.ChatClient;
import logic.communication.ClientUI;
import logic.librarian.BorrowController;
import logic.user.BooksController;

public class BorrowViewController {
	@FXML
	private TextField txtBookID; // Input field for Book ID Barcode

	@FXML
	private TextField txtSubscriberID; // Input of Subscriber ID

	@FXML
	private Button btnBorrow; // Declaration for the button to borrow a book

	@FXML
	private TextField txtBorrowID; // Declaration of text field to return the book in the return UI

	@FXML
	private Button btnReturn; // Declaration of return button in return UI

	@FXML
	private Button btnExtend; // Declaration on extend button

	@FXML
	private DatePicker datePicker; // Declaration on date picker field

	private static BorrowViewController instance = null;

	/*
	 * Private constructor
	 */
	private BorrowViewController() {
	}

	/*
	 * getInstance is singleton for BorrowController
	 */
	public BorrowViewController getInstance() {
		if (instance == null) {
			instance = new BorrowViewController();
			return instance;
		}
		return instance;
	}

	/*
	 * Handles the borrow button action. Sends subscriber ID and book ID to the
	 * server for validation (Message Type 107). Waits for server response (Message
	 * Type 207).
	 */
	public void createNewBorrow(String SubscriberID, String CopyID) {
		try {
			if (BooksController.getInstance().checkAvailability(CopyID)) {
				// Create a list to be sent in the message to the server
				ArrayList<String> detailsOfBorrow = new ArrayList<>();
				detailsOfBorrow.add(SubscriberID);
				detailsOfBorrow.add(CopyID);

				// Sending the server subscriberID and bookID
				ClientUI.chat.accept(new MessageType("107", detailsOfBorrow));
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	/*
	 * Method to extend the due date of a borrow. It retrieves the borrow ID and the
	 * new selected date from the UI, builds an ArrayList to send to the server, and
	 * handles the server response.
	 */
	public void ManualextendBorrow(String BorrowID, String SelectedDate) {
		try {
			// Building an ArrayList in order to send a MessageType Object in order to send
			// to the server
			ArrayList<String> listToSend = new ArrayList<>();
			listToSend.add(BorrowID);
			listToSend.add(SelectedDate);
			listToSend.add(ChatClient.librarian.getId());

			// Sending to the server MessageType 117
			ClientUI.chat.accept(new MessageType("117", listToSend));

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	/*
	 * Method to return a borrowed item. It retrieves the borrow ID from the UI,
	 * sends it to the server, and handles the response to confirm if the return was
	 * successful or failed.
	 */
	public void returnBorrow(String BorrowID) {
		try {
			// Sending to the server MessageType 109
			ClientUI.chat.accept(new MessageType("109", BorrowID));
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}
