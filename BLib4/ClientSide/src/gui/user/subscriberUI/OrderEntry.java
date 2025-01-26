package gui.user.subscriberUI;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents a single borrow entry in the table.
 */
public class OrderEntry {

    private final StringProperty orderId;
    private final StringProperty bookId;
    private final StringProperty bookTitle;
    private final StringProperty orderDate;

    public OrderEntry(String orderId, String bookId, String bookTitle, String orderDate) {
        this.orderId = new SimpleStringProperty(orderId);
        this.bookId = new SimpleStringProperty(bookId);
        this.bookTitle = new SimpleStringProperty(bookTitle);
        this.orderDate = new SimpleStringProperty(orderDate); // Format date
    }

    // Getters for properties
    public StringProperty orderIdProperty() {
        return orderId;
    }

    public StringProperty bookIdProperty() {
        return bookId;
    }

    public StringProperty bookTitleProperty() {
        return bookTitle;
    }

    public StringProperty orderDateProperty() {
        return orderDate;
    }

    // Getters for values
    public String getOrderId() {
        return orderId.get();
    }

    public String getBookId() {
        return bookId.get();
    }

    public String getBookTitle() {
        return bookTitle.get();
    }

    public String getOrderDate() {
        return orderDate.get();
    }

}
