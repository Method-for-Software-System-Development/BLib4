package entities.book;

import java.util.Date;

public class CoptOfTheBook {
	private final String copyID;
	private String locationOnShelf;
	private final Date purchaseDate;
	
	public CoptOfTheBook(String copyID, String locationOnShelf, Date purchaseDate) {
		this.copyID = copyID;
		this.locationOnShelf = locationOnShelf;
		this.purchaseDate = purchaseDate;
	}
 
	public String getCopyID() {
		return copyID;
	}

	public String getLocationOnShelf() {
		return locationOnShelf;
	}
	
	//Setter for changing a place on the shelf in the library
	public void setLocationOnShelf(String newLocation) {
		locationOnShelf = newLocation;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}
	
}
