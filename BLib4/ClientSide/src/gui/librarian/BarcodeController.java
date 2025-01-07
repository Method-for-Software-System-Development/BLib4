package gui.librarian;

public class BarcodeController {
	
	private static BarcodeController instance; // Singleton instance
    private String scannedBookBarcode = "1"; // Stores the scanned book ID (barcode)
    
	public String getScannedBookBarcode() {
		return this.scannedBookBarcode;
	}
	
    // Method to get the Singleton instance 
	// *Do Not touch must be singleton!!*
    public static BarcodeController getInstance() {
        if (instance == null) {
            instance = new BarcodeController();
        }
        return instance;
    }
}
