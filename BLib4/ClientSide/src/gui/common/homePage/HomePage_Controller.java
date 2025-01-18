package gui.common.homePage;

import entities.book.Book;
import entities.logic.MessageType;
import entities.user.Librarian;
import entities.user.Subscriber;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import logic.communication.ChatClient;
import logic.communication.ClientUI;
import logic.communication.SceneManager;
import logic.user.Subscriber_Controller;

import java.util.List;

/**
 * Controller class for the Home Page (HomePage_UI.fxml).
 * This class is responsible for handling user interactions on the home page,
 * including login functionality, scanning a reader card, and displaying books in HBoxes.
 */
public class HomePage_Controller {

    // FXML fields for seeIfNotLoggedIn
    @FXML
    private VBox seeIfNotLoggedIn;
    @FXML
    private RadioButton subscriberRadioButton;
    @FXML
    private RadioButton librarianRadioButton;
    @FXML
    private ToggleGroup accountTypeGroup;
    @FXML
    private TextField idTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Button scanReaderCardButton;
    @FXML
    private ImageView scanReaderCardImageView;
    @FXML
    private Button loginButton;
    @FXML
    private ImageView loginImageView;
    @FXML
    private Button searchBooksButton;
    @FXML
    private ImageView searchBooksImageView;

    // FXML fields for seeIfLoggedIn
    @FXML
    private VBox seeIfLoggedIn;
    @FXML
    private Text userGreeting;
    @FXML
    private Button dashboardButton;
    @FXML
    private ImageView dashboardImageView;
    @FXML
    private Button searchBooksButton2;
    @FXML
    private ImageView searchBooksImageView2;
    @FXML
    private Button viewHistoryButton;
    @FXML
    private ImageView viewHistoryImageView;
    @FXML
    private Button editProfileButton;
    @FXML
    private ImageView editProfileImageView;
    @FXML
    private Button logoutButton;
    @FXML
    private ImageView logoutImageView;

    // FXML fields for both
    @FXML
    private Button exitButton;
    @FXML
    private ImageView exitImageView;
    @FXML
    private HBox newArrivalsHBox;
    @FXML
    private HBox mostBorrowedHBox;

    // Fields
    private Subscriber_Controller subscriberController;
    private boolean isSubscriberLoggedIn;
    private boolean isLibrarianLoggedIn;
    private List<Book> newArrivalBooks;
    private List<Book> mostBorrowedBooks;

