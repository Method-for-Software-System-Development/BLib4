package entities.report;


import java.util.Date;
import java.util.List;
import java.util.Map;

public class BorrowingReport extends Report
{
    private Map<String, List<String>> borrowingData;


    /**
     * Constructor to initialize the SubscriberStatusReport
     *
     * @param reportNum     The report number
     * @param Month         The report's month
     * @param Year          The report's year
     * @param issueDate     The date the report is issued
     * @param borrowingData The borrowing data
     */
    public BorrowingReport(int reportNum, String Month, String Year, Date issueDate, Map<String, List<String>> borrowingData)
    {
        super(reportNum, Month, Year, issueDate);
        this.borrowingData = borrowingData;
    }


    /**
     * Retrieves the borrowing data for a specific book.
     *
     * @param bookTitle The title of the book.
     * @return A list of strings containing the borrow data for the book.
     */
    public List<String> getBorrowingDataForBook(String bookTitle)
    {
        return borrowingData.get(bookTitle);
    }

    /**
     * Retrieves the borrowing data for all books.
     * @return A map containing the borrowing data for all books.
     */
    public Map<String, List<String>> getBorrowingData()
    {
        return borrowingData;
    }


}
