package gui.common.search;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for the Search Page (Search_UI.fxml).
 * This class is responsible for handling user interactions on the search page,
 * including scanning a reader card, login functionality, and searching for books by various parameters.
 */
public class Search_Controller {

    /**
     * A VBox that is shown if the user is not logged in (Subscriber/Librarian).
     */
    @FXML
    private VBox seeIfNotLoggedIn;

    /**
     * A RadioButton used to indicate a Subscriber account type.
     */
    @FXML
    private RadioButton subscriberRadioButton;

    /**
     * A RadioButton used to indicate a Librarian account type.
     */
    @FXML
    private RadioButton librarianRadioButton;

    /**
     * The ToggleGroup that holds the account type radio buttons (Subscriber or Librarian).
     */
    @FXML
    private ToggleGroup accountTypeGroup;

    /**
     * A TextField for entering the user ID.
     */
    @FXML
    private TextField idTextField;

    /**
     * A TextField for entering the user password.
     */
    @FXML
    private TextField passwordTextField;

    /**
     * A Button that triggers scanning of a reader card.
     */
    @FXML
    private Button scanReaderCardButton;

    /**
     * The ImageView associated with the scanReaderCardButton.
     */
    @FXML
    private ImageView scanReaderCardImageView;

    /**
     * The Button used to submit the login form.
     */
    @FXML
    private Button loginButton;

    /**
     * The ImageView associated with the loginButton.
     */
    @FXML
    private ImageView loginImageView;

    /**
     * A Button used to navigate back from the search page to the relevant page based on the user's login status.
     */
    @FXML
    private Button backButton;

    /**
     * The ImageView associated with the backButton.
     */
    @FXML
    private ImageView backImageView;

    /**
     * A Button used to exit the application.
     */
    @FXML
    private Button exitButton;

    /**
     * The ImageView associated with the exitButton.
     */
    @FXML
    private ImageView exitImageView;

    /**
     * A TextField for entering the title of the book to search for.
     */
    @FXML
    private TextField titleTextField;

    /**
     * A TextField for entering the subject of the book to search for.
     */
    @FXML
    private TextField subjectTextField;

    /**
     * A TextField for entering any free text to search for.
     */
    @FXML
    private TextField freeTextField;

    /**
     * A VBox that is shown if any books are found by the search query.
     */
    @FXML
    private VBox seeIfFoundBooks;

    /**
     * An HBox that displays the results of the search query.
     */
    @FXML
    private HBox searchResultsHBox;

    /**
     * The singleton controller for handling subscriber-related logic.
     */
    private Subscriber_Controller subscriberController;

    /**
     * Indicates if a Subscriber is currently logged in.
     */
    private boolean isSubscriberLoggedIn;

    /**
     * Indicates if a Librarian is currently logged in.
     */
    private boolean isLibrarianLoggedIn;

    /**
     * A list of Book entities that match the user's search criteria.
     */
    private List<Book> searchResults;

    /**
     * Initializes the Search_Controller.
     * Sets up event handlers for hover effects on buttons, determines if the user is logged in,
     * and adjusts the UI accordingly.
     */
    @FXML
    public void initialize() {
        // Get the singleton instance of Subscriber_Controller
        subscriberController = Subscriber_Controller.getInstance();

        isSubscriberLoggedIn = subscriberController.getLoggedSubscriber() != null;
        isLibrarianLoggedIn = subscriberController.getLoggedLibrarian() != null;

        if (isSubscriberLoggedIn || isLibrarianLoggedIn) {
            seeIfNotLoggedIn.setVisible(false);
            backButton.setText("Back to Dashboard");
        }

        accountTypeGroup = new ToggleGroup();
        subscriberRadioButton.setToggleGroup(accountTypeGroup);
        librarianRadioButton.setToggleGroup(accountTypeGroup);
        subscriberRadioButton.setSelected(true);

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

        backButton.setOnMouseEntered(event -> {
            backImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/back_24dp_525FE1.png")));
        });

        backButton.setOnMouseExited(event -> {
            backImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/back_24dp_FFFFFF.png")));
        });

        exitButton.setOnMouseEntered(event -> {
            exitImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/close_24dp_525FE1.png")));
        });

        exitButton.setOnMouseExited(event -> {
            exitImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/close_24dp_FFFFFF.png")));
        });
    }

