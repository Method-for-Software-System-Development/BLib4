package entities.logic;

import java.io.Serializable;

public class MessageType implements Serializable
{
    private String id;
    public Object data;

    /**
     * Default constructor
     *
     * @param id
     * @param data
     */
    public MessageType(String id, Object data)
    {
        this.id = id;
        this.data = data;
    }

    /**
     * getter for the id
     * @return the id of the message
     */
    public String getId()
    {
        return id;
    }

    /**
     * getter for the data
     * @return the data object of the message
     */
    public Object getData()
    {
        return data;
    }

    /**
     * setter for the id of the message
     * @param id the id to set
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * setter for the data of the message
     * @param data the data to set
     */
    public void setData(Object data)
    {
        this.data = data;
    }
}
