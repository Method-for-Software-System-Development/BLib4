package entities.user;


import java.io.Serializable;

public class Subscriber extends User implements Serializable
{

    private int subscriptionHistory;
    private boolean status = true;

    
	/**
     * Default constructor
     *
     * @param ID
     * @param full_name
     * @param userName
     * @param password
     * @param phoneNumber
     * @param Email
     * @param subscriptionHistory
     * @param status
     */
    public Subscriber(String ID, String full_name, String userName, String password, String phoneNumber,
    				  String Email, int subscriptionHistory) {
    	
		super(ID, full_name, userName, password, phoneNumber, Email);
		this.subscriptionHistory = subscriptionHistory;
	}

    public int getSubscriptionHistory()
    {
        return subscriptionHistory;
    }

	public void setSubscriptionHistory(int subscriptionHistory)
    {
        this.subscriptionHistory = subscriptionHistory;
    }
    
    public void setAccToBeFrozen() {
    	this.status = false;
    }
    
    public void setAccToBeAcctive() {
    	this.status = true;
    }

    @Override
    public String toString()
    {
        return "Student{" +
                "id=" + getID() +
                ", name='" + getFull_name() + '\'' +
                ", subscriptionHistory=" + subscriptionHistory +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                ", email='" + getEmail() + '\'' +
                '}';
    }
    
}
