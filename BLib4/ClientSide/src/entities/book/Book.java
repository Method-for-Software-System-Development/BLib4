package entities.book;

import java.util.HashMap;
import java.util.Date;

public class Book {
	private final int book_id;
	private final String title;
	private final int edition_number;
	private final Date printDate;
	private final String subject;
	private final String description;
	private HashMap<String,String> waitlist = new HashMap<>();
	
	
	//Constructor without a subject
	public Book(int book_id, String title, int edition_number, Date printDate, String description) {
		this.book_id = book_id;
		this.title = title;
		this.edition_number = edition_number;
		this.printDate = printDate;
		this.subject = null;
		this.description = description;
	}
	
	//Constructor with all fields
	public Book(int book_id, String title, int edition_number, Date printDate, String subject, String description) {
		this.book_id = book_id;
		this.title = title;
		this.edition_number = edition_number;
		this.printDate = printDate;
		this.subject = subject;
		this.description = description;
	}

	public int getBookid() {
		return book_id;
	}

	public String getTitle() {
		return title;
	}

	public int getEditionNum() {
		return edition_number;
	}

	public Date getPrintDate() {
		return printDate;
	}

	public String getSubject() {
		return subject;
	}

	public String getDescription() {
		return description;
	}

	//Getter to all the wait list of the book
	public HashMap<String, String> getWaitlist() {
		return waitlist;
	}
	
	//Adding an order to the book wait list
	public void addSubscriberToWaitList(String SubscriberID, String orderDateTime) {
		//Adding a subscriber order to the wait list only if he didn't ordered the book already
		if(!waitlist.containsKey(SubscriberID)) {
			waitlist.put(SubscriberID, orderDateTime);
		}
	}
	
	//Removing an order from book wait list
	public void removeOrder(String SubscriberID) {
		//Checking if the wait list contains the subscriber id as a key in it in order to delete the order
		if(waitlist.containsKey(SubscriberID)) { 
			waitlist.remove(SubscriberID);
		}
	}
	
	
}
