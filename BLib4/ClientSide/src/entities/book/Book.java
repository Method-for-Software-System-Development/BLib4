package entities.book;

import javafx.scene.image.Image;

import java.util.Date;
import java.util.HashMap;

public class Book
{
    private final int book_id;
    private final String title;
    private final String author;
    private final int edition_number;
    private final Date printDate;
    private final String subject;
    private final String description;
    private Image image;
    private final HashMap<String, Date> waitlist = new HashMap<>();

    /**
     * Constructor to initialize the Book
     *
     * @param book_id        The unique identifier for the book
     * @param title          The title of the book
     * @param author         The author of the book
     * @param edition_number The edition number of the book
     * @param printDate      The date the book was printed
     * @param subject        The subject or genre of the book
     * @param description    A brief description of the book
     */
    public Book(int book_id, String title, String author, int edition_number, Date printDate, String subject, String description)
    {
        this.book_id = book_id;
        this.title = title;
        this.author = author;
        this.edition_number = edition_number;
        this.printDate = printDate;
        this.subject = subject;
        this.description = description;
    }

    /**
     * Getter to the book id
     * @return - The book id
     */
    public int getBookId()
    {
        return book_id;
    }

    /**
     * Getter to the book title
     * @return - The book title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Getter to the book author
     * @return - The book author
     */
    public String getAuthor() { return author; }

    /**
     * Getter to the book edition number
     * @return - The book edition number
     */
    public int getEditionNum()
    {
        return edition_number;
    }

    /**
     * Getter to the book print date
     * @return - The book print date
     */
    public Date getPrintDate()
    {
        return printDate;
    }

    /**
     * Getter to the book subject
     * @return - The book subject
     */
    public String getSubject()
    {
        return subject;
    }

    /**
     * Getter to the book description
     * @return - The book description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Getter to the book image
     * @return - The book image
     */
    public Image getImage()
    {
        return image;
    }

    /**
     * Get the book wait list
     * @return - The book wait list as a HashMap
     */
    public HashMap<String, Date> getWaitlist()
    {
        return waitlist;
    }

    /**
     * Set the book image
     * @param image - The book image
     */
    public void setImage(Image image)
    {
        this.image = image;
    }

    /**
     * Add a subscriber to the book wait list
     * @param SubscriberID - The subscriber id
     * @param orderDateTime - The date and time the subscriber ordered the book
     */
    public void addSubscriberToWaitlist(String SubscriberID, Date orderDateTime)
    {
        //Adding a subscriber order to the wait list only if he didn't order the book already
        if (!waitlist.containsKey(SubscriberID))
        {
            waitlist.put(SubscriberID, orderDateTime);
        }
    }

    /**
     * Remove a subscriber from the book wait list
     * @param SubscriberID - The subscriber id
     */
    public void removeSubscriberFromWaitlist(String SubscriberID)
    {
        waitlist.remove(SubscriberID);
    }
}
