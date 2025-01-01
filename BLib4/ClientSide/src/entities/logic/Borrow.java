package entities.logic;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

public class Borrow {
	private Date brrowDate;
	private Date dueDate;
	private Date returnDate;
	private String status;
	private int lateTime;
	private HashMap<String,ArrayList<Date>> manualExtention = new HashMap<>();
	
	/**
	 * Constructor to initialize a Borrow record
	 *
	 * @param brrowDate       	The date when the book was borrowed
	 * @param dueDate         	The due date for returning the book
	 * @param returnDate      	The actual return date of the book
	 * @param status          	The current status of the borrow (e.g., "Active", "Returned", "Overdue")
	 * @param lateTime        	The amount of time (in days) the return was late
	 * @param manualExtention 	A map of manual extension dates, with the reason as the key and the extended date as the value
	 */
	public Borrow(Date brrowDate, Date dueDate, Date returnDate, String status, int lateTime,
				  HashMap<String, ArrayList<Date>> manualExtention) {
		this.brrowDate = brrowDate;
		this.dueDate = dueDate;
		this.returnDate = returnDate;
		this.status = status;
		this.lateTime = lateTime;
		this.manualExtention = manualExtention;
	}

	public Date getBrrowDate() {
		return brrowDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public String getStatus() {
		return status;
	}

	public int getLateTime() {
		return lateTime;
	}

	public HashMap<String, ArrayList<Date>> getManualExtention() {
		return manualExtention;
	}
	
	public void addManualExtention(String librarian, Date newDate) {
		if(!manualExtention.containsKey(librarian)) {
			manualExtention.put(librarian, new ArrayList<Date>());
		}
		manualExtention.get(librarian).add(newDate);
	}
	
}