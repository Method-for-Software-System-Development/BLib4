package logic;

import javafx.beans.property.SimpleStringProperty;

/**
 * This class is used to store the information of a client for the monitor table.
 * It contains the index, ip, host and status of a client.
 */
public class ClientInfo
{
    private final SimpleStringProperty index;
    private final SimpleStringProperty ip;
    private final SimpleStringProperty host;
    private final SimpleStringProperty status;

    /**
     * Constructor of the class.
     *
     * @param index  - the index of the client
     * @param ip     - the ip of the client
     * @param host   - the host of the client
     * @param status - the connection status of the client
     */
    public ClientInfo(String index, String ip, String host, String status)
    {
        this.index = new SimpleStringProperty(index);
        this.ip = new SimpleStringProperty(ip);
        this.host = new SimpleStringProperty(host);
        this.status = new SimpleStringProperty(status);
    }

    /**
     * Get the ip of the client.
     *
     * @return - the ip of the client
     */
    public String getIndex()
    {
        return index.get();
    }

    /**
     * Get the ip of the client.
     *
     * @return - the ip of the client
     */
    public SimpleStringProperty indexProperty()
    {
        return index;
    }

    /**
     * Get the ip of the client.
     *
     * @return - the ip of the client
     */
    public SimpleStringProperty ipProperty()
    {
        return ip;
    }

    /**
     * Get the host of the client.
     *
     * @return - the host of the client
     */
    public SimpleStringProperty hostProperty()
    {
        return host;
    }

    /**
     * Get the status of the client.
     *
     * @return - the status of the client
     */
    public SimpleStringProperty statusProperty()
    {
        return status;
    }
}