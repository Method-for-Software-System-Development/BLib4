package logic;


import java.io.Serializable;

public class Subscriber implements Serializable {

    private int id;
    private String name;
    private int subscriptionHistory;
    private String phoneNumber;
    private String email;

    public Subscriber(int id, String name, int subscriptionHistory, String phoneNumber, String email) {
        this.id = id;
        this.name = name;
        this.subscriptionHistory = subscriptionHistory;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSubscriptionHistory() {
        return subscriptionHistory;
    }

    public void setSubscriptionHistory(int subscriptionHistory) {
        this.subscriptionHistory = subscriptionHistory;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", subscriptionHistory=" + subscriptionHistory +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
