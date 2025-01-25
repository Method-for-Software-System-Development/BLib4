package logic;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entities.book.Book;
import entities.user.Librarian;
import entities.user.Subscriber;

public class DbController
{
    private static volatile DbController instance = null;
    private Connection connection;

    /**
     * private constructor for the dbController
     */
    private DbController()
    {
        connect();
    }

    /**
     * get the instance of the dbController for singleton
     *
     * @return the instance of the dbController
     */
    public static DbController getInstance()
    {
        if (instance == null)
        {
            synchronized (DbController.class)
            {
                if (instance == null)
                {
                    instance = new DbController();
                }
            }
        }
        return instance;
    }

    /**
     * The method creates a connection to the db
     */
    public void connect()
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            System.out.println("Driver definition succeed");
        }
        catch (Exception ex)
        {
            /* handle the error*/
            System.out.println("Driver definition failed");
        }

        try
        {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost/blib4?serverTimezone=IST", "root", "Aa123456");
            System.out.println("SQL connection succeed");

        }
        catch (SQLException ex)
        {/* handle any errors*/
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    /**
     * The method run SQL query to get the subscriber from the db by id and password
     *
     * @param subscriberId - the id of the subscriber
     * @param password     - the password of the subscriber
     * @return - the subscriber if found, else null
     */
    public Subscriber handleSubscriberLogin(String subscriberId, String password)
    {
        Subscriber subscriber = null;
        PreparedStatement stmt;

        try
        {
            stmt = connection.prepareStatement("SELECT * from subscriber where subscriber_id = ? and subscriber_password = ?;");
            stmt.setString(1, subscriberId);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            // we have only on a result if existed, else zero.
            if (rs.next())
            {
                subscriber = new Subscriber(rs.getString("subscriber_id"),
                        rs.getString("subscriber_first_name"), rs.getString("subscriber_last_name"),
                        rs.getString("subscriber_phone_number"),
                        rs.getString("subscriber_email"), rs.getBoolean("is_active"));
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! subscriber login failed");
        }

        return subscriber;
    }

    /**
     * The method run SQL query to get the librarian from the db by id and password
     *
     * @param librarianId - the id of the librarian
     * @param password    - the password of the librarian
     * @return - the librarian if found, else null
     */
    public Librarian handleLibrarianLogin(String librarianId, String password)
    {
        Librarian librarian = null;
        PreparedStatement stmt;

        try
        {
            stmt = connection.prepareStatement("SELECT * from librarian where librarian_id = ? and librarian_password = ?;");
            stmt.setString(1, librarianId);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            // we have only on a result if existed, else zero.
            if (rs.next())
            {
                librarian = new Librarian(rs.getString("librarian_id"),
                        rs.getString("librarian_first_name"), rs.getString("librarian_last_name"));
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! librarian login failed");
        }

        return librarian;
    }

    /**
     * The method run SQL query to get the subscriber from the db by id
     *
     * @param subscriberID subscriber reader code - the ID of subscriber
     * @return the subscriber if log in succeed and null if not
     */
    public Subscriber handleLogInSubscriberByCard(String subscriberID)
    {
        Subscriber subscriber = null;
        PreparedStatement stmt;

        try
        {
            stmt = connection.prepareStatement("SELECT * from subscriber where subscriber_id = ?;");
            stmt.setString(1, subscriberID);
            ResultSet rs = stmt.executeQuery();

            // we have only on a result if existed, else zero.
            if (rs.next())
            {
                subscriber = new Subscriber(rs.getString("subscriber_id"),
                        rs.getString("subscriber_first_name"), rs.getString("subscriber_last_name"),
                        rs.getString("subscriber_phone_number"),
                        rs.getString("subscriber_email"), rs.getBoolean("is_active"));
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! subscriber login failed");
        }

        return subscriber;
    }

    /**
     * The method run SQL query to create a new subscriber in the db
     *
     * @param data - the data of the new subscriber
     * @return - true if the subscriber created, else false
     */
    public boolean handleSubscriberSignUp(List<String> data)
    {
        PreparedStatement stmt;

        try
        {
            //check that the subscriber id is not already in use
            stmt = connection.prepareStatement("SELECT * from subscriber where subscriber_id = ?;");
            stmt.setString(1, data.get(0));

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                return false;
            }


            //create a new row in the subscriber_history table
            stmt = connection.prepareStatement("INSERT INTO subsribers_history (id) SELECT MAX(id)+  1 FROM subsribers_history;");
            stmt.executeUpdate();

            // get the max id of the history table
            stmt = connection.prepareStatement("SELECT MAX(id) FROM subsribers_history;");
            rs = stmt.executeQuery();
            rs.next();
            int historyId = rs.getInt(1);

            // insert an empty history to the new row
            List<String[]> emptyHistory = new ArrayList<>();
            byte[] blob = BlobUtil.convertListToBlob(emptyHistory);
            stmt = connection.prepareStatement("UPDATE subsribers_history SET history_file = ? WHERE id = ?;");
            stmt.setBytes(1, blob);
            stmt.setInt(2, historyId);
            stmt.executeUpdate();

            //create a new row in the subscriber table
            stmt = connection.prepareStatement("INSERT INTO subscriber (subscriber_id, subscriber_first_name, " +
                    "subscriber_last_name, detailed_subscription_history,subscriber_phone_number, subscriber_email, " +
                    "is_active, subscriber_password) VALUES (?,?, ?, ?, ?, ?, ?, ?);");

            stmt.setString(1, data.get(0));
            stmt.setString(2, data.get(1));
            stmt.setString(3, data.get(2));
            stmt.setInt(4, historyId);
            stmt.setString(5, data.get(3));
            stmt.setString(6, data.get(4));
            stmt.setBoolean(7, true);
            stmt.setString(8, data.get(5));

            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("Error! subscriber sign up failed");
            return false;
        }
        catch (IOException e)
        {
            System.out.println("Error! cant create empty history file");
            return false;
        }

        return true;
    }

    /**
     * The method run SQL query to search for a book by name
     *
     * @param name - the name of the book to search
     * @return - list of books with the name
     */
    public List<Book> handleBookSearchByName(String name)
    {
        List<Book> books = new ArrayList<>();
        PreparedStatement stmt;

        try
        {
            // Prepare the SQL query with the MATCH and AGAINST functions
            stmt = connection.prepareStatement("SELECT * FROM book WHERE book_title = ?;");
            stmt.setString(1, name);

            ResultSet rs = stmt.executeQuery();

            books = getBooksFromResultSet(rs);

        }
        catch (SQLException e)
        {
            System.out.println("Error! book search by name failed");
        }

        return books;
    }

    /**
     * The method run SQL query to search for a book by category
     *
     * @param category - the category of the book to search
     * @return - list of books with the category
     */
    public List<Book> handleBookSearchByCategory(String category)
    {
        List<Book> books = new ArrayList<>();
        PreparedStatement stmt;

        try
        {
            // Prepare the SQL query with the MATCH and AGAINST functions
            stmt = connection.prepareStatement("SELECT * FROM book WHERE book_subject = ?;");
            stmt.setString(1, category);

            ResultSet rs = stmt.executeQuery();

            books = getBooksFromResultSet(rs);

        }
        catch (SQLException e)
        {
            System.out.println("Error! book search by category failed");
        }

        return books;
    }

    /**
     * The method run SQL query to search for a book by free text
     *
     * @param text - the text to search
     * @return - list of books that related to the text
     */
    public List<Book> handleBookSearchByFreeText(String text)
    {
        List<Book> books = new ArrayList<>();
        PreparedStatement stmt;

        try
        {
            // Prepare the SQL query with the MATCH and AGAINST functions
            stmt = connection.prepareStatement("SELECT * FROM book WHERE MATCH(description) AGAINST(? IN NATURAL LANGUAGE MODE);");
            stmt.setString(1, text);

            ResultSet rs = stmt.executeQuery();

            books = getBooksFromResultSet(rs);
        }
        catch (SQLException e)
        {
            System.out.println("Error! book search by free text failed");
        }

        return books;
    }

    /**
     * The method run SQL query to get the books from the result set
     *
     * @param rs - the result set
     * @return - list of books
     */
    private List<Book> getBooksFromResultSet(ResultSet rs) throws SQLException
    {
        List<Book> books = new ArrayList<>();

        // Iterate through the result set and create Book objects
        while (rs.next())
        {
            Book book = new Book(
                    rs.getInt("book_id"),
                    rs.getString("book_title"),
                    rs.getString("book_author"),
                    rs.getInt("edition_number"),
                    rs.getDate("print_date"),
                    rs.getString("book_subject"),
                    rs.getString("description")
            );

            // get the image of the book
            Blob blob = rs.getBlob("book_cover");
            int blobLength = (int) blob.length();
            byte[] blobAsBytes = blob.getBytes(1, blobLength);
            book.setImage(blobAsBytes);

            // add the book to the list
            books.add(book);
        }

        return books;
    }

    /**
     * The method run SQL query to get the subscriber history from the db
     *
     * @param subscriber_id - the id of the subscriber
     * @return - the subscriber history as List<String[]>
     */
    public List<String[]> handleReturnSubscriberHistory(String subscriber_id)
    {
        PreparedStatement stmt;
        List<String[]> history = null;

        try
        {
            stmt = connection.prepareStatement("SELECT detailed_subscription_history FROM subscriber WHERE subscriber_id = ?;");
            stmt.setString(1, subscriber_id);

            ResultSet rs = stmt.executeQuery();
            rs.next();
            int historyId = rs.getInt(1);

            stmt = connection.prepareStatement("SELECT history_file FROM subsribers_history WHERE id = ?;");
            stmt.setInt(1, historyId);
            rs = stmt.executeQuery();
            rs.next();

            byte[] blob = rs.getBytes(1);

            history = BlobUtil.convertBlobToList(blob);
        }
        catch (SQLException e)
        {
            System.out.println("Error! return subscriber history failed");
        }

        return history;
    }

    /**
     * The method run SQL query to update the subscriber details
     *
     * @param subscriber - the new details of the subscriber
     * @return - true if the update succeeds, else false
     */
    public boolean handleUpdateSubscriberDetails(Subscriber subscriber)
    {
        PreparedStatement stmt;

        try
        {
            stmt = connection.prepareStatement("UPDATE subscriber SET subscriber_phone_number = ?, subscriber_email = ? WHERE subscriber_id = ?;");

            stmt.setString(1, subscriber.getPhone());
            stmt.setString(2, subscriber.getEmail());
            stmt.setString(3, subscriber.getId());

            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("Error! update subscriber details failed");
            return false;
        }

        return true;
    }

    /**
     * The method run SQL query to update the subscriber password
     *
     * @param data - the subscriber id and new password
     * @return - true if the update succeeds, else false
     */
    public boolean handleUpdateSubscriberPassword(List<String> data)
    {
        PreparedStatement stmt;

        try
        {
            stmt = connection.prepareStatement("UPDATE subscriber SET subscriber_password = ? WHERE subscriber_id = ?;");

            stmt.setString(1, data.get(1));
            stmt.setString(2, data.get(0));

            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("Error! update subscriber password failed");
            return false;
        }

        return true;
    }

    /**
     * The method run SQL query to get all the subscribers in the db
     *
     * @return - list of all the subscribers
     */
    public List<Subscriber> handleGetAllSubscribers()
    {
        List<Subscriber> subscribers = new ArrayList<>();
        PreparedStatement stmt;

        try
        {
            // Prepare the SQL query with the MATCH and AGAINST functions
            stmt = connection.prepareStatement("SELECT * from subscriber;");

            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                //create new subscriber
                Subscriber subscriber = new Subscriber(rs.getString("subscriber_id"),
                        rs.getString("subscriber_first_name"), rs.getString("subscriber_last_name"), rs.getString("subscriber_phone_number"),
                        rs.getString("subscriber_email"), rs.getBoolean("is_active"));

                //add the subscriber to the list
                subscribers.add(subscriber);
            }

        }
        catch (SQLException e)
        {
            System.out.println("Error! get all subscribers failed");
        }

        return subscribers;
    }

    /**
     * The method run SQL query to check if a copy of the book is available to borrow (not borrowed and not ordered)
     *
     * @param copyId - the id of the copy to check
     * @return - true if the copy is available, else false
     */
    public List<String> handleCheckBorrowedBookAvailability(String copyId)
    {
        PreparedStatement stmt;
        boolean returnValue = true;
        String bookId = "";
        ResultSet rs;


        // check if the copy exists
        try
        {
            stmt = connection.prepareStatement("SELECT * from copy_of_the_book where copy_id = ? AND is_available <> 2;");
            stmt.setString(1, copyId);
            rs = stmt.executeQuery();

            if (!rs.next())
            {
                List<String> returnList = new ArrayList<>();
                returnList.add("false");
                return returnList;
            }

        }
        catch (SQLException e)
        {
            System.out.println("Error! check borrowed book availability failed - The book not found");
        }


        // check if the book is not borrowed
        try
        {
            stmt = connection.prepareStatement("SELECT * from copy_of_the_book WHERE copy_id = ?;");
            stmt.setString(1, copyId);

            rs = stmt.executeQuery();
            if (rs.next())
            {
                // save the book id for the next query
                bookId = rs.getString("book_id");

                if (rs.getInt("is_available") == 0)
                {
                    returnValue = false;
                }
            }
            else
            {
                // the copy not found
                returnValue = false;
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! check borrowed book availability failed - cant check if the book is borrowed");
        }

        if (returnValue)
        {
            // check that we have enough copies of the book that not ordered
            try
            {
                // count the number of copies of the book in the library that available to borrow
                stmt = connection.prepareStatement("SELECT COUNT(*) from copy_of_the_book WHERE book_id = ? AND is_available = 1;");
                stmt.setString(1, bookId);

                rs = stmt.executeQuery();
                rs.next();
                int copyAvailableToBorrow = rs.getInt(1);

                // count the number of copies of the book that are ordered
                stmt = connection.prepareStatement("SELECT COUNT(*) from subscriber_order WHERE book_id = ? AND is_active = true;");
                stmt.setString(1, bookId);

                rs = stmt.executeQuery();
                rs.next();
                int ordered = rs.getInt(1);

                // check if we have enough copies of the book that are not ordered
                if (copyAvailableToBorrow - ordered < 1)
                {
                    returnValue = false;
                }

            }
            catch (SQLException e)
            {
                System.out.println("Error! check borrowed book availability failed - cant check if the book is ordered");
            }
        }

        List<String> returnList = new ArrayList<>();
        returnList.add(String.valueOf(returnValue));

        // Add all the subscribers that have active orders for the book and got notified
        try
        {
            stmt = connection.prepareStatement("SELECT subscriber_id FROM subscriber_order WHERE book_id = ? AND is_active = true AND is_his_turn = true;");
            stmt.setString(1, bookId);
            rs = stmt.executeQuery();
            while (rs.next())
            {
                returnList.add(rs.getString("subscriber_id"));
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! check borrowed book availability failed - cant get the subscribers that have active orders for the book");
        }

        return returnList;
    }

    /**
     * The method run SQL query to handle the borrow of a book
     *
     * @param borrowDetails - the details of the borrow
     *                      [0] the subscriber id
     *                      [1] the copy id
     *                      [2] the due date
     * @return - 0 if the borrow succeeds, 1 if the subscriber not found,
     * 2 if the subscriber is frozen, 3 if the borrow failed due to db error
     */
    public Boolean handleBorrowBook(List<String> borrowDetails)
    {
        PreparedStatement stmt;
        String subscriberId = borrowDetails.get(0);
        String copyId = borrowDetails.get(1);
        String dueDate = borrowDetails.get(2);
        ResultSet rs;

        try
        {
            // update the copy of the book to be borrowed
            stmt = connection.prepareStatement("UPDATE copy_of_the_book SET is_available = 0 WHERE copy_id = ?;");
            stmt.setString(1, copyId);
            stmt.executeUpdate();

            // get the last borrow id
            stmt = connection.prepareStatement("SELECT MAX(borrow_id) FROM borrow_book;");
            rs = stmt.executeQuery();
            rs.next();
            int borrowId = rs.getInt(1) + 1;

            // create a new row in the borrow table
            stmt = connection.prepareStatement("INSERT INTO borrow_book (borrow_id, subscriber_id, copy_id, borrow_date, borrow_due_date, is_active) VALUES (?,?, ?, CURDATE(), ?, true);");
            stmt.setString(1, String.valueOf(borrowId));
            stmt.setString(2, subscriberId);
            stmt.setString(3, copyId);
            stmt.setString(4, dueDate);
            stmt.executeUpdate();

            //check if a matching book order exists
            stmt = connection.prepareStatement("SELECT order_id FROM subscriber_order WHERE subscriber_id= ? AND is_his_turn = 1 AND book_id=(SELECT book_id from copy_of_the_book WHERE copy_id = ?);");
            stmt.setString(1, subscriberId);
            stmt.setString(2, copyId);
            rs = stmt.executeQuery();

            //if a matching order exists - change the order's is_active to 0
            if (rs.next())
            {
                // save the order id for the next query
                String orderId = rs.getString("order_id");
                stmt = connection.prepareStatement("UPDATE subscriber_order SET is_active =0 WHERE order_id = ?;");
                stmt.setString(1, orderId);
                stmt.executeUpdate();
            }

            // get the max scheduler id
            stmt = connection.prepareStatement("SELECT MAX(scheduler_id) FROM scheduler_triggers;");
            rs = stmt.executeQuery();
            rs.next();

            // add trigger to send reminder email to the subscriber day before the return date
            stmt = connection.prepareStatement("INSERT INTO scheduler_triggers (scheduler_id, trigger_date, trigger_operation, relevant_id, is_triggered) VALUES (?, ?, ?, ?,0);");
            stmt.setString(1, String.valueOf(rs.getInt(1) + 1));
            stmt.setString(2, LocalDate.parse(dueDate).minusDays(1).toString());
            stmt.setString(3, "notifyDayBeforeReturnDate");
            stmt.setString(4, String.valueOf(borrowId));
            stmt.executeUpdate();

        }
        catch (SQLException e)
        {
            System.out.println("Error! borrow book failed - cant add the borrow to the db");
            return false;
        }

        // the borrow succeeded
        return true;
    }

    /**
     * The method run SQL query to handle book order by the subscriber
     *
     * @param orderDetails - [0] the subscriber id
     *                     [1] the book id
     * @return - List<Boolean> [0] true if the order succeeds, else false
     * [1] true if the subscriber is frozen, else false
     */
    public List<Boolean> handleOrderBook(List<String> orderDetails)
    {
        List<Boolean> returnValue = new ArrayList<>();
        boolean validFlag = true;
        PreparedStatement stmt;

        // check if the subscriber account is frozen
        try
        {
            stmt = connection.prepareStatement("SELECT * from subscriber WHERE subscriber_id = ?;");
            stmt.setString(1, orderDetails.get(0));

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                if (!rs.getBoolean("is_active"))
                {
                    returnValue.add(false);
                    returnValue.add(true);
                    return returnValue;
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! order book failed - cant check if the subscriber is frozen");
            returnValue.add(false);
            returnValue.add(false);
            validFlag = false;
        }

        if (validFlag)
        {
            // check that the subscriber not already ordered the book
            try
            {
                stmt = connection.prepareStatement("SELECT * from subscriber_order WHERE subscriber_id = ? AND book_id = ? AND is_active = true;");
                stmt.setString(1, orderDetails.get(0));
                stmt.setString(2, orderDetails.get(1));

                ResultSet rs = stmt.executeQuery();
                if (rs.next())
                {
                    returnValue.add(false);
                    returnValue.add(false);

                    validFlag = false;
                }
            }
            catch (SQLException e)
            {
                System.out.println("Error! order book failed - cant check if the subscriber already ordered the book");
                returnValue.add(false);
                returnValue.add(false);
                validFlag = false;
            }
        }

        if (validFlag)
        {
            // check that we not have more orders than copies of the book in the library
            try
            {
                // count the number of copies of the book in the library
                stmt = connection.prepareStatement("SELECT COUNT(*) from copy_of_the_book WHERE book_id = ? AND is_available <> 2");
                stmt.setString(1, orderDetails.get(1));

                ResultSet rs = stmt.executeQuery();
                rs.next();
                int copyAvailableInLibrary = rs.getInt(1);

                // count the number of copies of the book that are ordered
                stmt = connection.prepareStatement("SELECT COUNT(*) from subscriber_order WHERE book_id = ? AND is_active = true;");
                stmt.setString(1, orderDetails.get(1));

                rs = stmt.executeQuery();
                rs.next();
                int copyOrdered = rs.getInt(1);

                // check if we have enough copies of the book that are not copyOrdered
                if (copyAvailableInLibrary - copyOrdered < 1)
                {
                    returnValue.add(false);
                    returnValue.add(false);

                    validFlag = false;
                }

            }
            catch (SQLException e)
            {
                System.out.println("Error! order book failed - cant check if we have enough copies of the book that are not ordered");
                returnValue.add(false);
                returnValue.add(false);
                validFlag = false;
            }
        }

        if (validFlag)
        {
            // add the order to the db to the subscriber_order table
            try
            {
                // get the last order id
                stmt = connection.prepareStatement("SELECT MAX(order_id) FROM subscriber_order;");
                ResultSet rs = stmt.executeQuery();
                rs.next();
                int orderId = rs.getInt(1) + 1;

                // create a new row in the subscriber_order table
                stmt = connection.prepareStatement("INSERT INTO subscriber_order (order_id, subscriber_id, book_id, order_date, is_active) VALUES (?,?, ?, NOW(), true);");
                stmt.setString(1, String.valueOf(orderId));
                stmt.setString(2, orderDetails.get(0));
                stmt.setString(3, orderDetails.get(1));
                stmt.executeUpdate();

                returnValue.add(true);
                returnValue.add(false);
            }
            catch (SQLException e)
            {
                System.out.println("Error! order book failed - cant add the order to the db");
                returnValue.add(false);
                returnValue.add(false);
            }

        }

        return returnValue;
    }

    /**
     * The method run SQL query to handle the return of a borrowed book
     *
     * @param borrowId - the id of the borrow
     * @return - List<Boolean> [0] true if the return succeeds, else false
     * [1] true if the subscriber is frozen, else false
     */
    public List<Boolean> handleReturnBorrowedBook(String borrowId)
    {
        List<Boolean> returnValue = new ArrayList<>();
        boolean validFlag = true;
        PreparedStatement stmt;
        String copyId;

        // check if the borrow is active
        try
        {
            stmt = connection.prepareStatement("SELECT * from borrow_book WHERE borrow_id = ?;");
            stmt.setString(1, borrowId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                if (!rs.getBoolean("is_active"))
                {
                    returnValue.add(false);
                    returnValue.add(false);
                    validFlag = false;
                }
            }
            else
            {
                // the borrow not found
                returnValue.add(false);
                returnValue.add(false);
                validFlag = false;
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! return borrowed book failed - cant check if the borrow is active");
            returnValue.add(false);
            returnValue.add(false);
            validFlag = false;
        }

        if (validFlag)
        {
            // return the borrowed book, update the copy of the book to be available + update the borrow to be inactive
            try
            {
                // get the copy id of the borrow
                stmt = connection.prepareStatement("SELECT copy_id from borrow_book WHERE borrow_id = ?;");
                stmt.setString(1, borrowId);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                copyId = rs.getString(1);

                // update the copy of the book to be available
                stmt = connection.prepareStatement("UPDATE copy_of_the_book SET is_available = 1 WHERE copy_id = ?;");
                stmt.setString(1, copyId);
                stmt.executeUpdate();

                // update the borrow to be inactive
                stmt = connection.prepareStatement("UPDATE borrow_book SET is_active = false, borrow_return_date = CURDATE() WHERE borrow_id = ?;");
                stmt.setString(1, borrowId);
                stmt.executeUpdate();

                // check the due date of the borrow
                stmt = connection.prepareStatement("SELECT borrow_due_date from borrow_book WHERE borrow_id = ?;");
                stmt.setString(1, borrowId);
                rs = stmt.executeQuery();
                rs.next();
                Date dueDate = rs.getDate(1);

                // check if the borrow is late by a week or more
                if (LocalDate.now().isAfter(dueDate.toLocalDate().plusWeeks(1)))
                {
                    // get the subscriber id
                    stmt = connection.prepareStatement("SELECT subscriber_id from borrow_book WHERE borrow_id = ?;");
                    stmt.setString(1, borrowId);
                    rs = stmt.executeQuery();
                    rs.next();
                    String subscriberId = rs.getString(1);

                    // update the subscriber status to be frozen
                    stmt = connection.prepareStatement("UPDATE subscriber SET is_active = false WHERE subscriber_id = ?;");
                    stmt.setString(1, subscriberId);
                    stmt.executeUpdate();

                    // get the current id of the scheduler
                    stmt = connection.prepareStatement("SELECT MAX(scheduler_id) FROM scheduler_triggers;");
                    rs = stmt.executeQuery();
                    rs.next();
                    int schedulerId = rs.getInt(1) + 1;

                    // add trigger to the table
                    stmt = connection.prepareStatement("INSERT INTO scheduler_triggers (scheduler_id, trigger_date, trigger_operation, relevant_id) VALUES (?,?, ?, ?);");
                    stmt.setString(1, String.valueOf(schedulerId));
                    stmt.setDate(2, Date.valueOf(LocalDate.now().plusMonths(1)));
                    stmt.setString(3, "unfrozen");
                    stmt.setString(4, subscriberId);
                    stmt.executeUpdate();

                    returnValue.add(true);
                    returnValue.add(true);
                }
                else
                {
                    // borrow return succeeded
                    returnValue.add(true);
                    returnValue.add(false);
                }

                // update the scheduler trigger to be triggered (no need to send reminder email)
                stmt = connection.prepareStatement("SELECT scheduler_id FROM scheduler_triggers WHERE trigger_operation = 'notifyDayBeforeReturnDate' AND relevant_id = ? AND is_triggered = 0;");
                stmt.setString(1, borrowId);
                rs = stmt.executeQuery();
                rs.next();

                stmt = connection.prepareStatement("UPDATE scheduler_triggers SET is_triggered = 1 WHERE scheduler_id = ?;");
                stmt.setInt(1, rs.getInt(1));
                stmt.executeUpdate();

            }
            catch (SQLException e)
            {
                System.out.println("Error! return borrowed book failed - cant update the borrow in the db");
                returnValue.add(false);
                returnValue.add(false);
            }
        }

        // return successful, check if there are orders for the book and send notification
        if (returnValue.get(0))
        {
            try
            {
                // get the book id from the borrow
                stmt = connection.prepareStatement("SELECT book_id from copy_of_the_book WHERE copy_id = (SELECT copy_id from borrow_book WHERE borrow_id = ?);");
                stmt.setString(1, borrowId);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                String bookId = rs.getString(1);

                handleBookOrderWhenCopyAvailable(bookId);

            }
            catch (SQLException e)
            {
                System.out.println("Error! return borrowed book failed - cant check if there are orders for the book + send email");
            }
        }


        return returnValue;
    }

    /**
     * The method run SQL query
     * to handle sending email to the subscriber that has ordered a book and when the book is available
     *
     * @param bookId - the id of the book that is available
     */
    public void handleBookOrderWhenCopyAvailable(String bookId)
    {
        PreparedStatement stmt;
        ResultSet rs;

        try
        {
            // check if there are orders for the book and get the oldest order
            stmt = connection.prepareStatement("SELECT * from subscriber_order WHERE book_id = ? AND is_active = true AND is_his_turn = false ORDER BY order_date LIMIT 1;");
            stmt.setString(1, bookId);
            rs = stmt.executeQuery();

            // check if we have a subscriber that waits for the book
            if (rs.next())
            {
                // update order to have his turn on
                stmt = connection.prepareStatement("UPDATE subscriber_order SET is_his_turn = true WHERE order_id = ?;");
                stmt.setString(1, rs.getString("order_id"));
                stmt.executeUpdate();

                // get the subscriber email
                stmt = connection.prepareStatement("SELECT subscriber_first_name,subscriber_email from subscriber WHERE subscriber_id = ?;");
                stmt.setString(1, rs.getString("subscriber_id"));
                ResultSet rs2 = stmt.executeQuery();
                rs2.next();
                String subscriberFirstName = rs2.getString(1);
                String subscriberEmail = rs2.getString(2);

                // get the book name
                stmt = connection.prepareStatement("SELECT book_title from book WHERE book_id = ?;");
                stmt.setString(1, bookId);
                rs2 = stmt.executeQuery();
                rs2.next();

                // send email to the subscriber
                String message = "Dear " + subscriberFirstName + ",\n\n your order for the book " + rs2.getString("book_title") + " with book id \"" + bookId + "\" has arrived.\n\n Please visit the library to collect it.";
                Notification_Controller.getInstance().sendEmail(subscriberEmail, "The book you ordered is now available for you to borrow", message);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! send email to the subscriber that has ordered a book and when the book is available failed");
        }
    }


    /**
     * The method run SQL query to get the subscriber active borrow list
     *
     * @param subscriberId - the id of the subscriber
     * @return - list of the active borrows
     */
    public List<List<String>> handleGetSubscriberBorrowList(String subscriberId)
    {
        List<List<String>> borrowList = new ArrayList<>();
        PreparedStatement stmt;

        try
        {
            // Prepare the SQL query with the MATCH and AGAINST functions
            stmt = connection.prepareStatement("SELECT BB.borrow_id, BB.copy_id, B.book_title, BB.borrow_date, BB.borrow_due_date\n" +
                    "FROM book B, copy_of_the_book C, borrow_book BB\n" +
                    "WHERE B.book_id = C.book_id AND C.copy_id = BB.copy_id AND BB.is_active = true AND BB.subscriber_id = ?;");
            stmt.setString(1, subscriberId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                // save the borrow details in a list of row
                List<String> row = new ArrayList<>();
                row.add(rs.getString("borrow_id"));
                row.add(rs.getString("copy_id"));
                row.add(rs.getString("book_title"));
                row.add(rs.getString("borrow_date"));
                row.add(rs.getString("borrow_due_date"));

                // add the borrow to the return list
                borrowList.add(row);
            }

        }
        catch (SQLException e)
        {
            System.out.println("Error! get subscriber borrow list failed");
        }

        return borrowList;
    }

    /**
     * The method run SQL query to extend the borrow period of a book by the subscriber
     *
     * @param extensionRequest - [0] the borrow id
     *                         [1] the new due date
     * @return - true if the extent succeeds, else false
     */
    public boolean handleSubscriberExtendBorrow(List<String> extensionRequest)
    {
        PreparedStatement stmt;
        String borrowId = extensionRequest.get(0);
        Date newDueDate = Date.valueOf(extensionRequest.get(1));
        boolean returnValue = true;

        // check if the borrow is active
        try
        {
            stmt = connection.prepareStatement("SELECT * from borrow_book WHERE borrow_id = ?;");
            stmt.setString(1, borrowId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                if (!rs.getBoolean("is_active"))
                {
                    returnValue = false;
                }
            }
            else
            {
                // the borrow not found
                returnValue = false;
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! extend borrow failed - cant check if the borrow is active");
            returnValue = false;
        }

        // check if there is order for that book
        try
        {
            // get the book id
            stmt = connection.prepareStatement("SELECT book_id from copy_of_the_book WHERE copy_id = (SELECT copy_id FROM borrow_book WHERE borrow_id = ?);");
            stmt.setString(1, borrowId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                String bookId = rs.getString(1);

                // check if there is an order for the book that waits for the return of the book
                stmt = connection.prepareStatement("SELECT * from subscriber_order WHERE book_id = ? AND is_active = true AND is_his_turn = false;");
                stmt.setString(1, bookId);

                rs = stmt.executeQuery();
                if (rs.next())
                {
                    // we cant extend the borrow
                    returnValue = false;
                }
            }
            else
            {
                // the book not found
                returnValue = false;
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! extend borrow failed - cant check if there is an order for the book");
            returnValue = false;
        }

        if (returnValue)
        {
            // we can extend the borrow - update the borrow due date to the new due date and add to extend history
            try
            {
                // get the last extend id
                stmt = connection.prepareStatement("SELECT MAX(extension_id) FROM extension_book;");
                ResultSet rs = stmt.executeQuery();
                rs.next();
                int extendId = rs.getInt(1) + 1;

                // get the current due date
                stmt = connection.prepareStatement("SELECT borrow_due_date from borrow_book WHERE borrow_id = ?;");
                stmt.setString(1, borrowId);
                rs = stmt.executeQuery();
                rs.next();
                Date currentDueDate = rs.getDate(1);

                // create a new row in the extension_book table
                stmt = connection.prepareStatement("INSERT INTO extension_book (extension_id, borrow_id, original_due_date, new_due_date, extension_type) VALUES (?,?, ?, ?, false);");
                stmt.setString(1, String.valueOf(extendId));
                stmt.setString(2, borrowId);
                stmt.setDate(3, currentDueDate);
                stmt.setDate(4, newDueDate);
                stmt.executeUpdate();

                // update the borrow due date
                stmt = connection.prepareStatement("UPDATE borrow_book SET borrow_due_date = ? WHERE borrow_id = ?;");
                stmt.setDate(1, newDueDate);
                stmt.setString(2, borrowId);
                stmt.executeUpdate();

                // update the scheduler to send reminder email to the subscriber day before the new return date
                stmt = connection.prepareStatement("SELECT scheduler_id FROM scheduler_triggers WHERE trigger_operation = 'notifyDayBeforeReturnDate' AND relevant_id = ? AND is_triggered = 0;");
                stmt.setString(1, borrowId);
                rs = stmt.executeQuery();
                rs.next();

                stmt = connection.prepareStatement("UPDATE scheduler_triggers SET trigger_date = ? WHERE scheduler_id = ?;");
                stmt.setDate(1, Date.valueOf(newDueDate.toLocalDate().minusDays(1)));
                stmt.setInt(2, rs.getInt(1));
                stmt.executeUpdate();
            }
            catch (SQLException e)
            {
                System.out.println("Error! extend borrow failed - cant update the borrow in the db");
                returnValue = false;
            }
        }

        return returnValue;
    }

    /**
     * The method run SQL query to check if the subscriber exists in the db
     *
     * @param subscriberId - the id of the subscriber to check
     * @return - true if the subscriber exists, else false
     */
    public int handleCheckSubscriberStatus(String subscriberId)
    {
        PreparedStatement stmt;
        int returnValue = 0;

        // check if the subscriber in the DB
        try
        {
            stmt = connection.prepareStatement("SELECT * from subscriber WHERE subscriber_id = ?;");
            stmt.setString(1, subscriberId);

            ResultSet rs = stmt.executeQuery();
            if (!rs.next())
            {
                returnValue = 1;
            }
            else
            {
                // we have the subscriber, check if he is frozen
                if (!rs.getBoolean("is_active"))
                {
                    returnValue = 2;
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! check subscriber existence failed");
            returnValue = 3;
        }


        return returnValue;
    }


    /**
     * The method run SQL query to extend the borrow period of a book by the librarian
     *
     * @param extensionRequest - [0] the borrow id
     *                         [1] the new due date
     *                         [2] the librarian id
     * @return - true if the extension succeeds, else false
     */
    public boolean handleLibrarianExtendBorrow(List<String> extensionRequest)
    {
        PreparedStatement stmt;
        String borrowId = extensionRequest.get(0);
        Date newDueDate = Date.valueOf(extensionRequest.get(1));
        String librarianId = extensionRequest.get(2); // librarian ID passed in the request
        boolean returnValue = true;

        // check if the borrow is active
        try
        {
            stmt = connection.prepareStatement("SELECT * FROM borrow_book WHERE borrow_id = ?;");
            stmt.setString(1, borrowId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                if (!rs.getBoolean("is_active"))
                {
                    returnValue = false;
                }
            }
            else
            {
                // the borrow not found
                returnValue = false;
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! extend borrow failed - can't check if the borrow is active");
            returnValue = false;
        }

        // check if there is an order for that book
        try
        {
            // get the book id
            stmt = connection.prepareStatement("SELECT book_id FROM copy_of_the_book WHERE copy_id = (SELECT copy_id FROM borrow_book WHERE borrow_id = ?);");
            stmt.setString(1, borrowId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                String bookId = rs.getString(1);

                // check if there is an order for the book that waits for the return of the book
                stmt = connection.prepareStatement("SELECT * FROM subscriber_order WHERE book_id = ? AND is_active = true AND is_his_turn = false;");
                stmt.setString(1, bookId);

                rs = stmt.executeQuery();
                if (rs.next())
                {
                    // we can't extend the borrow
                    returnValue = false;
                }
            }
            else
            {
                // the book not found
                returnValue = false;
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! extend borrow failed - can't check if there is an order for the book");
            returnValue = false;
        }

        if (returnValue)
        {
            // we can extend the borrow - update the borrow due date to the new due date and add to extend history
            try
            {
                // get the last extension id
                stmt = connection.prepareStatement("SELECT MAX(extension_id) FROM extension_book;");
                ResultSet rs = stmt.executeQuery();
                rs.next();
                int extendId = rs.getInt(1) + 1;

                // get the current due date
                stmt = connection.prepareStatement("SELECT borrow_due_date FROM borrow_book WHERE borrow_id = ?;");
                stmt.setString(1, borrowId);
                rs = stmt.executeQuery();
                rs.next();
                Date currentDueDate = rs.getDate(1);

                // create a new row in the extension_book table with librarian details
                stmt = connection.prepareStatement(
                        "INSERT INTO extension_book (extension_id, borrow_id, original_due_date, new_due_date, extension_type, extension_operator, extension_date) " +
                                "VALUES (?, ?, ?, ?, true, ?, CURRENT_DATE);"
                );
                stmt.setString(1, String.valueOf(extendId));
                stmt.setString(2, borrowId);
                stmt.setDate(3, currentDueDate);
                stmt.setDate(4, newDueDate);
                stmt.setString(5, librarianId);
                stmt.executeUpdate();

                // update the borrow due date
                stmt = connection.prepareStatement("UPDATE borrow_book SET borrow_due_date = ? WHERE borrow_id = ?;");
                stmt.setDate(1, newDueDate);
                stmt.setString(2, borrowId);
                stmt.executeUpdate();

                // update the scheduler to send reminder email to the subscriber day before the new return date
                stmt = connection.prepareStatement("SELECT scheduler_id FROM scheduler_triggers WHERE trigger_operation = 'notifyDayBeforeReturnDate' AND relevant_id = ? AND is_triggered = 0;");
                stmt.setString(1, borrowId);
                rs = stmt.executeQuery();
                rs.next();

                stmt = connection.prepareStatement("UPDATE scheduler_triggers SET trigger_date = ? WHERE scheduler_id = ?;");
                stmt.setDate(1, Date.valueOf(newDueDate.toLocalDate().minusDays(1)));
                stmt.setInt(2, rs.getInt(1));
                stmt.executeUpdate();
            }
            catch (SQLException e)
            {
                System.out.println("Error! extend borrow failed - can't update the borrow in the db");
                returnValue = false;
            }
        }

        return returnValue;
    }

    /**
     * The method run SQL query to handle update book copy to lost status
     *
     * @param copyId - the id of the copy
     * @return - true if the update succeeds, else false
     */
    public boolean handleUpdateBookCopyToLost(String copyId)
    {
        PreparedStatement stmt;
        boolean returnValue = true;

        // check if the copy exists
        try
        {
            stmt = connection.prepareStatement("SELECT * from copy_of_the_book WHERE copy_id = ?;");
            stmt.setString(1, copyId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                if (rs.getInt("is_available") == 2)
                {
                    returnValue = false;
                }
            }
            else
            {
                // the copy not found
                returnValue = false;
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! update book copy to lost failed - cant check if the copy is available");
            returnValue = false;
        }

        if (returnValue)
        {
            // update the copy to be lost
            try
            {
                stmt = connection.prepareStatement("UPDATE copy_of_the_book SET is_available = 2 WHERE copy_id = ?;");
                stmt.setString(1, copyId);
                stmt.executeUpdate();
            }
            catch (SQLException e)
            {
                System.out.println("Error! update book copy to lost failed - cant update the copy in the db");
                returnValue = false;
            }
        }

        return returnValue;
    }

    /**
     * The method run SQL query to get the five newest books in the library
     *
     * @return - list of the five newest books
     */
    public List<Book> handleGetFiveNewestBooks()
    {
        List<Book> books = new ArrayList<>();
        PreparedStatement stmt;


        try
        {
            // Prepare the SQL query with the MATCH and AGAINST functions
            stmt = connection.prepareStatement("SELECT DISTINCT B.book_id,\n" +
                    "                B.book_title,\n" +
                    "                B.book_author,\n" +
                    "                B.edition_number,\n" +
                    "                B.print_date,\n" +
                    "                B.book_subject,\n" +
                    "                B.description,\n" +
                    "                B.book_cover,\n" +
                    "                C.latest_purchase_date\n" +
                    "FROM book B\n" +
                    "         JOIN (SELECT book_id, MAX(purchase_date) AS latest_purchase_date\n" +
                    "               FROM copy_of_the_book\n" +
                    "               WHERE is_available <> 2\n" +
                    "               GROUP BY book_id) C ON B.book_id = C.book_id\n" +
                    "ORDER BY C.latest_purchase_date DESC\n" +
                    "LIMIT 5;");

            ResultSet rs = stmt.executeQuery();

            books = getBooksFromResultSet(rs);

        }
        catch (SQLException e)
        {
            System.out.println("Error! get five newest books failed");
        }

        return books;
    }

    /**
     * The method run SQL query to get the five most popular books in the library
     *
     * @return - list of the five most popular books
     */
    public List<Book> handleGetFiveMostPopularBooks()
    {
        List<Book> books = new ArrayList<>();
        PreparedStatement stmt;

        try
        {
            // Prepare the SQL query with the MATCH and AGAINST functions
            stmt = connection.prepareStatement("SELECT B.book_id, B.book_title, B.book_author, B.edition_number, B.print_date, B.book_subject, B.description, B.book_cover, COUNT(BB.borrow_id) AS borrow_count\n" +
                    "FROM book B\n" +
                    "         JOIN copy_of_the_book C ON B.book_id = C.book_id\n" +
                    "         JOIN borrow_book BB ON C.copy_id = BB.copy_id\n" +
                    "WHERE C.is_available <> 2\n" +
                    "GROUP BY B.book_id, B.book_title, B.edition_number, B.print_date, B.book_subject, B.description, B.book_cover\n" +
                    "ORDER BY borrow_count DESC\n" +
                    "LIMIT 5;");

            ResultSet rs = stmt.executeQuery();

            books = getBooksFromResultSet(rs);
        }
        catch (SQLException e)
        {
            System.out.println("Error! get five popular books failed");
        }

        return books;
    }

    /**
     * The method run SQL query to get all the borrow details that the return date is tomorrow
     *
     * @return - list of the borrow details
     *              [0] - the subscriber id
     *              [1] - the subscriber name
     *              [2] - the subscriber email
     *              [3] - the book title
     */
    public List<List<String>> handleGetReminderDayBeforeDetails()
    {
        PreparedStatement stmt;
        ResultSet rs;
        List<List<String>> reminderDetails = new ArrayList<>();

        try
        {
            // get all the borrow ids that the return date is tomorrow (from the scheduler_triggers table)
            stmt = connection.prepareStatement("SELECT scheduler_id,relevant_id FROM scheduler_triggers " +
                    "WHERE trigger_operation = 'notifyDayBeforeReturnDate' AND trigger_date <= NOW() AND is_triggered = 0;");
            rs = stmt.executeQuery();

            String borrowId;
            while (rs.next())
            {
                borrowId = rs.getString("relevant_id");

                // get the subscriber name and email + the book title
                stmt = connection.prepareStatement("SELECT S.subscriber_id ,S.subscriber_first_name, S.subscriber_email, B.book_title\n" +
                        "FROM subscriber S, book B, copy_of_the_book C, borrow_book BB\n" +
                        "WHERE S.subscriber_id = BB.subscriber_id AND C.copy_id = BB.copy_id AND B.book_id = C.book_id AND BB.borrow_id = ?;");
                stmt.setString(1, borrowId);

                ResultSet rs2 = stmt.executeQuery();
                rs2.next();

                List<String> reminderRow = new ArrayList<>();
                reminderRow.add(rs2.getString("subscriber_id"));
                reminderRow.add(rs2.getString("subscriber_first_name"));
                reminderRow.add(rs2.getString("subscriber_email"));
                reminderRow.add(rs2.getString("book_title"));

                reminderDetails.add(reminderRow);

                // update the scheduler to be triggered
                stmt = connection.prepareStatement("UPDATE scheduler_triggers SET is_triggered = 1 WHERE scheduler_id = ?;");
                stmt.setString(1, rs.getString("scheduler_id"));
                stmt.executeUpdate();
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! notify day before return date failed");
        }

        return reminderDetails;
    }


    /**
     * Handles unfulfilled orders by retrieving books, updating the subscriber status,
     * and returning a map of books to the last subscriber waiting for each book.
     */
    public void handleDeleteUnfulfilledOrder()
    {
        PreparedStatement selectBookStmt;
        PreparedStatement updateStmt;
        ResultSet bookResultSet;

        try
        {
            // Step 1: Select book IDs related to the unfulfilled orders
            String selectBookQuery = "SELECT st.scheduler_id, o.order_id, o.book_id\n" +
                    "FROM scheduler_triggers st\n" +
                    "         JOIN subscriber_order o ON st.relevant_id = o.order_id\n" +
                    "WHERE st.is_triggered = 0\n" +
                    "  AND st.trigger_date <= NOW()\n" +
                    "  AND st.trigger_operation = 'order';";

            selectBookStmt = connection.prepareStatement(selectBookQuery);
            bookResultSet = selectBookStmt.executeQuery();

            List<String> bookIds = new ArrayList<>();
            List<String> orderIds = new ArrayList<>();
            List<String> schedulerIds = new ArrayList<>();
            while (bookResultSet.next())
            {
                String bookId = bookResultSet.getString("book_id");
                bookIds.add(bookId);

                String orderId = bookResultSet.getString("order_id");
                orderIds.add(orderId);

                String schedulerId = bookResultSet.getString("scheduler_id");
                schedulerIds.add(schedulerId);
            }

            // Step 2: Update `is_active` status for the relevant subscribers
            String updateQuery = "UPDATE subscriber_order SET is_active = 2 WHERE order_id = ?;";

            updateStmt = connection.prepareStatement(updateQuery);

            for (String orderId : orderIds)
            {
                updateStmt.setString(1, orderId);
                updateStmt.executeUpdate();
            }

            // Step 3: update the scheduler_triggers table to mark the trigger as triggered
            String updateTriggerQuery = "UPDATE scheduler_triggers SET is_triggered = 1 WHERE scheduler_id = ?";
            updateStmt = connection.prepareStatement(updateTriggerQuery);

            for (String schedulerId : schedulerIds)
            {
                updateStmt.setString(1, schedulerId);
                updateStmt.executeUpdate();
            }


            // Step 4: send notification to the last subscriber waiting for each book
            for (String book : bookIds)
            {
                handleBookOrderWhenCopyAvailable(book);
            }

        }
        catch (SQLException e)
        {
            System.out.println("Error! delete unfulfilled order failed");
        }
    }


    /**
     * Updates subscribers who have been frozen exactly for one month.
     * <p>
     * This method performs the following steps:
     * 1. Find all subscribers in the `scheduler_triggers` table where the trigger operation is 'freeze' and the trigger date is exactly 30 days ago.
     * 2. Updates the `is_active` status in the `subscriber` table to 1 for these subscribers.
     * 3. Prints to the console which subscribers were unfrozen.
     */
    public void handleOneMonthFromFreezeDate()
    {
        PreparedStatement selectStmt;
        PreparedStatement updateSubscriberStmt;
        ResultSet rs;

        try
        {
            // Step 1: Select frozen subscribers from `scheduler_triggers`
            // where the unfrozen date today or past today and not triggered before
            String selectQuery = "SELECT scheduler_id, relevant_id from scheduler_triggers WHERE is_triggered = 0 AND trigger_date <= NOW() AND trigger_operation = 'unfrozen';";
            selectStmt = connection.prepareStatement(selectQuery);
            rs = selectStmt.executeQuery();

            List<String> frozenSubscriberIds = new ArrayList<>();
            List<String> schedulerIds = new ArrayList<>();
            while (rs.next())
            {
                String subscriberId = rs.getString("relevant_id");
                frozenSubscriberIds.add(subscriberId);

                String schedulerId = rs.getString("scheduler_id");
                schedulerIds.add(schedulerId);
            }

            // Step 2: Update `subscriber` table to set `is_active = 1` for frozen subscribers
            if (!frozenSubscriberIds.isEmpty())
            {
                String updateSubscriberQuery = "UPDATE subscriber\n" +
                        "SET is_active = 1\n" +
                        "WHERE subscriber_id = ?";
                updateSubscriberStmt = connection.prepareStatement(updateSubscriberQuery);

                for (String subscriberId : frozenSubscriberIds)
                {
                    updateSubscriberStmt.setString(1, subscriberId);
                    updateSubscriberStmt.executeUpdate();
                }
            }

            // Step 3: Update the `scheduler_triggers` table to mark the trigger as triggered
            String updateTriggerQuery = "UPDATE scheduler_triggers SET is_triggered = 1 WHERE scheduler_id = ?;";
            updateSubscriberStmt = connection.prepareStatement(updateTriggerQuery);

            for (String schedulerId : schedulerIds)
            {
                updateSubscriberStmt.setString(1, schedulerId);
                updateSubscriberStmt.executeUpdate();
            }

        }
        catch (SQLException e)
        {
            System.out.println("Error! one month from freeze date failed");
        }
    }

    /**
     * The method run SQL query to check if we need to update the daily report and create trigger for tomorrow
     *
     * @return - true if we need to update the daily report, else false
     */
    public boolean handleGetTriggerOfDailyReportUpdate()
    {
        PreparedStatement stmt;
        ResultSet rs;
        try
        {
            stmt = connection.prepareStatement("SELECT scheduler_id FROM scheduler_triggers WHERE trigger_operation = 'daily report' AND is_triggered = 0 AND trigger_date < NOW();");
            rs = stmt.executeQuery();
            if (rs.next())
            {
                // update the trigger to be triggered
                stmt = connection.prepareStatement("UPDATE scheduler_triggers SET is_triggered = 1 WHERE scheduler_id = ?;");
                stmt.setString(1, rs.getString("scheduler_id"));
                stmt.executeUpdate();

                // get the max scheduler id
                stmt = connection.prepareStatement("SELECT MAX(scheduler_id) FROM scheduler_triggers;");
                rs = stmt.executeQuery();
                rs.next();
                int schedulerId = rs.getInt(1) + 1;

                // create trigger for tomorrow
                stmt = connection.prepareStatement("INSERT INTO scheduler_triggers (scheduler_id, trigger_date, trigger_operation, relevant_id,is_triggered) VALUES (?,?,?, 0, 0);");
                stmt.setString(1, String.valueOf(schedulerId));
                stmt.setDate(2, Date.valueOf(LocalDate.now().plusDays(1)));
                stmt.setString(3, "daily report");

                stmt.executeUpdate();

                return true;
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! get trigger of daily report update failed");
        }
        return false;
    }

    /**
     * The method run SQL query to check if we need to update the monthly report and create trigger for next month
     *
     * @return - true if we need to update the monthly report, else false
     */
    public boolean handleGetTriggerOfMonthlyReportUpdate()
    {
        PreparedStatement stmt;
        ResultSet rs;
        try
        {
            stmt = connection.prepareStatement("SELECT scheduler_id FROM scheduler_triggers WHERE trigger_operation = 'monthly report' AND is_triggered = 0 AND trigger_date < NOW();");
            rs = stmt.executeQuery();
            if (rs.next())
            {
                // update the trigger to be triggered
                stmt = connection.prepareStatement("UPDATE scheduler_triggers SET is_triggered = 1 WHERE scheduler_id = ?;");
                stmt.setString(1, rs.getString("scheduler_id"));
                stmt.executeUpdate();

                // get the max scheduler id
                stmt = connection.prepareStatement("SELECT MAX(scheduler_id) FROM scheduler_triggers;");
                rs = stmt.executeQuery();
                rs.next();
                int schedulerId = rs.getInt(1) + 1;

                // create trigger for the start of next month
                stmt = connection.prepareStatement("INSERT INTO scheduler_triggers (scheduler_id, trigger_date, trigger_operation, relevant_id,is_triggered) VALUES (?,?,?, 0, 0);");
                stmt.setString(1, String.valueOf(schedulerId));
                stmt.setDate(2, Date.valueOf(LocalDate.now().plusMonths(1).withDayOfMonth(1)));
                stmt.setString(3, "monthly report");

                stmt.executeUpdate();

                return true;
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! get trigger of daily report update failed");
        }
        return false;
    }


    /**
     * Fetches the subscriber's name and the book title for their order.*
     *
     * @param subscriberID The ID of the subscriber.
     * @return A map containing the subscriber's name and the book title.
     */
    public Map<String, String> fetchOrderDetails(String subscriberID)
    {
        Map<String, String> details = new HashMap<>();
        PreparedStatement stmt;
        ResultSet rs;

        try
        {
            String query = "SELECT s.subscriber_first_name, s.subscriber_last_name, b.book_title\n" +
                    "FROM subscriber_order so\n" +
                    "         JOIN subscriber s ON so.subscriber_id = s.subscriber_id\n" +
                    "         JOIN book b ON so.book_id = b.book_id\n" +
                    "WHERE so.subscriber_id = ?\n" +
                    "  AND so.is_active = 1";

            stmt = connection.prepareStatement(query);
            stmt.setString(1, subscriberID);

            rs = stmt.executeQuery();
            if (rs.next())
            {
                details.put("subscriberName", rs.getString("subscriber_first_name") + " " + rs.getString("subscriber_last_name"));
                details.put("bookTitle", rs.getString("book_title"));
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! fetch order details failed");
        }

        return details;
    }


    /**
     * The method run SQL query to get the history of a subscriber by his id
     *
     * @param history_id - the id of the history
     * @return - the history of the subscriber in byte array
     */
    public List<String[]> HandleGetHistoryFileById(String history_id)
    {
        PreparedStatement stmt;
        ResultSet rs;
        byte[] blob = null;
        try
        {
            String query = "SELECT history_file FROM subsribers_history WHERE id = ?";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, history_id);
            rs = stmt.executeQuery();
            if (rs.next())
            {
                blob = rs.getBytes("history_file");
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! get history file failed");
        }

        return BlobUtil.convertBlobToList(blob);
    }

    /**
     * The method run SQL query to get the history of a subscriber by his id
     *
     * @param subscriberId - the id of the subscriber
     * @return - the history of the subscriber in byte array
     */
    public List<String[]> getHistoryFileBySubscriberId(String subscriberId)
    {
        List<String[]> historyList;

        String historyId = getHistoryIdBySubscriberId(subscriberId);
        historyList = HandleGetHistoryFileById(historyId);

        return historyList;
    }

    /**
     * The method run SQL query to update the history of a subscriber by his id
     *
     * @param subscriberId - the id of the subscriber
     * @param list         - the new history file
     */
    public void handleUpdateHistoryFileBySubscriberId(String subscriberId, List<String[]> list)
    {
        PreparedStatement stmt;

        try
        {
            byte[] blob = BlobUtil.convertListToBlob(list);

            String query = "UPDATE subsribers_history SET history_file = ? WHERE id = ?";
            stmt = connection.prepareStatement(query);
            stmt.setBytes(1, blob);
            stmt.setString(2, getHistoryIdBySubscriberId(subscriberId));
            stmt.executeUpdate();
        }
        catch (IOException e)
        {
            System.out.println("Error! update history file by subscriber id failed - cant convert list to blob");
        }
        catch (SQLException e)
        {
            System.out.println("Error! update history file by subscriber id failed");
        }
    }

    /**
     * The method run SQL query to get the history id by the subscriber id
     *
     * @param subscriberId - the id of the subscriber
     * @return - the history id
     */
    private String getHistoryIdBySubscriberId(String subscriberId)
    {
        PreparedStatement stmt;
        ResultSet rs;
        String historyId = null;
        try
        {
            String query = "SELECT detailed_subscription_history FROM subscriber WHERE subscriber_id = ?";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, subscriberId);
            rs = stmt.executeQuery();
            if (rs.next())
            {
                historyId = rs.getString("detailed_subscription_history");
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! get history id by subscriber id failed");
        }
        return historyId;
    }

    /**
     * The method run SQL query to get the book by book id
     *
     * @param bookId - the id of the book to get
     * @return - Book object with the book details
     */
    public Book getBookByBookId(String bookId)
    {
        PreparedStatement stmt;
        ResultSet rs;
        Book book = null;

        try
        {
            String query = "SELECT * FROM book WHERE book_id = ?";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, bookId);
            rs = stmt.executeQuery();
            if (rs.next())
            {
                book = new Book(rs.getInt("book_id"), rs.getString("book_title"), rs.getString("book_author"),
                        rs.getInt("edition_number"), rs.getDate("print_date"), rs.getString("book_subject"),
                        rs.getString("description"));
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! get book by book id failed");
        }

        return book;
    }

    /**
     * The method run SQL query to get the book by copy id
     *
     * @param copyId - the id of the copy to get the book
     * @return - Book object with the book details
     */
    public Book getBookByCopyId(String copyId)
    {
        PreparedStatement stmt;
        ResultSet rs;
        Book book = null;

        try
        {
            String query = "SELECT book_id FROM copy_of_the_book WHERE copy_id = ?";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, copyId);
            rs = stmt.executeQuery();
            if (rs.next())
            {
                book = getBookByBookId(rs.getString("book_id"));
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! get book by copy id failed");
        }

        return book;
    }

    /**
     * The method run SQL query to get the subscriber id by the borrow id
     *
     * @param borrowId - the id of the borrow
     * @return - the subscriber id
     */
    public String getSubscriberIdFromBorrowId(String borrowId)
    {
        PreparedStatement stmt;
        ResultSet rs;
        String subscriberId = null;

        try
        {
            String query = "SELECT subscriber_id FROM borrow_book WHERE borrow_id = ?";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, borrowId);
            rs = stmt.executeQuery();
            if (rs.next())
            {
                subscriberId = rs.getString("subscriber_id");
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! get subscriber id from borrow id failed");
        }

        return subscriberId;
    }

    /**
     * The method run SQL query to get the subscriber by subscriber id
     *
     * @param subscriberId - the id of the subscriber
     * @return - Subscriber object with the subscriber details
     */
    public Subscriber getSubscriberBySubscriberId(String subscriberId)
    {
        PreparedStatement stmt;
        ResultSet rs;
        Subscriber subscriber = null;

        try
        {
            String query = "SELECT * FROM subscriber WHERE subscriber_id = ?";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, subscriberId);
            rs = stmt.executeQuery();
            if (rs.next())
            {
                subscriber = new Subscriber(rs.getString("subscriber_id"), rs.getString("subscriber_first_name"),
                        rs.getString("subscriber_last_name"), rs.getString("subscriber_phone_number"),
                        rs.getString("subscriber_email"), rs.getBoolean("is_active"));
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! get subscriber by subscriber id failed");
        }

        return subscriber;
    }

    /**
     * The method run SQL query to get the book object by the borrow id
     *
     * @param borrowId - the id of the borrow
     * @return - Book object with the book details
     */
    public Book getBookDetailsByBorrowId(String borrowId)
    {
        PreparedStatement stmt;
        ResultSet rs;
        Book book = null;

        try
        {
            String query = "SELECT B.book_id, B.book_title, B.book_author, B.edition_number, B.print_date, B.book_subject, B.description\n" +
                    "FROM book B\n" +
                    "         JOIN copy_of_the_book C ON B.book_id = C.book_id\n" +
                    "         JOIN borrow_book BB ON C.copy_id = BB.copy_id\n" +
                    "WHERE BB.borrow_id = ?;";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, borrowId);
            rs = stmt.executeQuery();
            if (rs.next())
            {
                book = new Book(rs.getInt("book_id"), rs.getString("book_title"), rs.getString("book_author"),
                        rs.getInt("edition_number"), rs.getDate("print_date"), rs.getString("book_subject"),
                        rs.getString("description"));
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! get book details by borrow id failed");
        }

        return book;
    }

    /**
     * The method run SQL query to get the book location by the book id
     *
     * @param bookId - the id of the book
     * @return - The location in the library / the date the book will be available
     */
    public List<String> handleGetBookLocation(String bookId)
    {
        List<String> returnValue = new ArrayList<>();
        PreparedStatement stmt;
        boolean inLibrary = true;
        int ordered;

        // check if there is a copy of the book available in the library
        try
        {
            // count the number of copies of the book in the library that available to borrow
            stmt = connection.prepareStatement("SELECT COUNT(*) from copy_of_the_book WHERE book_id = ? AND is_available = 1;");
            stmt.setString(1, bookId);

            ResultSet rs = stmt.executeQuery();
            rs.next();
            int copyAvailableToBorrow = rs.getInt(1);

            // count the number of copies of the book that are ordered
            stmt = connection.prepareStatement("SELECT COUNT(*) from subscriber_order WHERE book_id = ? AND is_active = true;");
            stmt.setString(1, bookId);

            rs = stmt.executeQuery();
            rs.next();
            ordered = rs.getInt(1);

            // check if we have enough copies of the book that are not ordered
            if (copyAvailableToBorrow - ordered < 1)
            {
                returnValue.add("Date");
                inLibrary = false;
            }
            else
            {
                returnValue.add("Library");
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! check borrowed book availability failed - cant check if the book is ordered");
            return null;
        }

        if (inLibrary)
        {
            // find the location of the book in the library
            try
            {
                stmt = connection.prepareStatement("SELECT location_on_shelf FROM copy_of_the_book WHERE book_id = ? AND is_available = 1;");
                stmt.setString(1, bookId);
                stmt.executeQuery();

                ResultSet rs = stmt.executeQuery();
                rs.next();
                returnValue.add(rs.getString(1));
            }
            catch (SQLException e)
            {
                System.out.println("Error! get book location failed - cant get the book location");

            }
        }
        else
        {
            // check how much copies of the book exists
            try
            {
                stmt = connection.prepareStatement("SELECT COUNT(*) from copy_of_the_book WHERE book_id = ? AND is_available <> 2;");
                stmt.setString(1, bookId);

                ResultSet rs = stmt.executeQuery();
                rs.next();
                int copies = rs.getInt(1);

                if (copies == ordered)
                {
                    // we need to check the closest return date and add 2 weeks
                    stmt = connection.prepareStatement("SELECT MIN(borrow_due_date) from borrow_book WHERE is_active = 1 AND copy_id IN (SELECT copy_id from copy_of_the_book WHERE book_id = ? AND is_available <> 2);");
                    stmt.setString(1, bookId);
                    stmt.executeQuery();

                    rs = stmt.executeQuery();
                    rs.next();
                    Date expectedReturnDate = rs.getDate(1);
                    LocalDate newDate = expectedReturnDate.toLocalDate().plusWeeks(2).plusDays(1);
                    returnValue.add(newDate.toString());
                }
                else
                {
                    // get all the active borrow due date ordered by the due date
                    stmt = connection.prepareStatement("SELECT borrow_due_date from borrow_book WHERE is_active = 1 AND copy_id IN (SELECT copy_id from copy_of_the_book WHERE book_id = ? AND is_available <> 2) ORDER BY borrow_due_date LIMIT 1;");
                    stmt.setString(1, bookId);
                    stmt.executeQuery();

                    rs = stmt.executeQuery();
                    rs.next();

                    Date expectedReturnDate = rs.getDate(1);
                    returnValue.add((expectedReturnDate.toLocalDate().plusDays(1)).toString());

                }
            }
            catch (SQLException e)
            {
                System.out.println("Error! get book location failed - cant get the book location");
            }
        }

        return returnValue;
    }

    /**
     * The method run SQL query to check if the book is available for order
     *
     * @param bookId - the id of the book
     * @return - true if the book is available for order, false otherwise
     */
    public boolean handleCheckIfBookIsAvailableForOrder(String bookId)
    {
        PreparedStatement stmt;
        ResultSet rs;
        boolean returnValue = false;
        int ordered = 0;

        // check if we can add order (max the number of copies of the book)
        try
        {
            // count the number of copies of the book in the library
            stmt = connection.prepareStatement("SELECT COUNT(*) from copy_of_the_book WHERE book_id = ? AND is_available <> 2;");
            stmt.setString(1, bookId);
            rs = stmt.executeQuery();

            if (rs.next())
            {
                int copies = rs.getInt(1);

                stmt = connection.prepareStatement("SELECT COUNT(*) from subscriber_order WHERE book_id = ? AND is_active = true;");
                stmt.setString(1, bookId);
                rs = stmt.executeQuery();

                if (rs.next())
                {
                    ordered = rs.getInt(1);

                    // there is no copy available to borrow, we can order the book
                    returnValue = copies - ordered > 0;
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! check borrowed book availability failed - cant check if the book is ordered");
            return false;
        }


        if (returnValue)
        {
            try
            {
                // check if there is a copy that is available to borrow -> we cant make an order
                stmt = connection.prepareStatement("SELECT COUNT(*) from copy_of_the_book WHERE book_id = ? AND is_available = 1;");
                stmt.setString(1, bookId);
                rs = stmt.executeQuery();

                if (rs.next())
                {
                    int copies = rs.getInt(1);

                    if (copies - ordered > 0)
                    {
                        returnValue = false;
                    }
                }

            }
            catch (SQLException e)
            {
                System.out.println("Error! check borrowed book availability failed - cant check if the book is ordered");
                return false;
            }

        }

        return returnValue;
    }


    /**
     * The method run SQL query to get the active subscribers count
     *
     * @return - the number of active subscribers
     */
    public int fetchActiveSubscribersCount()
    {
        String query = "SELECT COUNT(*) FROM subscriber WHERE is_active = 1";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery())
        {
            if (rs.next())
            {
                return rs.getInt(1);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! fetch active subscribers count failed");
        }
        return 0;
    }

    /**
     * The method run SQL query to get the frozen subscribers count
     *
     * @return - the number of frozen subscribers
     */
    public int fetchFrozenSubscribersCount()
    {
        String query = "SELECT COUNT(*) FROM subscriber WHERE is_active = 0";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery())
        {
            if (rs.next())
            {
                return rs.getInt(1);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! fetch frozen subscribers count failed");
        }
        return 0;
    }

    /**
     * Fetches the monthly subscriber status report for the current month.
     *
     * @return List<String [ ]> where each row contains frozen_count, active_count, and day.
     */
    public List<String[]> fetchMonthlySubscribersStatusReport()
    {
        try
        {
            // get the latest report (max report_id) for the same report type
            PreparedStatement stmt = connection.prepareStatement("SELECT MAX(report_id) FROM monthly_reports WHERE report_type = 'SubscribersStatus'");
            ResultSet rs = stmt.executeQuery();

            rs.next();
            int reportId = rs.getInt(1);

            String query = "SELECT report_file " +
                    "FROM monthly_reports " +
                    "WHERE report_id = ?;";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, reportId);
            rs = stmt.executeQuery();

            if (rs.next())
            {
                byte[] blobData = rs.getBytes("report_file");
                return BlobUtil.convertBlobToList(blobData); // Convert Blob to List<String[]>
            }
            else return new ArrayList<>();
        }
        catch (SQLException e)
        {
            System.out.println("Error! fetch monthly subscribers status report failed");
        }
        return null; // Return null if no data found or error occurred
    }

    /**
     * Updates the monthly subscribers' status report in the database with the given Blob data.
     *
     * @param updatedBlob The Blob data representing the updated report.
     */
    public void updateMonthlySubscribersStatusReport(byte[] updatedBlob)
    {
        try
        {
            // get the latest report (max report_id) for the same report type
            PreparedStatement stmt = connection.prepareStatement("SELECT MAX(report_id) FROM monthly_reports WHERE report_type = 'SubscribersStatus'");
            ResultSet rs = stmt.executeQuery();

            rs.next();
            int reportId = rs.getInt(1);


            String updateQuery = "UPDATE monthly_reports " +
                    "SET report_file = ? " +
                    "WHERE report_id = ?";
            stmt = connection.prepareStatement(updateQuery);
            stmt.setBytes(1, updatedBlob);
            stmt.setInt(2, reportId);
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Report updated successfully. Rows affected: " + rowsAffected);
        }
        catch (SQLException e)
        {
            System.out.println("Error! update monthly subscribers status report failed");
        }
    }

    /**
     * Fetches the monthly borrowing report for the current month - total borrow time.
     *
     * @return - Map<String, String> where the key is the book title and the value is the total borrow time.
     */
    public Map<String, String> fetchTotalBorrowTime()
    {
        String query = "SELECT b.book_title,\n" +
                "       SUM(\n" +
                "               CASE\n" +
                "                   WHEN bb.borrow_return_date IS NOT NULL AND MONTH(bb.borrow_date) <> MONTH(CURRENT_DATE)\n" +
                "                       AND MONTH(bb.borrow_return_date) = MONTH(CURRENT_DATE) THEN\n" +
                "                       DATEDIFF(bb.borrow_return_date, DATE_FORMAT(CURRENT_DATE, '%Y-%m-01')) + 1\n" +
                "                   WHEN bb.borrow_return_date IS NOT NULL AND MONTH(bb.borrow_date) = MONTH(CURRENT_DATE)\n" +
                "                       AND MONTH(bb.borrow_return_date) = MONTH(CURRENT_DATE) THEN\n" +
                "                       DATEDIFF(bb.borrow_return_date, bb.borrow_date) + 1\n" +
                "                   WHEN bb.borrow_return_date IS NULL AND MONTH(bb.borrow_date) = MONTH(CURRENT_DATE) THEN\n" +
                "                       DATEDIFF(CURRENT_DATE, bb.borrow_date)\n" +
                "                   WHEN bb.borrow_return_date IS NULL AND MONTH(bb.borrow_date) <> MONTH(CURRENT_DATE) THEN\n" +
                "                       DATEDIFF(CURRENT_DATE, DATE_FORMAT(CURRENT_DATE, '%Y-%m-01'))\n" +
                "                   ELSE 0\n" +
                "                   END\n" +
                "       ) AS total_borrow_time\n" +
                "FROM borrow_book bb\n" +
                "         JOIN\n" +
                "     copy_of_the_book cob ON bb.copy_id = cob.copy_id\n" +
                "         JOIN\n" +
                "     book b ON cob.book_id = b.book_id\n" +
                "WHERE (cob.is_available <> 2) AND (bb.borrow_return_date IS NULL\n" +
                "    OR MONTH(bb.borrow_date) = MONTH(CURRENT_DATE)\n" +
                "    OR MONTH(bb.borrow_return_date) = MONTH(CURRENT_DATE))\n" +
                "GROUP BY b.book_title;";
        Map<String, String> totalBorrowTimeMap = new HashMap<>();

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery())
        {

            while (rs.next())
            {
                String bookTitle = rs.getString("book_title");
                String totalBorrowTime = rs.getString("total_borrow_time");
                totalBorrowTimeMap.put(bookTitle, totalBorrowTime);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! fetch total borrow time failed");
        }
        return totalBorrowTimeMap;
    }

    /**
     * Fetches the monthly borrowing report for the current month - late borrow time.
     *
     * @return - Map<String, String> where the key is the book title and the value is the late borrow time.
     */
    public Map<String, String> fetchLateBorrowTime()
    {
        String query = "SELECT b.book_title,\n" +
                "       SUM(\n" +
                "               CASE\n" +
                "                   -- Case 1: Book borrowed in the previous month and returned late in the current month\n" +
                "                   WHEN bb.borrow_return_date IS NOT NULL AND MONTH(bb.borrow_date) <> MONTH(CURRENT_DATE)\n" +
                "                       AND MONTH(bb.borrow_return_date) = MONTH(CURRENT_DATE)\n" +
                "                       AND bb.borrow_return_date > bb.borrow_due_date THEN\n" +
                "                       DATEDIFF(bb.borrow_return_date, DATE_FORMAT(CURRENT_DATE, '%Y-%m-01')) + 1\n" +
                "\n" +
                "                   -- Case 2: Book borrowed in the previous month and still not returned but late in the current month\n" +
                "                   WHEN bb.borrow_return_date IS NULL AND MONTH(bb.borrow_date) <> MONTH(CURRENT_DATE)\n" +
                "                       AND CURRENT_DATE > bb.borrow_due_date THEN\n" +
                "                       DATEDIFF(CURRENT_DATE, DATE_FORMAT(CURRENT_DATE, '%Y-%m-01'))\n" +
                "\n" +
                "                   -- Case 3: Book borrowed in the current month and returned late in the current month\n" +
                "                   WHEN bb.borrow_return_date IS NOT NULL AND MONTH(bb.borrow_date) = MONTH(CURRENT_DATE)\n" +
                "                       AND bb.borrow_return_date > bb.borrow_due_date THEN\n" +
                "                       DATEDIFF(bb.borrow_return_date, bb.borrow_due_date) + 1\n" +
                "\n" +
                "                   -- Case 4: Book borrowed in the current month and still not returned, but late\n" +
                "                   WHEN bb.borrow_return_date IS NULL AND MONTH(bb.borrow_date) = MONTH(CURRENT_DATE)\n" +
                "                       AND CURRENT_DATE > bb.borrow_due_date THEN\n" +
                "                       DATEDIFF(CURRENT_DATE, bb.borrow_due_date)\n" +
                "\n" +
                "                   ELSE 0\n" +
                "                   END\n" +
                "       ) AS late_borrow_time\n" +
                "FROM borrow_book bb\n" +
                "         JOIN\n" +
                "     copy_of_the_book cob ON bb.copy_id = cob.copy_id\n" +
                "         JOIN\n" +
                "     book b ON cob.book_id = b.book_id\n" +
                "WHERE (cob.is_available <> 2) AND (bb.borrow_return_date IS NULL\n" +
                "   OR (MONTH(bb.borrow_date) = MONTH(CURRENT_DATE)\n" +
                "    OR MONTH(bb.borrow_return_date) = MONTH(CURRENT_DATE)))\n" +
                "GROUP BY b.book_title;";

        Map<String, String> lateBorrowTimeMap = new HashMap<>();

        try
        {
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                String bookTitle = rs.getString("book_title");
                String lateBorrowTime = rs.getString("late_borrow_time");
                lateBorrowTimeMap.put(bookTitle, lateBorrowTime);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! fetch late borrow time failed");
        }
        return lateBorrowTimeMap;
    }

    /**
     * Saves a monthly report to the monthly_reports table in the database.
     *
     * @param reportType The type of the report (e.g., BorrowingReport).
     * @param reportBlob The Blob data representing the report.
     */
    public void saveMonthlyReport(String reportType, byte[] reportBlob)
    {
        try
        {
            // get the latest report (max report_id) for the same report type
            PreparedStatement stmt = connection.prepareStatement("SELECT MAX(report_id) FROM monthly_reports WHERE report_type = ?");
            stmt.setString(1, reportType);
            ResultSet rs = stmt.executeQuery();

            rs.next();
            int reportId = rs.getInt(1);

            // update the report with the new data
            stmt = connection.prepareStatement("UPDATE monthly_reports SET report_file = ?, is_ready = 1 WHERE report_id = ?");
            stmt.setBytes(1, reportBlob);
            stmt.setInt(2, reportId);
            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("Error! save monthly report failed");
        }
    }

    /**
     * Inserts an empty report into the monthly_reports table for the next month.
     * Each new report gets a unique report_id automatically.
     *
     * @param data- [0] the report type
     *              [1] the next month
     *              [2] the year
     */
    public void insertEmptyMonthlyReport(List<String> data)
    {
        String query = "INSERT INTO monthly_reports (report_id, report_type, report_month, report_year, report_file, is_ready) " +
                "VALUES (?,?, ?, ? , '', 0);";

        try
        {
            PreparedStatement stmt = connection.prepareStatement("SELECT MAX(report_id) FROM monthly_reports;");
            ResultSet rs = stmt.executeQuery();
            int notificationId;
            if (rs.next())
                notificationId = rs.getInt(1) + 1;
            else notificationId = 1;

            stmt = connection.prepareStatement(query);
            stmt.setString(1, "" + notificationId);
            stmt.setString(2, data.get(0));
            stmt.setInt(3, Integer.parseInt(data.get(1)));
            stmt.setInt(4, Integer.parseInt(data.get(2)));
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0)
            {
                System.out.println("Empty report created for the next month. Rows affected: " + rowsAffected);
            }
            else
            {
                System.out.println("No report was inserted.");
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! insert empty monthly report failed");
        }
    }

    /**
     * The method run SQL query to update the subscriber report from last month to ready
     */
    public void handleUpdateSubscriberReportToReady()
    {
        PreparedStatement stmt;
        ResultSet rs;

        try
        {
            // get the latest report (max report_id) for the same report type
            stmt = connection.prepareStatement("SELECT MAX(report_id) FROM monthly_reports WHERE report_type = 'SubscribersStatus'");
            rs = stmt.executeQuery();
            rs.next();

            // update the report to ready
            stmt = connection.prepareStatement("UPDATE monthly_reports SET is_ready = 1 WHERE report_id = ?");
            stmt.setInt(1, rs.getInt(1));
            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("Error! update subscriber report to ready failed");
        }
    }

    /**
     * Fetches the report ID for a given type, month, and year.
     *
     * @param data- [0] the report type
     *              [1] the month
     *              [2] the year
     * @return The report ID, or -1 if not found.
     */
    public int fetchReportId(List<String> data)
    {
        String query = "SELECT report_id " +
                "FROM monthly_reports " +
                "WHERE report_type = ? " +
                "AND report_month = ? " +
                "AND report_year = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.setString(1, data.get(0));
            stmt.setInt(2, Integer.parseInt(data.get(1)));
            stmt.setInt(3, Integer.parseInt(data.get(2)));

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                return rs.getInt("report_id");
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! fetch report ID failed");
        }
        return -1; // Return -1 if no report is found
    }

    /**
     * Fetches the Blob data for a specific report from the database.
     *
     * @param data - [0] the report type
     *             [1] the month
     *             [2] the year
     * @return The Blob data as a byte array, or null if not found.
     */
    public byte[] fetchReportBlob(List<String> data)
    {
        String query = "SELECT report_file " +
                "FROM monthly_reports " +
                "WHERE report_type = ? " +
                "AND report_month = ? " +
                "AND report_year = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.setString(1, data.get(0));
            stmt.setInt(2, Integer.parseInt(data.get(1)));
            stmt.setInt(3, Integer.parseInt(data.get(2)));

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                return rs.getBytes("report_file");
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! fetch report blob failed");
        }
        return null; // Return null if no report is found or an error occurs
    }

    /**
     * The method run SQL query to get all the librarian unread messages
     *
     * @return - list of all the unread messages
     */
    public List<List<String>> handleGetUnreadLibrarianMessages()
    {
        PreparedStatement stmt;
        List<List<String>> returnList = new ArrayList<>();

        try
        {
            stmt = connection.prepareStatement("SELECT * FROM notification WHERE is_confirmed = 0 AND notification_type = 'Librarian';");

            ResultSet rs = stmt.executeQuery();

            // add all the messages to the list
            while (rs.next())
            {
                List<String> message = new ArrayList<>();
                message.add(rs.getString("notification_id"));

                String dateTime = rs.getString("notification_date");
                // Utility method to format dates
                try
                {
                    LocalDateTime localDateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    dateTime = localDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                }
                catch (Exception e)
                {
                    // Do nothing
                }
                message.add(dateTime);

                message.add(rs.getString("notification_text"));
                returnList.add(message);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! get unread librarian messages failed");
        }

        return returnList;
    }

    /**
     * The method run SQL query to update the librarian message to read
     *
     * @param notificationId - the id of the message to update
     * @return - true if the update was successful, false otherwise
     */
    public boolean handleMarkLibrarianNotificationAsRead(String notificationId)
    {
        PreparedStatement stmt;

        try
        {
            stmt = connection.prepareStatement("UPDATE notification SET is_confirmed = 1 WHERE notification_id = ?;");
            stmt.setString(1, notificationId);
            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("Error! mark librarian notification as read failed");
            return false;
        }

        return true;
    }

    /**
     * The method run SQL query to get all the subscriber unread messages and then update them to read
     *
     * @param subscriberId - the id of the subscriber
     * @return - list of all the unread messages
     */
    public List<String> handleGetSubscriberMissedSms(String subscriberId)
    {
        PreparedStatement stmt;
        List<String> messages = new ArrayList<>();

        try
        {
            stmt = connection.prepareStatement("SELECT * FROM notification WHERE notification_type = 'Subscriber'  AND user_id = ? AND is_confirmed = 0;");
            stmt.setString(1, subscriberId);
            ResultSet rs = stmt.executeQuery();

            // add all the messages to the list
            while (rs.next())
            {
                messages.add(rs.getString("notification_text"));
            }

            // update the messages to read
            stmt = connection.prepareStatement("UPDATE notification SET is_confirmed = 1 WHERE notification_type = 'Subscriber' AND  user_id = ?;");
            stmt.setString(1, subscriberId);
            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("Error! get unread librarian messages failed");
        }

        return messages;
    }

    /**
     * The method run SQL query to save the subscriber missed sms in DB
     *
     * @param subscriberId - the id of the subscriber
     * @param message      - the message of the sms
     */
    public void handleSaveSubscriberMissedSms(String subscriberId, String message)
    {
        PreparedStatement stmt;

        try
        {
            // get the latest notification id
            stmt = connection.prepareStatement("SELECT MAX(notification_id) FROM notification;");
            ResultSet rs = stmt.executeQuery();
            rs.next();
            int notificationId = rs.getInt(1) + 1;


            stmt = connection.prepareStatement("INSERT INTO notification (notification_id, notification_type, user_id, notification_date, notification_text, is_confirmed) VALUES (? ,'Subscriber',?, NOW(), ?, 0);");
            stmt.setInt(1, notificationId);
            stmt.setString(2, subscriberId);
            stmt.setString(3, message);
            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("Error! save subscriber missed sms failed");
        }
    }

    /**
     * The method run SQL query to save the librarian message in DB
     *
     * @param message - the message of the librarian
     */
    public void handleSaveLibrarianMessage(String message)
    {
        PreparedStatement stmt;

        try
        {
            // get the latest notification id
            stmt = connection.prepareStatement("SELECT MAX(notification_id) FROM notification;");
            ResultSet rs = stmt.executeQuery();
            rs.next();
            int notificationId = rs.getInt(1) + 1;

            stmt = connection.prepareStatement("INSERT INTO notification (notification_id, notification_type, user_id, notification_date, notification_text, is_confirmed) VALUES (? ,'Librarian',0, NOW(), ?, 0);");
            stmt.setInt(1, notificationId);
            stmt.setString(2, message);
            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("Error! save librarian message failed");
        }
    }

    /**
     * The method run SQL query to get the subscriber name by his id
     *
     * @param subscriberId - the id of the subscriber to get his name
     * @return - the full name of the subscriber
     */
    public String GetSubscriberNameById(String subscriberId)
    {
        PreparedStatement stmt;
        ResultSet rs;
        String subscriberName = null;

        try
        {
            String query = "SELECT subscriber_first_name, subscriber_last_name FROM subscriber WHERE subscriber_id = ?";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, subscriberId);
            rs = stmt.executeQuery();
            if (rs.next())
            {
                subscriberName = rs.getString("subscriber_first_name") + " " + rs.getString("subscriber_last_name");
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! get subscriber name by id failed");
        }

        return subscriberName;
    }
}
