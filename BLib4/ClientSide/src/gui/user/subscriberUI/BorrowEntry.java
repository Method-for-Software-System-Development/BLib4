package gui.user.subscriberUI;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single borrow entry in the table.
 */
public class BorrowEntry {

    private final StringProperty borrowId;
    private final StringProperty copyId;
    private final StringProperty bookTitle;
    private final StringProperty borrowDate;
    private final StringProperty dueDate;

    public BorrowEntry(String borrowId, String copyId, String bookTitle, String borrowDate, String dueDate) {
        this.borrowId = new SimpleStringProperty(borrowId);
        this.copyId = new SimpleStringProperty(copyId);
        this.bookTitle = new SimpleStringProperty(bookTitle);
        this.borrowDate = new SimpleStringProperty(formatDate(borrowDate)); // Format date
        this.dueDate = new SimpleStringProperty(formatDate(dueDate));       // Format date
    }

    // Getters for properties
    public StringProperty borrowIdProperty() {
        return borrowId;
    }

    public StringProperty copyIdProperty() {
        return copyId;
    }

    public StringProperty bookTitleProperty() {
        return bookTitle;
    }

    public StringProperty borrowDateProperty() {
        return borrowDate;
    }

    public StringProperty dueDateProperty() {
        return dueDate;
    }

    // Getters for values
    public String getBorrowId() {
        return borrowId.get();
    }

    public String getCopyId() {
        return copyId.get();
    }

    public String getBookTitle() {
        return bookTitle.get();
    }

    public String getBorrowDate() {
        return borrowDate.get();
    }

    public String getDueDate() {
        return dueDate.get();
    }

    // Utility method to format dates
    private String formatDate(String date) {
        try {
            String[] parts = date.split("-");
            return parts[2] + "/" + parts[1] + "/" + parts[0]; // Convert yyyy-mm-dd to dd/mm/yyyy
        } catch (Exception e) {
            return date; // Return the original date if formatting fails
        }
    }
}