    /**
     * Initializes the HomePage_Controller.
     * Sets up event handlers for hover effects on buttons, retrieves books data,
     * and populates the corresponding UI elements.
     */
    @FXML
    public void initialize() {
        // Get the singleton instance of Subscriber_Controller
        subscriberController = Subscriber_Controller.getInstance();

        // Check if a Subscriber or Librarian is logged in
        isSubscriberLoggedIn = subscriberController.getLoggedSubscriber() != null;
        isLibrarianLoggedIn = subscriberController.getLoggedLibrarian() != null;

        // Show the appropriate VBox based on login status
        if (isSubscriberLoggedIn || isLibrarianLoggedIn) {
            seeIfNotLoggedIn.setVisible(false);
            seeIfNotLoggedIn.setManaged(false);
            seeIfLoggedIn.setVisible(true);
            seeIfLoggedIn.setManaged(true);
            userGreeting.setText(getGreetingMessage() + " " + subscriberController.getLoggedSubscriber().getFirstName() + " !");
        } else {
            seeIfNotLoggedIn.setVisible(true);
            seeIfNotLoggedIn.setManaged(true);
            seeIfLoggedIn.setVisible(false);
            seeIfLoggedIn.setManaged(false);
        }

        // Set up radio buttons for account type selection
        accountTypeGroup = new ToggleGroup();
        subscriberRadioButton.setToggleGroup(accountTypeGroup);
        librarianRadioButton.setToggleGroup(accountTypeGroup);
        subscriberRadioButton.setSelected(true);

        // Set up hover effects for buttons
        scanReaderCardButton.setOnMouseEntered(event -> {
            scanReaderCardImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/qr_code_scanner_24dp_525FE1.png")));
        });
        scanReaderCardButton.setOnMouseExited(event -> {
            scanReaderCardImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/qr_code_scanner_24dp_FFFFFF.png")));
        });

        loginButton.setOnMouseEntered(event -> {
            loginImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/login_24dp_F86F03.png")));
        });
        loginButton.setOnMouseExited(event -> {
            loginImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/login_24dp_FFFFFF.png")));
        });

        searchBooksButton.setOnMouseEntered(event -> {
            searchBooksImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/search_24dp_525FE1.png")));
        });
        searchBooksButton.setOnMouseExited(event -> {
            searchBooksImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/search_24dp_FFFFFF.png")));
        });

        dashboardButton.setOnMouseEntered(event -> {
            dashboardImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/person_24dp_F86F03.png")));
        });
        dashboardButton.setOnMouseExited(event -> {
            dashboardImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/person_24dp_FFFFFF.png")));
        });

        searchBooksButton2.setOnMouseEntered(event -> {
            searchBooksImageView2.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/search_24dp_525FE1.png")));
        });
        searchBooksButton2.setOnMouseExited(event -> {
            searchBooksImageView2.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/search_24dp_FFFFFF.png")));
        });

        viewHistoryButton.setOnMouseEntered(event -> {
            viewHistoryImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/history_24dp_525FE1.png")));
        });
        viewHistoryButton.setOnMouseExited(event -> {
            viewHistoryImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/history_24dp_FFFFFF.png")));
        });

        editProfileButton.setOnMouseEntered(event -> {
            editProfileImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/edit_24dp_525FE1.png")));
        });
        editProfileButton.setOnMouseExited(event -> {
            editProfileImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/edit_24dp_FFFFFF.png")));
        });

        logoutButton.setOnMouseEntered(event -> {
            logoutImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/logout_24dp_525FE1.png")));
        });
        logoutButton.setOnMouseExited(event -> {
            logoutImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/logout_24dp_FFFFFF.png")));
        });

        exitButton.setOnMouseEntered(event -> {
            exitImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/close_24dp_525FE1.png")));
        });
        exitButton.setOnMouseExited(event -> {
            exitImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/close_24dp_FFFFFF.png")));
        });

        // Retrieve data for new arrivals and most borrowed books
        ClientUI.chat.accept(new MessageType("120", null));
        newArrivalBooks = ChatClient.books;
        populateBooksHBox(newArrivalsHBox, newArrivalBooks);

        ClientUI.chat.accept(new MessageType("121", null));
        mostBorrowedBooks = ChatClient.books;
        populateBooksHBox(mostBorrowedHBox, mostBorrowedBooks);
    }

    /**
     * Returns a greeting message based on the current time of day.
     *
     * @return the greeting message
     */
    private String getGreetingMessage() {
        int hour = java.time.LocalTime.now().getHour();

        if (hour >= 5 && hour < 12) {
            return "Good Morning";
        } else if (hour >= 12 && hour < 17) {
            return "Good Afternoon";
        } else if (hour >= 17 && hour < 21) {
            return "Good Evening";
        } else {
            return "Good Night";
        }
    }

