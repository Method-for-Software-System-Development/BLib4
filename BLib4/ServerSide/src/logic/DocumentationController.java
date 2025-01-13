package logic;

import java.util.List;
import java.sql.Date;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DocumentationController {
	
	// Static variable to hold the single instance
    private static DocumentationController instance;

    private DocumentationController() {
    }
    /**
     * Public method to get the single instance of DocumentationController.
     * If the instance does not exist, it is created.
     *
     * @return The single instance of DocumentationController.
     */
    public static DocumentationController getInstance() {
        if (instance == null) {
            synchronized (DocumentationController.class) { // Ensure thread safety
                if (instance == null) {
                    instance = new DocumentationController();
                }
            }
        }
        return instance;
    }
    
    /*
     * Generates documentation for a reader card.
     *
     * This method processes a subscriber's detailed subscription history,
     * extracts relevant data such as copy ID, borrow date, and return date,
     * and saves the information into a CSV file for the subscriber.
     *
     * @param subscriberId  The unique identifier of the subscriber whose history is to be documented.
     * @param filePath      The path where the generated CSV file will be saved.
     */
    public List<String[]> documentOnReaderCard(String id, List<String[]> readerCard, String obj, String obj2) {
    	String[] addToReaderCard = new String[3];
    	// Get the current time
        LocalTime currentTime = LocalTime.now();
        // Format to display only hours and minutes
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        // Adding to reader card in place 1 current time and in place 2 the current date
        addToReaderCard[1] = currentTime.format(formatter);
        addToReaderCard[2] = new Date(System.currentTimeMillis()).toString();
    	switch (id) 
    	{
    	case "104":
    		// New subscriber document on reader card
    		addToReaderCard[0] = "Subscriber signed up for the library";
    		break;
    		
    	case "107":
    		// Borrowing book document on reader card
    		addToReaderCard[0] = "Subscriber borrowed book: " + obj + " with due date of: " + obj2;
    		break;
    		
    	case "108":
    		// Order book document on reader card
    		addToReaderCard[0] = "Subscriber ordered book: " + obj;
    		break;
    		
    	case "109":
    		// Return of a book document on reader card
    		addToReaderCard[0] = "Subscriber returned book: " + obj;
    		break;
    		
    	case "111-1":
    		// Extension of a book granted document on reader card
    		addToReaderCard[0] = "Subscriber extended borrow time for book: " + obj + " was granted, new reutrn date: " + obj2;
    		break;
    	
    	case "111-2":
    		// Extension of a book denied document on reader card
    		addToReaderCard[0] = "Subscriber denied extention borrow time for book: " + obj;
    		break;
    		
    	case "113-1":
    		// Update phone number only document on reader card
    		addToReaderCard[0] = "Subscriber updated phone number to " + obj;
    		break;
    		
    	case "113-2":
    		// Update Email only document on reader card
    		addToReaderCard[0] = "Subscriber updated Email to " + obj;
    		break;
    		
    	case "113-3":
    		// Update phone number and Email document on reader card
    		addToReaderCard[0] = "Subscriber updated phone number to: " + obj + " and Email to: " + obj2;
    		break;
    		
    	case "114":
    		// Update password document on reader card
    		addToReaderCard[0] = "Subscriber update password";
    		break;
    		
    	case "117":
    		// Manual extension document on reader card
    		addToReaderCard[0] = "Manual extention granted by librarian to the book: " + obj;
    		break;
    		
    	default:
    		System.out.println("Invalid MessageType");
    		return readerCard;
    	}
    	// Returning updated reader card
		readerCard.add(addToReaderCard);
        return readerCard;
    }

}