    /**
     * Validates the search form fields, ensuring only one field is filled out.
     * If valid, sends a search request to the server and displays any matching books.
     * If no results are found, shows an error alert.
     */
    @FXML
    public void validate_search_form() {
        // Store values in variables
        String title = "";
        if (titleTextField != null && titleTextField.getText() != null) {
            title = titleTextField.getText().trim();
        }

        String subject = "";
        if (subjectTextField != null && subjectTextField.getText() != null) {
            subject = subjectTextField.getText().trim();
        }

        String freeText = "";
        if (freeTextField != null && freeTextField.getText() != null) {
            freeText = freeTextField.getText().trim();
        }

        // Count how many fields are filled
        int filledFields = 0;
        if (!title.isEmpty()) filledFields++;
        if (!subject.isEmpty()) filledFields++;
        if (!freeText.isEmpty()) filledFields++;

        // Validate the form
        if (filledFields == 0) {
            // No fields filled
            showErrorAlert("Validation Error", "Please fill in at least one field for the search.");
        } else if (filledFields > 1) {
            // More than one field filled
            showErrorAlert("Validation Error", "Please fill in only one field for the search.");
        } else {
            // Exactly one field filled - proceed with the search
            List<String> toServer = new ArrayList<>();

            if (!title.isEmpty()) {
                toServer.add("name");
                toServer.add(title);
            } else if (!subject.isEmpty()) {
                toServer.add("subject");
                toServer.add(subject);
            } else if (!freeText.isEmpty()) {
                toServer.add("freeText");
                toServer.add(freeText);
            }

            // Send the search request to the server
            ClientUI.chat.accept(new MessageType("105", toServer));
            searchResults = ChatClient.books;

            // Check if any books were found
            if (searchResults != null && !searchResults.isEmpty()) {
                populateBooksHBox(searchResultsHBox, searchResults); // Populate the HBox with the search results
                seeIfFoundBooks.setVisible(true); // Show the HBox
            } else {
                seeIfFoundBooks.setVisible(false); // Hide the HBox
                showErrorAlert("No Results", "No books were found matching the search criteria."); // Show an error alert
            }
        }
    }

