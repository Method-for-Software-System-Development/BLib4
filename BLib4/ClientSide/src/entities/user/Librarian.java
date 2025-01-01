package entities.user;

import java.io.Serializable;
import java.util.Date;

public class Librarian extends User implements Serializable{
	private Date employmentDate;

	public Librarian(String ID, String full_name, String userName, String password, String phoneNumber,
					 String Email, Date employmentDate) {
		
		super(ID, full_name, userName, password, phoneNumber, Email);
		this.employmentDate = employmentDate;
	}

	public Date getEmploymentDate() {
		return employmentDate;
	}
	
}
