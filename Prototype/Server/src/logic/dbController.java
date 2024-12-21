package logic;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
     * get the instance of the dbController
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
     * connect to the db
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
     * get all the subscribers from the db
     *
     * @return list of all the subscribers
     */
    public List<Subscriber> getAllSubscribers()
    {
        List<Subscriber> subscribers = new ArrayList<>();
        Statement stmt;

        try
        {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from subscriber");

            // save the data to the list
            while (rs.next())
            {
                // create new subscriber
                Subscriber subscriber = new Subscriber(Integer.parseInt(rs.getString(1)),
                        rs.getString(2), Integer.parseInt(rs.getString(3)),
                        rs.getString(4), rs.getString(5));

                // add the subscriber to the list
                subscribers.add(subscriber);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return subscribers;
    }

    /**
     * get subscriber from db by id
     *
     * @param id - the id of the subscriber
     * @return the subscriber with the id
     */
    public Subscriber getSubscriberById(String id)
    {
        Subscriber subscriber = null;

        PreparedStatement stmt;

        try
        {
            stmt = connection.prepareStatement("select * from subscriber where subscriber_id = ?");
            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();

            // we have only on result if existed, else zero.
            if (rs.next())
            {
                subscriber = new Subscriber(Integer.parseInt(rs.getString(1)),
                        rs.getString(2), Integer.parseInt(rs.getString(3)),
                        rs.getString(4), rs.getString(5));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return subscriber;
    }


    /**
     * update the subscriber in the db with the new data
     *
     * @param newSubscriber - the new subscriber data
     */
    public boolean updateSubscriber(Subscriber newSubscriber)
    {
        PreparedStatement stmt;

        try
        {
            stmt = connection.prepareStatement("UPDATE subscriber SET subscriber_email = ?, subscriber_phone_number = ? where subscriber_id = ?");

            stmt.setString(1, newSubscriber.getEmail());
            stmt.setString(2, newSubscriber.getPhoneNumber());
            stmt.setInt(3, newSubscriber.getId());

            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}