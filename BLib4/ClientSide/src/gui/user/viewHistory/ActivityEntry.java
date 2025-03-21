package gui.user.viewHistory;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents a single activity entry in the table.
 */
public class ActivityEntry
{

    private final StringProperty activityDate;
    private final StringProperty activityTime;
    private final StringProperty activityDetails;

    /**
     * Constructor for ActivityEntry.
     *
     * @param activityDate    - The date of the activity.
     * @param activityTime    - The time of the activity.
     * @param activityDetails - The details of the activity.
     */
    public ActivityEntry(String activityDate, String activityTime, String activityDetails)
    {
        this.activityDate = new SimpleStringProperty(activityDate);
        this.activityTime = new SimpleStringProperty(activityTime);
        this.activityDetails = new SimpleStringProperty(activityDetails);
    }

    /**
     * Property getters of activity date
     *
     * @return - The property of the activity date.
     */
    public StringProperty activityDateProperty()
    {
        return activityDate;
    }

    /**
     * Property getters of activity time
     *
     * @return - The property of the activity time.
     */
    public StringProperty activityTimeProperty()
    {
        return activityTime;
    }

    /**
     * Property getters of activity details
     *
     * @return - The property of the activity details.
     */
    public StringProperty activityDetailsProperty()
    {
        return activityDetails;
    }

    /**
     * Getters for the activity date, time, and details.
     * @return - The activity date, time, and details.
     */
    public String getActivityDate()
    {
        return activityDate.get();
    }

    /**
     * Getters for the activity date, time, and details.
     * @return - The activity date, time, and details.
     */
    public String getActivityTime()
    {
        return activityTime.get();
    }

    /**
     * Getters for the activity date, time, and details.
     * @return - The activity date, time, and details.
     */
    public String getActivityDetails()
    {
        return activityDetails.get();
    }
}
