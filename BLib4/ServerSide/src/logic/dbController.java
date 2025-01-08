package logic;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
                        rs.getString("subscriber_email"), rs.getBoolean("subscriber_status"));
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
                    rs.getString("title"),
                    rs.getInt("edition_number"),
                    rs.getDate("printDate"),
                    rs.getString("subject"),
                    rs.getString("description")
            );

            // get the image of the book
            book.setImage(ImageUtil.convertBlobToImage(rs.getBlob("image")));

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
                        rs.getString("subscriber_email"), rs.getBoolean("subscriber_status"));

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

                if (rs.getBoolean("is_borrowed"))
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
                if (!rs.getBoolean("subscriber_status"))
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

}
