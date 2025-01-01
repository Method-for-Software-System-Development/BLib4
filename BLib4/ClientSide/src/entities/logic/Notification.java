package entities.logic;

public class Notification {
	private int noteNum;
	private String sender;
	private String reciver;
	private String mean;
	private String content;
	
	/**
	 * Constructor to initialize the Notification
	 *
	 * @param noteNum   The unique number identifying the notification
	 * @param sender    The sender of the notification
	 * @param reciver   The receiver of the notification
	 * @param mean      The medium or means of the notification (e.g., email, SMS)
	 * @param content   The content or message of the notification
	 */
	public Notification(int noteNum, String sender, String reciver, String mean, String content) {
		this.noteNum = noteNum;
		this.sender = sender;
		this.reciver = reciver;
		this.mean = mean;
		this.content = content;
	}


	public int getNoteNum() {
		return noteNum;
	}


	public String getSender() {
		return sender;
	}


	public String getReciver() {
		return reciver;
	}


	public String getMean() {
		return mean;
	}


	public String getContent() {
		return content;
	}
	
	
}