    /**
     * Populates the specified HBox with a list of books, each represented by a GridPane.
     *
     * @param targetHBox the HBox in which to place book displays
     * @param books      the list of books to be displayed
     */
    public void populateBooksHBox(HBox targetHBox, List<Book> books) {
        targetHBox.getChildren().clear(); // Clear previous content

        for (Book book : books) {
            // Create a GridPane for each book
            GridPane bookGrid = new GridPane();
            bookGrid.getStyleClass().add("bookContainer"); // CSS class for book container
            bookGrid.setHgap(10); // Horizontal gap between elements
            bookGrid.setVgap(10); // Vertical gap between elements

            // Set a fixed width and height for the GridPane
            bookGrid.setPrefWidth(438);  // Set preferred width (adjust as needed)
            bookGrid.setPrefHeight(285); // Set preferred height (adjust as needed)

            // Check if the book has an image
            if (book.getImage() != null) {
                // Create ImageView for the book cover
                ImageView bookImageView = new ImageView(book.getImage());
                bookImageView.setFitHeight(255);  // Set image height
                bookImageView.setFitWidth(185);   // Set image width

                // Add rounded corners to the image
                Rectangle clip = new Rectangle(185, 255);  // Define the size of the rectangle (matching image size)
                clip.setArcWidth(15);  // Set horizontal corner radius
                clip.setArcHeight(15); // Set vertical corner radius
                bookImageView.setClip(clip);  // Apply the clipping

                // Place the image at (0, 0) in GridPane
                GridPane.setConstraints(bookImageView, 0, 0);
                GridPane.setRowSpan(bookImageView, 5);  // Make the image span over 5 rows
                bookGrid.getChildren().add(bookImageView);

            } else {
                // Create a placeholder rectangle styled with CSS
                Rectangle placeholder = new Rectangle(185, 255); // Placeholder size
                placeholder.setArcWidth(15);  // Set horizontal corner radius
                placeholder.setArcHeight(15); // Set vertical corner radius
                placeholder.getStyleClass().add("book-placeholder"); // CSS class for styling
                Text placeholderText = new Text(book.getTitle());
                placeholderText.getStyleClass().add("small-grey-text"); // CSS class for text inside placeholder
                placeholderText.setWrappingWidth(165);
                placeholderText.setTextAlignment(TextAlignment.CENTER);
                GridPane.setConstraints(placeholder, 0, 0); // Place placeholder at (0, 0) in GridPane
                GridPane.setRowSpan(placeholder, 5); // Make the placeholder span over 5 rows
                bookGrid.getChildren().addAll(placeholder, placeholderText);
            }

            // Create Text for the book title with automatic wrapping
            Text bookTitle = new Text(book.getTitle());
            bookTitle.getStyleClass().add("medium-bold-purple-text");
            bookTitle.setWrappingWidth(213); // Set a wrapping width for the title text
            GridPane.setConstraints(bookTitle, 1, 0); // Place title at (1, 0) in GridPane

            // Create Text for the book author
            Text bookAuthor = new Text(book.getAuthor());
            bookAuthor.getStyleClass().add("small-bold-grey-text");
            bookAuthor.setWrappingWidth(213); // Set a wrapping width for the author text
            GridPane.setConstraints(bookAuthor, 1, 1); // Place author at (1, 1) in GridPane

            // Create Text for the book subject
            Text bookSubject = new Text(book.getSubject());
            bookSubject.getStyleClass().add("small-grey-text");
            bookSubject.setWrappingWidth(213); // Set a wrapping width for the subject text
            GridPane.setConstraints(bookSubject, 1, 2); // Place subject at (1, 2) in GridPane

            // Add a spacer region above the button
            Region spacer = new Region();
            GridPane.setConstraints(spacer, 1, 3);  // Place the spacer in the same row as the button
            GridPane.setVgrow(spacer, Priority.ALWAYS);  // Allow spacer to take available vertical space

            // Create the View Book button
            Button viewBookButton = new Button("View Book");
            viewBookButton.getStyleClass().add("purple-transparent-button"); // CSS class for styling
            viewBookButton.setPrefHeight(50.0);
            viewBookButton.setMaxWidth(Double.MAX_VALUE); // Make the button span the width of the GridPane
            GridPane.setConstraints(viewBookButton, 1, 4); // Place button at (1, 4) in GridPane

            // Add action to the button to open the ViewOrderBook scene
            viewBookButton.setOnAction(event -> {
                // Pass the current book to the ViewOrderBook scene
                SceneManager.switchSceneWithData("/gui/common/viewOrderBook/ViewOrderBook_UI.fxml", "BLib.4 - Braude Library Management", book);
            });

            // Add all elements to the GridPane
            bookGrid.getChildren().addAll(bookTitle, bookAuthor, bookSubject, spacer, viewBookButton);

            // Add the GridPane to the target HBox
            targetHBox.getChildren().add(bookGrid);
        }
    }

