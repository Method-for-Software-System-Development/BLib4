package entities.user;

import java.util.Date;
import java.io.Serializable;

public class Subscriber extends User implements Serializable
{
	private Date birthDate;
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
    				  String Email, int subscriptionHistory, Date birthDate) {
    	
		super(ID, full_name, userName, password, phoneNumber, Email);
		this.subscriptionHistory = subscriptionHistory;
		this.birthDate = birthDate;
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

    public boolean getAccStatus() {
    	return status;
    }
    
    public Date getDOB() {
    	return birthDate;
    }
    
    public void setDOB(Date newBirthDate) {
    	birthDate = newBirthDate;
    }
    
    
    @Override
    public String toString() {
    	StringBuilder s = new StringBuilder();
    	s.append("ID: " + getID() + "\n");
    	s.append("name: " + getFull_name() + "\n");
    	s.append("Subscription history file: " + subscriptionHistory + "\n");
    	s.append("Phone number: " + getPhoneNumber() + "\n");
    	s.append("Email: " + getEmail());
    	return s.toString();
    }  
    
}
