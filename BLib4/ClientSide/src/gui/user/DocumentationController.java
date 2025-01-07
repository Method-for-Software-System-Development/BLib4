package gui.user;

import entities.logic.Borrow;


public class DocumentationController {
	
	// Static variable to hold the single instance
    private static DocumentationController instance;

    /**
     * Public method to get the single instance of DocumentationController.
     * If the instance does not exist, it is created.
     *
     * @return The single instance of DocumentationController.
     */
    public static DocumentationController getInstance() {
        if (instance == null) {
            synchronized (DocumentationController.class) { // Ensure thread safety
                if (instance == null) {
                    instance = new DocumentationController();
                }
            }
        }
        return instance;
    }

    /**
     * Method to receive a Borrow object.
     *
     * @param borrow The Borrow object to process.
     */
	public static void receiveBorrow(Borrow clientBorrow) {
		// To-Do
        System.out.println("Borrow received in DocumentationController:");
        System.out.println("Borrow Date: " + clientBorrow.getBrrowDate());
        System.out.println("Due Date: " + clientBorrow.getDueDate());
        System.out.println("Status: " + clientBorrow.getStatus());
	}
}
