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
            stmt = connection.prepareStatement("select * from subscriber where subscriber_id = ? and subscriber_password = ?;");
            stmt.setString(1, subscriberId);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            // we have only on a result if existed, else zero.
            if (rs.next())
            {
                subscriber = new Subscriber(rs.getString(1), rs.getString(2), /*add a file as 3*/
                        rs.getString(4), rs.getString(5), rs.getBoolean(6));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
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
            stmt = connection.prepareStatement("select * from librarian where librarian_id = ? and librarian_password = ?;");
            stmt.setString(1, librarianId);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            // we have only on a result if existed, else zero.
            if (rs.next())
            {
                librarian = new Librarian(rs.getString(1), rs.getString(2));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
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
            stmt = connection.prepareStatement("select * from subscriber where subscriber_id = ?;");
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
            stmt = connection.prepareStatement("INSERT INTO subscriber (subscriber_id, subscriber_name, " +
                    "detailed_subscription_history, subscriber_phone_number, subscriber_email, subscriber_status, " +
                    "subscriber_password) VALUES (?, ?, ?, ?, ?, ?, ?);");

            stmt.setString(1, data.get(0));
            stmt.setString(2, data.get(1));
            stmt.setInt(3, historyId);
            stmt.setString(4, data.get(2));
            stmt.setString(5, data.get(3));
            stmt.setBoolean(6, true);
            stmt.setString(7, data.get(4));

            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }

        return books;
    }

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

}

//    /**
//     * The method run SQL query to get all the subscribers from the db
//     *
//     * @return list of all the subscribers
//     */
//    public List<Subscriber> getAllSubscribers()
//    {
//        List<Subscriber> subscribers = new ArrayList<>();
//        Statement stmt;
//
//        try
//        {
//            stmt = connection.createStatement();
//            ResultSet rs = stmt.executeQuery("select * from subscriber");
//
//            // save the data to the list
//            while (rs.next())
//            {
//                // create new subscriber
//                Subscriber subscriber = new Subscriber(Integer.parseInt(rs.getString(1)),
//                        rs.getString(2), Integer.parseInt(rs.getString(3)),
//                        rs.getString(4), rs.getString(5));
//
//                // add the subscriber to the list
//                subscribers.add(subscriber);
//            }
//        }
//        catch (SQLException e)
//        {
//            e.printStackTrace();
//        }
//
//        return subscribers;
//    }
//
//    /**
//     * The method run SQL query to get subscriber from db by id
//     *
//     * @param id - the id of the subscriber
//     * @return the subscriber with the id
//     */
//    public Subscriber getSubscriberById(String id)
//    {
//        Subscriber subscriber = null;
//
//        PreparedStatement stmt;
//
//        try
//        {
//            stmt = connection.prepareStatement("select * from subscriber where subscriber_id = ?");
//            stmt.setString(1, id);
//
//            ResultSet rs = stmt.executeQuery();
//
//            // we have only on result if existed, else zero.
//            if (rs.next())
//            {
//                subscriber = new Subscriber(Integer.parseInt(rs.getString(1)),
//                        rs.getString(2), Integer.parseInt(rs.getString(3)),
//                        rs.getString(4), rs.getString(5));
//            }
//        }
//        catch (SQLException e)
//        {
//            e.printStackTrace();
//        }
//
//        return subscriber;
//    }
//
//
//    /**
//     * The method run SQL query to update the subscriber in the db with the new data
//     *
//     * @param newSubscriber - the new subscriber data
//     */
//    public boolean updateSubscriber(Subscriber newSubscriber)
//    {
//        PreparedStatement stmt;
//
//        try
//        {
//            stmt = connection.prepareStatement("UPDATE subscriber SET subscriber_email = ?, subscriber_phone_number = ? where subscriber_id = ?");
//
//            stmt.setString(1, newSubscriber.getEmail());
//            stmt.setString(2, newSubscriber.getPhoneNumber());
//            stmt.setInt(3, newSubscriber.getId());
//
//            stmt.executeUpdate();
//        }
//        catch (SQLException e)
//        {
//            e.printStackTrace();
//            return false;
//        }
//
//        return true;
//    }
//}