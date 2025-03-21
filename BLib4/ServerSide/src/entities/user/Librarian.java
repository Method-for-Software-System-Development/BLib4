package entities.user;

import java.io.Serializable;

public class Librarian implements Serializable
{
    private String id;
    private String firstName;
    private String lastName;

    /**
     * Default constructor
     *
     * @param id        - the id of the librarian
     * @param firstName - the firstName of the librarian
     * @param lastName  - the lastName of the librarian
     */
    public Librarian(String id, String firstName, String lastName)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * get the id of the librarian
     *
     * @return - the id of the librarian
     */
    public String getId()
    {
        return id;
    }

    /**
     * get the name of the librarian
     *
     * @return - the name of the librarian
     */
    public String getName()
    {
        return firstName;
    }

    /**
     * get the last name of the librarian
     *
     * @return - the last name of the librarian
     */
    public String getLastName()
    {
        return lastName;
    }
}
