package entities.report;

import java.util.Date;
import java.util.List;

public class SubscriberStatusReport extends Report
{
    private List<String[]> usersActivityStatus;

    /**
     * Constructor to initialize the SubscriberStatusReport
     *
     * @param reportNum           The report number
     * @param month               The report's month
     * @param year                The report's year
     * @param usersActivityStatus The report's users' activity status
     */
    public SubscriberStatusReport(int reportNum, String month, String year, Date issueDate, List<String[]> usersActivityStatus)
    {
        super(reportNum, month, year, issueDate);
        this.usersActivityStatus = usersActivityStatus;
    }

    /**
     * Get the users' activity status
     *
     * @return The users' activity status
     */
    public List<String[]> getUsersActivityStatus()
    {
        return usersActivityStatus;
    }
}