    /**
     * Prompts the user to enter a reader card ID in a custom Alert dialog,
     * then automatically populates the login form fields if valid input is provided.
     */
    @FXML
    private void validate_scanReaderCard_login() {
        // Create a custom Alert
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Scan Reader Card");
        alert.setHeaderText("Enter the Reader Card ID");

        // Create a TextField for input
        TextField inputField = new TextField();
        inputField.setPromptText("Enter ID");

        // Add the TextField to the Alert
        VBox content = new VBox();
        content.setSpacing(10);
        content.getChildren().add(inputField);
        alert.getDialogPane().setContent(content);

        // Add buttons
        ButtonType confirmButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().addAll(confirmButton, cancelButton);

        // Show the Alert and wait for a response
        alert.showAndWait().ifPresent(response -> {
            if (response == confirmButton) {
                String enteredId = inputField.getText();

                // Check if the ID is not empty
                if (enteredId != null && !enteredId.trim().isEmpty()) {
                    // Update the Login fields
                    idTextField.setText(enteredId);
                    passwordTextField.setText("readerCard");
                } else {
                    // Show an error if the ID is empty
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText("Invalid ID");
                    errorAlert.setContentText("Please enter a valid Reader Card ID.");
                    alert.initOwner(SceneManager.getStage());
                    errorAlert.showAndWait();
                }
            }
        });
    }

    /**
     * Validates the login form fields for user ID, password, and account type selection.
     * Attempts to log in as either a Subscriber or a Librarian based on the selected radio button.
     * If the login is successful, navigates to the corresponding UI screen; otherwise, shows an error alert.
     */
    @FXML
    public void validate_login_form() {
        // Reset styles before validation
        idTextField.getStyleClass().remove("error-text-field");
        passwordTextField.getStyleClass().remove("error-text-field");

        boolean isValid = true;

        // Store values in variables
        String userID = idTextField.getText();
        String password = passwordTextField.getText();
        RadioButton selectedRadioButton = (RadioButton) accountTypeGroup.getSelectedToggle();

        // Check if a radio button is selected
        if (selectedRadioButton == null) {
            isValid = false;
        }

        // Check if ID field is empty
        if (userID == null || userID.trim().isEmpty()) {
            idTextField.setPromptText("ID is required");
            idTextField.getStyleClass().add("error-text-field");
            isValid = false;
        }

        // Check if Password field is empty
        if (password == null || password.trim().isEmpty()) {
            passwordTextField.setPromptText("Password is required");
            passwordTextField.getStyleClass().add("error-text-field");
            isValid = false;
        }

        if (isValid) {
            String accountType = selectedRadioButton.getText();

            if (accountType.equals("Subscriber")) {
                Subscriber subscriber = subscriberController.attemptLoginAsSubscriber(userID, password);
                if (subscriber != null) {
                    SceneManager.switchScene("/gui/user/subscriberUI/SubscriberUI_UI.fxml", "BLib.4 - Braude Library Management");
                } else {
                    clearFields();
                    showErrorAlert("Login Failed", "The account was not found. Please verify that the details you entered are correct.");
                }
            } else if (accountType.equals("Librarian")) {
                Librarian librarian = subscriberController.attemptLoginAsLibrarian(userID, password);
                if (librarian != null) {
                    SceneManager.switchScene("/gui/librarian/librarianUI/LibrarianUI_UI.fxml", "BLib.4 - Braude Library Management");
                } else {
                    clearFields();
                    showErrorAlert("Login Failed", "The account was not found. Please verify that the details you entered are correct.");
                }
            }
        }
    }

    /**
     * Clears the ID and Password text fields.
     */
    private void clearFields() {
        idTextField.clear();
        passwordTextField.clear();
    }

    /**
     * Displays an error alert with the specified title and message.
     *
     * @param title   the title of the error alert
     * @param message the message displayed in the error alert
     */
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(SceneManager.getStage());
        alert.showAndWait();
    }

    /**
     * Logs out the current user and navigates to the Home Page.
     */
    @FXML
    private void logout() {
        subscriberController.attemptLogOut();
    }

    @FXML
    private void goToDashboard() {
        SceneManager.switchScene("/gui/user/subscriberUI/SubscriberUI_UI.fxml", "BLib.4 - Braude Library Management");
    }

    /**
     * Navigates to the Search screen.
     */
    @FXML
    private void goToSearch() {
        SceneManager.switchScene("/gui/common/search/Search_UI.fxml", "BLib.4 - Braude Library Management");
    }

    @FXML
    private void goToViewHistory() {
        SceneManager.switchScene("/gui/user/viewHistory/ViewHistory_UI.fxml", "BLib.4 - Braude Library Management");
    }

    /**
     * Exits the application by quitting the client connection.
     */
    @FXML
    private void exitApp() {
        ClientUI.chat.getClient().quit();
    }
}