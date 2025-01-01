package entities.user;

import java.util.regex.Pattern;

public abstract class User {
	private String ID;
	private String full_name;
	private String userName;
	private String password;
	private String phoneNumber = null;
	public String email = null;
	private boolean EmailChanged;
	private boolean PhoneChanged;
	
	
    /**
     * Default constructor
     *
     * @param ID
     * @param full_name
     * @param userName
     * @param phoneNumber
     * @param email
     * @param EmailChanged
     */
	
	public User(String ID, String full_name, String userName, String password, String newPhoneNumber, String Email) {
		this.ID = ID;
		this.full_name = full_name;
		this.userName = userName;
		this.password = password;

		if(newPhoneNumber!=null) {
			phoneNumber = newPhoneNumber;
		}
		
		if (Email != null && validateEmail(Email)) {
            this.email = Email;
            EmailChanged = true;
        } else {
        	EmailChanged = false;
        }
	}
	
	private boolean validateEmail(String Email) {
		//Pattern for correct email
		 Pattern validEmailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
		 
		 //Checking if the entered email is valid
		 return validEmailPattern.matcher(Email).matches();
	}

	// getting the password is available
	public String getPassword() {
		return password;
	}

	// Setting a new password is available
	public void setPassword(String password) {
		if(password!=null) {
			this.password = password;
		}
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	// Setting a new phone number is available
	public void setPhoneNumber(String newPhoneNumber) {
		if(newPhoneNumber!=null && phoneNumber!= newPhoneNumber) {
			this.phoneNumber = newPhoneNumber;
			PhoneChanged = true;
		}
		else { PhoneChanged = false; }
	}
	
	public boolean getPhoneNumberChanged() {
		return PhoneChanged;
	}

	public String  getEmail() {
		return email;
	}

	// Changing email is available
	public void setEmail(String newEmail){
		this.EmailChanged = true;
		Pattern validEmailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
		try {
			if (newEmail == null || !validEmailPattern.matcher(newEmail).matches()) {
	            throw new IllegalArgumentException("Invalid Email");
			}
			email = newEmail;
		}catch(IllegalArgumentException e) {
			EmailChanged = false;
		}
	}
	
	//Getter to know if the new email was set correctly
	public boolean getEmailSet() {
		return EmailChanged;
	}

	public String getID() {
		return ID;
	}

	public String getFull_name() {
		return full_name;
	}

	// Changing name is available
	public void setFull_name(String newName) {
		if(newName!=null) {
			full_name = newName;
		}
	}

	public String getUserName() {
		return userName;
	} 

}
