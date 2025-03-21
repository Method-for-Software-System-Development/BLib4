package gui.user.subscriberUI;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents a single borrow entry in the table.
 */
public class OrderEntry
{
    private final StringProperty orderId;
    private final StringProperty bookId;
    private final StringProperty bookTitle;
    private final StringProperty orderDate;

    /**
     * Constructor for OrderEntry.
     *
     * @param orderId   - The ID of the order.
     * @param bookId    - The ID of the book.
     * @param bookTitle - The title of the book.
     * @param orderDate - The date the book was ordered.
     */
    public OrderEntry(String orderId, String bookId, String bookTitle, String orderDate)
    {
        this.orderId = new SimpleStringProperty(orderId);
        this.bookId = new SimpleStringProperty(bookId);
        this.bookTitle = new SimpleStringProperty(bookTitle);
        this.orderDate = new SimpleStringProperty(orderDate); // Format date
    }

    /**
     * Property getters of order id
     * @return - The property of the order id.
     */
    public StringProperty orderIdProperty()
    {
        return orderId;
    }

    /**
     * Property getters of book id
     * @return - The property of the book id.
     */
    public StringProperty bookIdProperty()
    {
        return bookId;
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
     * Property getters of order date
     * @return - The property of the order date.
     */
    public StringProperty orderDateProperty()
    {
        return orderDate;
    }

    /**
     * Getters for order id
     * @return - The order id.
     */
    public String getOrderId()
    {
        return orderId.get();
    }

    /**
     * Getters for a book id
     * @return - The book id.
     */
    public String getBookId()
    {
        return bookId.get();
    }

    /**
     * Getters for a book title
     * @return - The book title.
     */
    public String getBookTitle()
    {
        return bookTitle.get();
    }

    /**
     * Getters for an order date
     * @return - The order date.
     */
    public String getOrderDate()
    {
        return orderDate.get();
    }
}