    /**
     * Clears the search form by resetting the text fields (title, subject, free text).
     */
    @FXML
    public void clear_form() {
        titleTextField.clear();
        subjectTextField.clear();
        freeTextField.clear();
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
            bookGrid.setPrefWidth(438);
            bookGrid.setPrefHeight(285);

            // Check if the book has an image
            if (book.getImage() != null) {
                // Create ImageView for the book cover
                ImageView bookImageView = new ImageView(book.getImage());
                bookImageView.setFitHeight(255);
                bookImageView.setFitWidth(185);

                // Add rounded corners to the image
                Rectangle clip = new Rectangle(185, 255);
                clip.setArcWidth(15);
                clip.setArcHeight(15);
                bookImageView.setClip(clip);

                GridPane.setConstraints(bookImageView, 0, 0);
                GridPane.setRowSpan(bookImageView, 5);
                bookGrid.getChildren().add(bookImageView);

            } else {
                // Create a placeholder rectangle styled with CSS
                Rectangle placeholder = new Rectangle(185, 255);
                placeholder.setArcWidth(15);
                placeholder.setArcHeight(15);
                placeholder.getStyleClass().add("book-placeholder");
                Text placeholderText = new Text(book.getTitle());
                placeholderText.getStyleClass().add("small-grey-text");
                placeholderText.setWrappingWidth(165);
                placeholderText.setTextAlignment(TextAlignment.CENTER);
                GridPane.setConstraints(placeholder, 0, 0);
                GridPane.setRowSpan(placeholder, 5);
                bookGrid.getChildren().addAll(placeholder, placeholderText);
            }

            // Create Text for the book title with automatic wrapping
            Text bookTitle = new Text(book.getTitle());
            bookTitle.getStyleClass().add("medium-bold-purple-text");
            bookTitle.setWrappingWidth(213);
            GridPane.setConstraints(bookTitle, 1, 0);

            // Create Text for the book author
            Text bookAuthor = new Text(book.getAuthor());
            bookAuthor.getStyleClass().add("small-bold-grey-text");
            bookAuthor.setWrappingWidth(213);
            GridPane.setConstraints(bookAuthor, 1, 1);

            // Create Text for the book subject
            Text bookSubject = new Text(book.getSubject());
            bookSubject.getStyleClass().add("small-grey-text");
            bookSubject.setWrappingWidth(213);
            GridPane.setConstraints(bookSubject, 1, 2);

            // Add a spacer region above the button
            Region spacer = new Region();
            GridPane.setConstraints(spacer, 1, 3);
            GridPane.setVgrow(spacer, Priority.ALWAYS);

            // Create the View Book button
            Button viewBookButton = new Button("View Book");
            viewBookButton.getStyleClass().add("purple-transparent-button");
            viewBookButton.setPrefHeight(50.0);
            viewBookButton.setMaxWidth(Double.MAX_VALUE);
            GridPane.setConstraints(viewBookButton, 1, 4);

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
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Scan Reader Card");
        alert.setHeaderText("Enter the Reader Card ID");

        TextField inputField = new TextField();
        inputField.setPromptText("Enter ID");

        VBox content = new VBox();
        content.setSpacing(10);
        content.getChildren().add(inputField);
        alert.getDialogPane().setContent(content);

        ButtonType confirmButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().addAll(confirmButton, cancelButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == confirmButton) {
                String enteredId = inputField.getText();

                if (enteredId != null && !enteredId.trim().isEmpty()) {
                    idTextField.setText(enteredId);
                    passwordTextField.setText("readerCard");
                } else {
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
        idTextField.getStyleClass().remove("error-text-field");
        passwordTextField.getStyleClass().remove("error-text-field");

        boolean isValid = true;

        String userID = idTextField.getText();
        String password = passwordTextField.getText();
        RadioButton selectedRadioButton = (RadioButton) accountTypeGroup.getSelectedToggle();

        if (selectedRadioButton == null) {
            isValid = false;
        }

        if (userID == null || userID.trim().isEmpty()) {
            idTextField.setPromptText("ID is required");
            idTextField.getStyleClass().add("error-text-field");
            isValid = false;
        }

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
     * Switches the scene to the appropriate UI based on the user's login status:
     * <ul>
     *     <li>If a Subscriber is logged in, navigates to the Subscriber UI.</li>
     *     <li>If a Librarian is logged in, navigates to the Librarian UI.</li>
     *     <li>Otherwise, navigates back to the Home Page.</li>
     * </ul>
     */
    @FXML
    private void goBack() {
        if (isSubscriberLoggedIn) {
            SceneManager.switchScene("/gui/user/subscriberUI/SubscriberUI_UI.fxml", "BLib.4 - Braude Library Management");
        } else if (isLibrarianLoggedIn) {
            SceneManager.switchScene("/gui/librarian/librarianUI/LibrarianUI_UI.fxml", "BLib.4 - Braude Library Management");
        } else {
            SceneManager.switchScene("/gui/common/homePage/HomePage_UI.fxml", "BLib.4 - Braude Library Management");
        }
    }

    /**
     * Exits the application by quitting the client connection.
     */
    @FXML
    private void exitApp() {
        ClientUI.chat.getClient().quit();
    }
}