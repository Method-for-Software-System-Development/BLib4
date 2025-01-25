package entities.report;

import java.util.Date;

public abstract class Report
{
    private int reportNum;
    private String month;
    private String year;
    private Date issueDate;
    private boolean validmonth = false;
    private boolean validyear = false;
    private boolean isIssued = false;
    private String[] months = {"january", "february", "march", "april", "may", "june",
            "july", "august", "september", "october", "november", "december"};

    /**
     * Constructor for the Report class
     *
     * @param reportNum - the report number
     * @param month     - the month of the report
     * @param year      - the year of the report
     * @param issueDate - the date the report was issued
     */
    public Report(int reportNum, String month, String year, Date issueDate)
    {
        this.reportNum = reportNum;
        this.issueDate = issueDate;
        this.month = validateMonth(month);
        this.year = validateYear(year);

    }

    /**
     * Method to check if the month given in the constructor is valid
     *
     * @param monthGiven - the month given in the constructor
     * @return - the month given in the constructor
     */
    public String validateMonth(String monthGiven)
    {
        if (monthGiven != null)
        {
            for (String month : months)
            {
                if (month.toLowerCase().equals(monthGiven))
                    validmonth = true; //Setting the flag to be true - month given is valid
                return monthGiven;
            }
        }
        return null;
    }

    /**
     * Method to check if the year given in the constructor is valid
     *
     * @param yearGiven - the year given in the constructor
     * @return - the year given in the constructor
     */
    private String validateYear(String yearGiven)
    {
        if (yearGiven != null && (Integer.parseInt(yearGiven) <= 2025 && Integer.parseInt(yearGiven) >= 0))
        {
            validyear = true; //Setting the flag to be true - year given is valid
            return yearGiven;
        }
        return null;
    }

    /**
     * Method to get the report number
     *
     * @return - the report number
     */
    public int getReportNum()
    {
        return reportNum;
    }

    /**
     * Method to get the month of the report
     *
     * @return - the month of the report
     */
    public String getMonth()
    {
        return month;
    }

    /**
     * Method to get the year of the report
     *
     * @return - the year of the report
     */
    public String getYear()
    {
        return year;
    }

    /**
     * Method to get the issue date of the report
     *
     * @return - the issue date of the report
     */
    public Date getIssueDate()
    {
        return issueDate;
    }

    /**
     * Method to check if the report can be issued according to the month and year given in the constructor
     *
     * @return - true if the report can be issued, false otherwise
     */
    private boolean canBeIssue()
    {
        return validmonth && validyear;
    }

    /**
     * Method to set the report as issued
     */
    public void setIssued()
    {
        if (canBeIssue())
        {
            isIssued = true;
        }
    }

    /**
     * The method to set the month of the report
     *
     * @param month - the month of the report
     */
    public void setMonth(String month)
    {
        if (!validmonth)
        {
            this.month = validateMonth(month);
        }
    }

    /**
     * The method to set the year of the report
     *
     * @param year - the year of the report
     */
    public void setYear(String year)
    {
        if (!validyear)
        {
            this.year = validateYear(year);
        }
    }

    /**
     * Method to get the status of the report
     *
     * @return - true if the report is issued, false otherwise
     */
    public boolean getIsIssued()
    {
        return isIssued;
    }
}
