package logic.communication;

public interface DataReceiver
{
    /**
     * Receives data from a sender.
     * @param data - The data received.
     */
    void receiveData(Object data);
}