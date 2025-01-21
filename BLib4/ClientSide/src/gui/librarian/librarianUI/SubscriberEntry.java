package gui.librarian.librarianUI;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SubscriberEntry {

    private final StringProperty userId;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty status;

    public SubscriberEntry(String userId, String firstName, String lastName, String status) {
        this.userId = new SimpleStringProperty(userId);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.status = new SimpleStringProperty(status);
    }

    // Getters for properties
    public StringProperty userIdProperty() {
        return userId;
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public StringProperty statusProperty() {
        return status;
    }

    // Getters for values
    public String getUserId() {
        return userId.get();
    }

    public String getFirstName() {
        return firstName.get();
    }

    public String getLastName() {
        return lastName.get();
    }

    public String getStatus() {
        return status.get();
    }
}


