package entities.user;

import java.io.Serializable;
import java.util.regex.Pattern;

public class Subscriber implements Serializable
{
    private final String id;
    private final String firstName;
    private final String lastName;
    private String phone;
    private String email;
    private boolean status;

    /**
     * Default constructor
     *
     * @param id        - the id of the subscriber
     * @param firstName - the name of the subscriber
     * @param lastName  - the last name of the subscriber
     * @param phone     - the phone number of the subscriber
     * @param email     - the email of the subscriber
     * @param status    - the status of the subscriber
     */
    public Subscriber(String id, String firstName, String lastName, String phone, String email, boolean status)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.status = status;
    }

    /**
     * get the id of the subscriber
     *
     * @return - the id of the subscriber
     */
    public String getId()
    {
        return id;
    }

    /**
     * get the first name of the subscriber
     *
     * @return - the first name of the subscriber
     */
    public String getFirstName()
    {
        return firstName;
    }

    /**
     * get the last name of the subscriber
     *
     * @return - the last name of the subscriber
     */
    public String getLastName()
    {
        return lastName;
    }

    /**
     * get the phone number of the subscriber
     *
     * @return - the phone number of the subscriber
     */
    public String getPhone()
    {
        return phone;
    }

    /**
     * get the email of the subscriber
     *
     * @return - the email of the subscriber
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * get the status of the subscriber
     *
     * @return - the status of the subscriber
     */
    public boolean getStatus()
    {
        return status;
    }

    /**
     * set the phone number of the subscriber
     *
     * @param phone - the phone number of the subscriber
     */
    public void setPhone(String phone)
    {
        if (!this.phone.equals(phone))
        {
            this.phone = phone;
        }
    }

    /**
     * set the email of the subscriber
     *
     * @param email - the email of the subscriber
     */
    public void setEmail(String email)
    {
        if (validateEmail(email) && !this.email.equals(email))
        {
            this.email = email;
        }
    }

    /**
     * set the status of the subscriber
     *
     * @param status - the status of the subscriber
     */
    public void setStatus(boolean status)
    {
        this.status = status;
    }

    /**
     * Validate the email
     *
     * @param email - the email to validate
     * @return - true if the email is valid, false otherwise
     */
    private boolean validateEmail(String email)
    {
        //Pattern for correct email
        Pattern validEmailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        //Checking if the entered email is valid
        return validEmailPattern.matcher(email).matches();
    }

    /**
     * The method returns the subscriber as string
     *
     * @return the subscriber as string
     */
    @Override
    public String toString()
    {
        return "Subscriber{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                '}';
    }
}
