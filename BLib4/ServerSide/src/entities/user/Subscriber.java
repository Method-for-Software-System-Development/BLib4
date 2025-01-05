package entities.user;

import java.io.Serializable;
import java.util.Date;
import java.util.regex.Pattern;

public class Subscriber implements Serializable
{
    private final String id;
    private final String name;
    //? save the subscription history in a file
    private String phone;
    private String email;
    private boolean status;

    private boolean EmailChanged = false;
    private boolean PhoneChanged = false;

    /**
     * Default constructor
     * @param id - the id of the subscriber
     * @param name - the name of the subscriber
     * @param phone - the phone number of the subscriber
     * @param email - the email of the subscriber
     * @param status - the status of the subscriber
     */
    public Subscriber(String id, String name, String phone, String email, boolean status)
    {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.status = status;

        // ToDo: add subscription history file
    }

    /**
     * get the id of the subscriber
     * @return - the id of the subscriber
     */
    public String getId()
    {
        return id;
    }

    /**
     * get the name of the subscriber
     * @return - the name of the subscriber
     */
    public String getName()
    {
        return name;
    }

    /**
     * get the phone number of the subscriber
     * @return - the phone number of the subscriber
     */
    public String getPhone()
    {
        return phone;
    }

    /**
     * get the email of the subscriber
     * @return - the email of the subscriber
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * get the status of the subscriber
     * @return - the status of the subscriber
     */
    public boolean getStatus()
    {
        return status;
    }

    /**
     * set the phone number of the subscriber
     * @param phone - the phone number of the subscriber
     */
    public void setPhone(String phone)
    {
        if (!this.phone.equals(phone))
        {
            this.phone = phone;
            this.PhoneChanged = true;
        }
    }

    /**
     * set the email of the subscriber
     * @param email - the email of the subscriber
     */
    public void setEmail(String email)
    {
        if (validateEmail(email) && !this.email.equals(email))
        {
            this.email = email;
            this.EmailChanged = true;
        }
    }

    /**
     * set the status of the subscriber
     * @param status - the status of the subscriber
     */
    public void setStatus(boolean status)
    {
        this.status = status;
    }

    /**
     * Validate the email
     * @param email - the email to validate
     * @return
     */
    private boolean validateEmail(String email) {
        //Pattern for correct email
        Pattern validEmailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        //Checking if the entered email is valid
        return validEmailPattern.matcher(email).matches();
    }

    /**
     * The method returns if the subscriber data has changed
     * @return - true if the subscriber data has changed, false otherwise
     */
    public boolean isChanged()
    {
        return EmailChanged || PhoneChanged;
    }

    /**
     * The method resets the changed flags
     */
    public void resetChanged()
    {
        EmailChanged = false;
        PhoneChanged = false;
    }

    /**
     * The method returns the subscriber as string
     * @return the subscriber as string
     */
    @Override
    public String toString()
    {
        return "Subscriber{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                '}';
    }



}
