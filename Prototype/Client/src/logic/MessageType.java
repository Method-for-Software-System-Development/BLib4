package logic;

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

    public String getId()
    {
        return id;
    }

    public Object getData()
    {
        return data;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setData(Object data)
    {
        this.data = data;
    }
}
