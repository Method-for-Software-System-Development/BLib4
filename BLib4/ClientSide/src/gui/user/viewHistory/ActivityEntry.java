package gui.user.viewHistory;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents a single activity entry in the table.
 */
public class ActivityEntry {

    private final StringProperty activityDate;
    private final StringProperty activityTime;
    private final StringProperty activityDetails;

    public ActivityEntry(String activityDate, String activityTime, String activityDetails) {
        this.activityDate = new SimpleStringProperty(activityDate);
        this.activityTime = new SimpleStringProperty(activityTime);
        this.activityDetails = new SimpleStringProperty(activityDetails);
    }

    // Getters for properties
    public StringProperty activityDateProperty() {
        return activityDate;
    }

    public StringProperty activityTimeProperty() {
        return activityTime;
    }

    public StringProperty activityDetailsProperty() {
        return activityDetails;
    }

    // Getters for values
    public String getActivityDate() {
        return activityDate.get();
    }

    public String getActivityTime() {
        return activityTime.get();
    }

    public String getActivityDetails() {
        return activityDetails.get();
    }
}
