package entities.logic;

public class Notification
{
    private int noteNum;
    private String sender;
    private String reciver;
    private String mean;
    private String content;

    /**
     * Constructor to initialize the Notification
     *
     * @param noteNum  The unique number identifying the notification
     * @param sender   The sender of the notification
     * @param receiver The receiver of the notification
     * @param mean     The medium or means of the notification (e.g., email, SMS)
     * @param content  The content or message of the notification
     */
    public Notification(int noteNum, String sender, String receiver, String mean, String content)
    {
        this.noteNum = noteNum;
        this.sender = sender;
        this.reciver = receiver;
        this.mean = mean;
        this.content = content;
    }


    /**
     * Get the unique number identifying the notification
     *
     * @return The unique number identifying the notification
     */
    public int getNoteNum()
    {
        return noteNum;
    }

    /**
     * Get the sender of the notification
     *
     * @return The sender of the notification
     */
    public String getSender()
    {
        return sender;
    }

    /**
     * Get the receiver of the notification
     *
     * @return The receiver of the notification
     */
    public String getReceiver()
    {
        return reciver;
    }

    /**
     * Get the medium or means of the notification
     *
     * @return The medium or means of the notification
     */
    public String getMean()
    {
        return mean;
    }

    /**
     * Get the content or message of the notification
     *
     * @return The content or message of the notification
     */
    public String getContent()
    {
        return content;
    }
}
