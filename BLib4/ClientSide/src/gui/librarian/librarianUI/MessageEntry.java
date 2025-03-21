package gui.librarian.librarianUI;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents a single message entry in the table.
 */
public class MessageEntry
{

    private final StringProperty messageId;
    private final StringProperty messageDateTime;
    private final StringProperty messageContent;

    /**
     * Constructor for a message entry.
     *
     * @param messageId      - The ID of the message.
     * @param messageDateTime    - The date and time the message was sent.
     * @param messageContent - The content of the message.
     */
    public MessageEntry(String messageId, String messageDateTime, String messageContent)
    {
        this.messageId = new SimpleStringProperty(messageId);
        this.messageDateTime = new SimpleStringProperty(messageDateTime);
        this.messageContent = new SimpleStringProperty(messageContent);
    }

    /**
     * Getters for the properties id.
     *
     * @return - The property of the specified value.
     */
    public StringProperty messageIdProperty()
    {
        return messageId;
    }

    /**
     * Getters for the property date and time.
     *
     * @return - The property of the specified value.
     */
    public StringProperty messageDateTimeProperty()
    {
        return messageDateTime;
    }

    /**
     * Getters for the property content.
     *
     * @return - The property of the specified value.
     */
    public StringProperty messageContentProperty()
    {
        return messageContent;
    }

    /**
     * Getters for the message ID.
     *
     * @return - The message ID.
     */
    public String getMessageId()
    {
        return messageId.get();
    }

    /**
     * Getters for the message date and time.
     *
     * @return - The message date and time.
     */
    public String getMessageDateTime()
    {
        return messageDateTime.get();
    }

    /**
     * Getters for the message content.
     *
     * @return - The message content.
     */
    public String getMessageContent()
    {
        return messageContent.get();
    }
}

