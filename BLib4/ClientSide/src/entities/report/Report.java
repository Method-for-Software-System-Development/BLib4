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

    public Report(int reportNum, String Month, String Year, Date issueDate)
    {
        this.reportNum = reportNum;
        this.issueDate = issueDate;
        this.month = validateMonth(Month);
        this.year = validateYear(Year);

    }

    //Private method to check if the month given in the constructor is valid
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

    //Private method to check if the year given in the constructor is valid
    private String validateYear(String yearGiven)
    {
        if (yearGiven != null && (Integer.parseInt(yearGiven) <= 2025 && Integer.parseInt(yearGiven) >= 0))
        {
            validyear = true; //Setting the flag to be true - year given is valid
            return yearGiven;
        }
        return null;
    }

    public int getReportNum()
    {
        return reportNum;
    }

    public String getMonth()
    {
        return month;
    }

    public String getYear()
    {
        return year;
    }

    public Date getIssueDate()
    {
        return issueDate;
    }

    //Method to check if the report can be issued according to the month and year given in the constructor
    private boolean canBeIssue()
    {
        return validmonth && validyear;
    }

    //Method that set the
    public void setIssued()
    {
        if (canBeIssue())
        {
            isIssued = true;
        }
    }

    //Able to change the month of the report only if the month given before was invalid
    public void setMonth(String month)
    {
        if (!validmonth)
        {
            this.month = validateMonth(month);
        }
    }

    //Able to change the year of the report only if the year given before was invalid
    public void setYear(String year)
    {
        if (!validyear)
        {
            this.year = validateYear(year);
        }
    }

    public boolean getIsIssued()
    {
        return isIssued;
    }


}
