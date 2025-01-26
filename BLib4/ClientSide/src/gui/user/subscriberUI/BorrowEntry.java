package gui.user.subscriberUI;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents a single borrow entry in the table.
 */
public class BorrowEntry
{
    private final StringProperty borrowId;
    private final StringProperty copyId;
    private final StringProperty bookTitle;
    private final StringProperty borrowDate;
    private final StringProperty dueDate;

    /**
     * Constructor for BorrowEntry.
     *
     * @param borrowId   - The ID of the borrow.
     * @param copyId     - The ID of the copy.
     * @param bookTitle  - The title of the book.
     * @param borrowDate - The date the book was borrowed.
     * @param dueDate    - The due date for returning the book.
     */
    public BorrowEntry(String borrowId, String copyId, String bookTitle, String borrowDate, String dueDate)
    {
        this.borrowId = new SimpleStringProperty(borrowId);
        this.copyId = new SimpleStringProperty(copyId);
        this.bookTitle = new SimpleStringProperty(bookTitle);
        this.borrowDate = new SimpleStringProperty(formatDate(borrowDate)); // Format date
        this.dueDate = new SimpleStringProperty(formatDate(dueDate));       // Format date
    }

    /**
     * Property getters of borrow id
     * @return - The property of the borrow id.
     */
    public StringProperty borrowIdProperty()
    {
        return borrowId;
    }

    /**
     * Property getters of copy id
     * @return - The property of the copy id.
     */
    public StringProperty copyIdProperty()
    {
        return copyId;
    }

    /**
     * Property getters of book title
     * @return - The property of the book title.
     */
    public StringProperty bookTitleProperty()
    {
        return bookTitle;
    }

    /**
     * Property getters of borrow date
     * @return - The property of the borrow date.
     */
    public StringProperty borrowDateProperty()
    {
        return borrowDate;
    }

    /**
     * Property getters of due date
     * @return - The property of the due date.
     */
    public StringProperty dueDateProperty()
    {
        return dueDate;
    }

    /**
     * Get the borrow id.
     * @return - The borrow id.
     */
    public String getBorrowId()
    {
        return borrowId.get();
    }

    /**
     * Get the copy id.
     * @return - The copy id.
     */
    public String getCopyId()
    {
        return copyId.get();
    }

    /**
     * Get the book title.
     * @return - The book title.
     */
    public String getBookTitle()
    {
        return bookTitle.get();
    }

    /**
     * Get the borrow date.
     * @return - The borrow date.
     */
    public String getBorrowDate()
    {
        return borrowDate.get();
    }

    /**
     * Get the due date.
     * @return - The due date.
     */
    public String getDueDate()
    {
        return dueDate.get();
    }

    /**
     * Format the date to dd/mm/yyyy.
     * @param date - The date to format.
     * @return - The formatted date.
     */
    private String formatDate(String date)
    {
        try
        {
            String[] parts = date.split("-");
            return parts[2] + "/" + parts[1] + "/" + parts[0]; // Convert yyyy-mm-dd to dd/mm/yyyy
        }
        catch (Exception e)
        {
            return date; // Return the original date if formatting fails
        }
    }
}
