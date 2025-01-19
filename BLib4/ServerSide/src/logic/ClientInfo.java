package logic;

import javafx.beans.property.SimpleStringProperty;
public class ClientInfo {
    private final SimpleStringProperty index;
    private final SimpleStringProperty ip;
    private final SimpleStringProperty host;
    private final SimpleStringProperty status;

    public ClientInfo(String index, String ip, String host, String status) {
        this.index = new SimpleStringProperty(index);
        this.ip = new SimpleStringProperty(ip);
        this.host = new SimpleStringProperty(host);
        this.status = new SimpleStringProperty(status);
    }

    public String getIndex() {
        return index.get();
    }

    public SimpleStringProperty indexProperty() {
        return index;
    }

    public String getIp() {
        return ip.get();
    }

    public SimpleStringProperty ipProperty() {
        return ip;
    }

    public String getHost() {
        return host.get();
    }

    public SimpleStringProperty hostProperty() {
        return host;
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }
}