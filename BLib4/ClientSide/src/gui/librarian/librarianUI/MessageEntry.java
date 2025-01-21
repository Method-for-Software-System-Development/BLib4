package gui.librarian.librarianUI;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents a single message entry in the table.
 */
public class MessageEntry {

    private final StringProperty messageId;
    private final StringProperty messageDate;
    private final StringProperty messageContent;

    public MessageEntry(String messageId, String messageDate, String messageContent) {
        this.messageId = new SimpleStringProperty(messageId);
        this.messageDate = new SimpleStringProperty(messageDate);
        this.messageContent = new SimpleStringProperty(messageContent);
    }

    // Getters for properties
    public StringProperty messageIdProperty() {
        return messageId;
    }

    public StringProperty messageDateProperty() {
        return messageDate;
    }

    public StringProperty messageContentProperty() {
        return messageContent;
    }

    // Getters for values
    public String getMessageId() {
        return messageId.get();
    }

    public String getMessageDate() {
        return messageDate.get();
    }

    public String getMessageContent() {
        return messageContent.get();
    }
}

