package gui.librarian.librarianUI;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SubscriberEntry
{

    private final StringProperty userId;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty status;

    /**
     * Constructor for SubscriberEntry.
     *
     * @param userId    - The user ID of the subscriber.
     * @param firstName - The first name of the subscriber.
     * @param lastName  - The last name of the subscriber.
     * @param status    - The status of the subscriber.
     */
    public SubscriberEntry(String userId, String firstName, String lastName, String status)
    {
        this.userId = new SimpleStringProperty(userId);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.status = new SimpleStringProperty(status);
    }

    /**
     * Property getters of id.
     *
     * @return - The property of the id.
     */
    public StringProperty userIdProperty()
    {
        return userId;
    }

    /**
     * Property getters of first name.
     *
     * @return - The property of the first name.
     */
    public StringProperty firstNameProperty()
    {
        return firstName;
    }

    /**
     * Property getters of last name.
     *
     * @return - The property of the last name.
     */
    public StringProperty lastNameProperty()
    {
        return lastName;
    }

    /**
     * Property getters of status.
     *
     * @return - The property of the status.
     */
    public StringProperty statusProperty()
    {
        return status;
    }

    /**
     * Getters for the subscriber's user ID.
     *
     * @return - The user ID of the subscriber.
     */
    public String getUserId()
    {
        return userId.get();
    }

    /**
     * Getters for the subscriber's first name.
     *
     * @return - The first name of the subscriber.
     */
    public String getFirstName()
    {
        return firstName.get();
    }

    /**
     * Getters for the subscriber's last name.
     *
     * @return - The last name of the subscriber.
     */
    public String getLastName()
    {
        return lastName.get();
    }

    /**
     * Getters for the subscriber's status.
     *
     * @return - The status of the subscriber.
     */
    public String getStatus()
    {
        return status.get();
    }
}


