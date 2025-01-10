package logic;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entities.book.Book;
import entities.user.Librarian;
import entities.user.Subscriber;
import javafx.scene.image.Image;

public class dbController
{
    private static dbController instance = null;
    private Connection connection;

    /**
     * private constructor for the dbController
     */
    private dbController()
    {
        connect();
    }

    /**
     * get the instance of the dbController for singleton
     *
     * @return the instance of the dbController
     */
    public static dbController getInstance()
    {
        if (instance == null)
        {
            instance = new dbController();
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

            // get the id of the new row
            stmt = connection.prepareStatement("SELECT MAX(id) FROM subsribers_history;");
            rs = stmt.executeQuery();

            rs.next();
            int historyId = rs.getInt(1);

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
                    rs.getInt("edition_number"),
                    rs.getDate("print_date"),
                    rs.getString("book_subject"),
                    rs.getString("description")
            );

            // get the image of the book
            book.setImage(ImageUtil.convertBlobToImage(rs.getBlob("book_cover")));

            // add the book to the list
            books.add(book);
        }

        return books;
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
    public boolean handleCheckBorrowedBookAvailability(String copyId)
    {
        PreparedStatement stmt;
        boolean returnValue = true;
        String bookId = "";

        // check if the book is not borrowed
        try
        {
            stmt = connection.prepareStatement("SELECT * from copy_of_the_book WHERE copy_id = ?;");
            stmt.setString(1, copyId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                // save the book id for the next query
                bookId = rs.getString("book_id");

                if (rs.getBoolean("is_available"))
                {
                    returnValue = false;
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! check borrowed book availability failed");
        }

        if (returnValue)
        {
            // check that we have enough copies of the book that not ordered
            try
            {
                // count the number of copies of the book in the library that available to borrow
                stmt = connection.prepareStatement("SELECT COUNT(*) from copy_of_the_book WHERE book_id = ? AND is_available = true;");
                stmt.setString(1, bookId);

                ResultSet rs = stmt.executeQuery();
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
                System.out.println("Error! check borrowed book availability failed");
            }
        }

        return returnValue;
    }

    /**
     * The method run SQL query to handle the borrow of a book
     *
     * @param borrowDetails - the details of the borrow (subscriber id and copy id)
     * @return - true if the borrow succeeds, else false
     */
    public boolean handleBorrowBook(List<String> borrowDetails)
    {
        PreparedStatement stmt;
        String subscriberId = borrowDetails.get(0);
        String copyId = borrowDetails.get(1);

        // Check that the subscriber not frozen
        try
        {
            stmt = connection.prepareStatement("SELECT * from subscriber WHERE subscriber_id = ?;");
            stmt.setString(1, subscriberId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                if (!rs.getBoolean("is_active"))
                {
                    return false;
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("Error! borrow book failed");
            return false;
        }


        try
        {
            // update the copy of the book to be borrowed
            stmt = connection.prepareStatement("UPDATE copy_of_the_book SET is_available = true WHERE copy_id = ?;");
            stmt.setString(1, copyId);
            stmt.executeUpdate();


            // get the last borrow id
            stmt = connection.prepareStatement("SELECT MAX(borrow_id) FROM borrow_book;");
            ResultSet rs = stmt.executeQuery();
            rs.next();
            int borrowId = rs.getInt(1) + 1;

            // create a new row in the borrow table
            stmt = connection.prepareStatement("INSERT INTO borrow_book (borrow_id, subscriber_id, copy_id, borrow_date, borrow_due_date, is_active) VALUES (?,?, ?, CURDATE(), CURDATE() + 14, true);");
            stmt.setString(1, subscriberId);
            stmt.setString(2, copyId);
            stmt.setString(3, String.valueOf(borrowId));
            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("Error! borrow book failed");
            return false;
        }

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
            System.out.println("Error! order book failed");
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
                System.out.println("Error! order book failed");
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
                stmt = connection.prepareStatement("SELECT COUNT(*) from copy_of_the_book WHERE book_id = ?");
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
                System.out.println("Error! order book failed");
                returnValue.add(false);
                returnValue.add(false);
                validFlag = false;
            }
        }

        if (validFlag)
        {
            // check that all the copies of the book are borrowed
            try
            {
                // count the number of copies of the book in the library
                stmt = connection.prepareStatement("SELECT COUNT(*) from copy_of_the_book WHERE book_id = ? AND is_available = false;");
                stmt.setString(1, orderDetails.get(1));

                ResultSet rs = stmt.executeQuery();
                rs.next();
                int copyAvailableInLibrary = rs.getInt(1);

                // check if we have enough copies of the book that are not borrowed
                if (copyAvailableInLibrary < 1)
                {
                    returnValue.add(false);
                    returnValue.add(false);

                    validFlag = false;
                }

            }
            catch (SQLException e)
            {
                System.out.println("Error! order book failed");
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
                stmt = connection.prepareStatement("INSERT INTO subscriber_order (order_id, subscriber_id, book_id, order_date, is_active) VALUES (?,?, ?, CURDATE(), true);");
                stmt.setString(1, String.valueOf(orderId));
                stmt.setString(2, orderDetails.get(0));
                stmt.setString(3, orderDetails.get(1));
                stmt.executeUpdate();

                returnValue.add(true);
                returnValue.add(false);
            }
            catch (SQLException e)
            {
                System.out.println("Error! order book failed");
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
        }
        catch (SQLException e)
        {
            System.out.println("Error! return borrowed book failed");
            returnValue.add(false);
            returnValue.add(false);
            validFlag = false;
        }

        if (validFlag)
        {
            // return the borrowed book, update the copy of the book to be available + update the borrow to be inactive
            try
            {
                // update the copy of the book to be available
                stmt = connection.prepareStatement("UPDATE copy_of_the_book SET is_available = false WHERE copy_id = ?;");
                stmt.setString(1, borrowId);
                stmt.executeUpdate();

                // update the borrow to be inactive
                stmt = connection.prepareStatement("UPDATE borrow_book SET is_active = false WHERE borrow_id = ?;");
                stmt.setString(1, borrowId);
                stmt.executeUpdate();


                // check the due date of the borrow
                stmt = connection.prepareStatement("SELECT borrow_due_date from borrow_book WHERE borrow_id = ?;");
                stmt.setString(1, borrowId);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                LocalDate dueDate = rs.getDate(1).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                // check if the borrow is late by a week or more
                if (dueDate.isBefore(LocalDate.now().minusWeeks(1)))
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

                    //ToDo: send to scheduler to unfreeze the subscriber after a month

                    returnValue.add(true);
                    returnValue.add(true);
                }
                else
                {
                    // borrow return succeeded
                    returnValue.add(true);
                    returnValue.add(false);
                }
            }
            catch (SQLException e)
            {
                System.out.println("Error! return borrowed book failed");
                returnValue.add(false);
                returnValue.add(false);
            }
        }

        return returnValue;
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
            stmt = connection.prepareStatement("SELECT DISTINCT B.book_id, B.book_title, B.edition_number, B.print_date, B.book_subject, B.description, B.book_cover, C.purchase_date\n" +
                    "FROM book B\n" +
                    "         JOIN copy_of_the_book C ON B.book_id = C.book_id\n" +
                    "ORDER BY C.purchase_date DESC\n" +
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
            stmt = connection.prepareStatement("SELECT B.book_id, B.book_title, B.edition_number, B.print_date, B.book_subject, B.description, B.book_cover, COUNT(BB.borrow_id) AS borrow_count\n" +
                    "FROM book B\n" +
                    "         JOIN copy_of_the_book C ON B.book_id = C.book_id\n" +
                    "         JOIN borrow_book BB ON C.copy_id = BB.copy_id\n" +
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

    public List<Subscriber> handleNotifyDayBeforeReturnDate() {
        List<Subscriber> allSubscribers = handleGetAllSubscribers();
        List<Subscriber> dueSubscribers = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT subscriber_id FROM borrow_book WHERE borrow_return_date = DATE_ADD(CURRENT_DATE, INTERVAL 1 DAY)"
        )) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String subscriberId = rs.getString("subscriber_id");
                // Find and add the matching subscriber
                for (Subscriber subscriber : allSubscribers) {
                    if (subscriber.getId().equals(subscriberId)) {
                        dueSubscribers.add(subscriber);
                        break;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dueSubscribers;
    }
    /**
     * Updates the database to mark subscribers eligible to extend their borrow period.
     * This method performs the following:
     * 1. Queries the database to find all subscribers whose return date is exactly one week away.
     * 2. Updates the "can_extend" column in the database to TRUE for those subscribers.
     */
    //ToDO: maybe
//    public String handleOpenOptionToExtendBorrow() {
//        PreparedStatement selectStmt = null;
//        PreparedStatement updateStmt = null;
//
//        try {
//            // SQL query to find subscribers whose return date is a week from today
//            String selectQuery = "SELECT subscriber_id FROM borrow_book WHERE return_date = DATE_ADD(CURRENT_DATE, INTERVAL 7 DAY)";
//            selectStmt = connection.prepareStatement(selectQuery);
//
//            ResultSet rs = selectStmt.executeQuery();
//
//            // SQL query to update the "can_extend" field to TRUE
//            String updateQuery = "UPDATE borrow_book SET can_extend = TRUE WHERE subscriber_id = ?";
//            updateStmt = connection.prepareStatement(updateQuery);
//
//            while (rs.next()) {
//                String subscriberId = rs.getString("subscriber_id");
//
//                // Update the database for each eligible subscriber
//                updateStmt.setString(1, subscriberId);
//                updateStmt.executeUpdate();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                // Ensure resources are closed to prevent leaks
//                if (selectStmt != null) selectStmt.close();
//                if (updateStmt != null) updateStmt.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        return "Borrow extension options updated.";
//    }
    /**
     * Removes unfulfilled reservations older than two days from the waitlist in the database.
     */
    /**
     * Handles unfulfilled orders by retrieving books, updating the subscriber status,
     * and returning a map of books to the last subscriber waiting for each book.
     *
     * @return A map where the key is the book ID, and the value is the last subscriber waiting for the book.
     * @throws SQLException if a database access error occurs.
     */
    public Map<String, Subscriber> handleDeleteUnfulfilledOrder() {
        PreparedStatement selectBookStmt = null;
        PreparedStatement updateStmt = null;
        PreparedStatement getLastWaitingSubscriberStmt = null;
        ResultSet bookResultSet = null;
        Map<String, Subscriber> bookToSubscriberMap = new HashMap<>();

        try {
            // Step 1: Select book IDs related to the unfulfilled orders
            String selectBookQuery = "SELECT DISTINCT so.book_id\n" +
                    "FROM subscriber_order so\n" +
                    "         JOIN subscriber s ON so.subscriber_id = s.subscriber_id\n" +
                    "         JOIN scheduler_triggers t ON t.relevent_id = s.subscriber_id\n" +
                    "WHERE t.trigger_date = CURRENT_DATE\n" +
                    "  AND t.trigger_operation = 'order'\n" +
                    "  AND so.is_active = 1;";

            selectBookStmt = connection.prepareStatement(selectBookQuery);
            bookResultSet = selectBookStmt.executeQuery();

            List<String> bookIds = new ArrayList<>();
            while (bookResultSet.next()) {
                String bookId = bookResultSet.getString("book_id");
                bookIds.add(bookId);
            }
            System.out.println("Books related to unfulfilled orders: " + bookIds);

            // Step 2: Update `is_active` status for the relevant subscribers
            String updateQuery = "UPDATE subscriber_order\n" +
                    "            SET is_active = 0\n" +
                    "            WHERE subscriber_id IN\n" +
                    "                (SELECT s.subscriber_id\n" +
                    "               FROM scheduler_triggers t\n" +
                    "               JOIN subscriber s ON t.relevent_id = s.subscriber_id\n" +
                    "               WHERE t.trigger_date = CURRENT_DATE\n" +
                    "                  AND t.trigger_operation = 'order'\n" +
                    "                 AND s.is_active = 1);";

            updateStmt = connection.prepareStatement(updateQuery);
            int rowsUpdated = updateStmt.executeUpdate();
            System.out.println("Number of rows updated in subscriber_order: " + rowsUpdated);

            // Step 3: Retrieve the last subscriber waiting for each book based on order_date
            String getLastWaitingSubscriberQuery = "SELECT s.subscriber_id, s.subscriber_first_name, s.subscriber_last_name,\n" +
                    "       s.subscriber_phone_number, s.subscriber_email, so.is_active\n" +
                    "FROM subscriber_order so\n" +
                    "         JOIN subscriber s ON so.subscriber_id = s.subscriber_id\n" +
                    "WHERE so.book_id = ?\n" +
                    "  AND so.is_active = 1\n" +
                    "ORDER BY so.order_date DESC\n" +
                    "LIMIT 1";
            getLastWaitingSubscriberStmt = connection.prepareStatement(getLastWaitingSubscriberQuery);

            for (String bookId : bookIds) {
                getLastWaitingSubscriberStmt.setString(1, bookId);
                ResultSet subscriberResultSet = getLastWaitingSubscriberStmt.executeQuery();

                if (subscriberResultSet.next()) {
                    Subscriber lastWaitingSubscriber = new Subscriber(
                            subscriberResultSet.getString("subscriber_id"),
                            subscriberResultSet.getString("subscriber_first_name"),
                            subscriberResultSet.getString("subscriber_last_name"),
                            subscriberResultSet.getString("subscriber_phone_number"),
                            subscriberResultSet.getString("subscriber_email"),
                            subscriberResultSet.getBoolean("is_active")
                    );

                    bookToSubscriberMap.put(bookId, lastWaitingSubscriber);
                    System.out.println("Last waiting subscriber for book ID " + bookId + ": " + lastWaitingSubscriber);
                }
                subscriberResultSet.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bookResultSet != null) bookResultSet.close();
                if (selectBookStmt != null) selectBookStmt.close();
                if (updateStmt != null) updateStmt.close();
                if (getLastWaitingSubscriberStmt != null) getLastWaitingSubscriberStmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return bookToSubscriberMap;
    }



    /**
     * Retrieves a list of subscribers who have been frozen for more than one calendar month.
     *
     * @return List of subscribers who have been frozen for more than one calendar month.
     */
    /**
     * Updates subscribers who have been frozen exactly for one month.
     *
     * This method performs the following steps:
     * 1. Finds all subscribers in the `scheduler_triggers` table where the trigger operation is 'freeze' and the trigger date is exactly 30 days ago.
     * 2. Updates the `is_active` status in the `subscriber` table to 1 for these subscribers.
     * 3. Prints to the console which subscribers were unfrozen.
     *
     * @throws SQLException if a database access error occurs.
     */
    public void handleOneMonthFromFreezeDate() {
        PreparedStatement selectStmt = null;
        PreparedStatement updateSubscriberStmt = null;
        ResultSet rs = null;

        try {
            // Step 1: Select frozen subscribers from `scheduler_triggers` where freeze date is exactly 30 days ago
            String selectQuery = "SELECT t.relevent_id, s.subscriber_first_name, s.subscriber_last_name\n" +
                    "FROM scheduler_triggers t\n" +
                    "         JOIN subscriber s ON t.relevent_id = s.subscriber_id\n" +
                    "WHERE t.trigger_operation = 'freeze'\n" +
                    "  AND DATEDIFF(CURRENT_DATE, t.trigger_date) = 30\n" +
                    "  AND s.is_active = 0";
            selectStmt = connection.prepareStatement(selectQuery);
            rs = selectStmt.executeQuery();

            List<String> frozenSubscriberIds = new ArrayList<>();

            while (rs.next()) {
                String subscriberId = rs.getString("relevent_id");
                String subscriberName = rs.getString("subscriber_first_name") + " " + rs.getString("subscriber_last_name");
                frozenSubscriberIds.add(subscriberId);
                System.out.println("Unfreezing subscriber: " + subscriberName + " (ID: " + subscriberId + ")");
            }

            // Step 2: Update `subscriber` table to set `is_active = 1` for frozen subscribers
            if (!frozenSubscriberIds.isEmpty()) {
                String updateSubscriberQuery = "UPDATE subscriber\n" +
                        "SET is_active = 1\n" +
                        "WHERE subscriber_id = ?";
                updateSubscriberStmt = connection.prepareStatement(updateSubscriberQuery);

                for (String subscriberId : frozenSubscriberIds) {
                    updateSubscriberStmt.setString(1, subscriberId);
                    updateSubscriberStmt.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (selectStmt != null) selectStmt.close();
                if (updateSubscriberStmt != null) updateSubscriberStmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Fetches the full name of a subscriber based on their ID.
     *
     * @param subscriberID The ID of the subscriber.
     * @return The full name of the subscriber, or null if not found.
     */
    public String fetchSubscriberName(String subscriberID) {
        String subscriberName = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String query = "SELECT subscriber_first_name, subscriber_last_name FROM subscriber WHERE subscriber_id = ?";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, subscriberID);

            rs = stmt.executeQuery();
            if (rs.next()) {
                subscriberName = rs.getString("subscriber_first_name") + " " + rs.getString("subscriber_last_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return subscriberName;
    }

    /**
     * Fetches the subscriber's name and the book title for their order.
     *
     * @param subscriberID The ID of the subscriber.
     * @return A map containing the subscriber's name and the book title.
     */
    public Map<String, String> fetchOrderDetails(String subscriberID) {
        Map<String, String> details = new HashMap<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String query = "SELECT s.subscriber_first_name, s.subscriber_last_name, b.book_title\n" +
                    "FROM subscriber_order so\n" +
                    "         JOIN subscriber s ON so.subscriber_id = s.subscriber_id\n" +
                    "         JOIN book b ON so.book_id = b.book_id\n" +
                    "WHERE so.subscriber_id = ?\n" +
                    "  AND so.is_active = 1";

            stmt = connection.prepareStatement(query);
            stmt.setString(1, subscriberID);

            rs = stmt.executeQuery();
            if (rs.next()) {
                details.put("subscriberName", rs.getString("subscriber_first_name") + " " + rs.getString("subscriber_last_name"));
                details.put("bookTitle", rs.getString("book_title"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return details;
    }





}

