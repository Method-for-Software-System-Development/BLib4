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

    // FXML fields for seeIfLibrarianLoggedIn
    @FXML
    private VBox seeIfLibrarianLoggedIn;
    @FXML
    private Text userGreeting2;
    @FXML
    private Button librarianDashButton;
    @FXML
    private ImageView librarianDashImageView;
    @FXML
    private Button newBorrowButton;
    @FXML
    private ImageView newBorrowImageView;
    @FXML
    private Button addSubscriberButton;
    @FXML
    private ImageView addSubscriberImageView;
    @FXML
    private Button reportsButton;
    @FXML
    private ImageView reportsImageView;
    @FXML
    private Button logoutButton2;
    @FXML
    private ImageView logoutImageView2;

    // FXML fields for both
    @FXML
    private Button homePageButton;
    @FXML
    private ImageView homePageImageView;
    @FXML
    private Button exitButton;
    @FXML
    private ImageView exitImageView;
    @FXML
    private TextField titleTextField;
    @FXML
    private TextField subjectTextField;
    @FXML
    private TextField freeTextField;
    @FXML
    private VBox seeIfFoundBooks;
    @FXML
    private HBox searchResultsHBox;

    // Fields
    private Subscriber_Controller subscriberController;
    private boolean isSubscriberLoggedIn;
    private boolean isLibrarianLoggedIn;
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

        // Check if a Subscriber or Librarian is logged in
        isSubscriberLoggedIn = subscriberController.getLoggedSubscriber() != null;
        isLibrarianLoggedIn = subscriberController.getLoggedLibrarian() != null;

        // Show the appropriate VBox based on login status
        if (isSubscriberLoggedIn) {
            seeIfNotLoggedIn.setVisible(false);
            seeIfNotLoggedIn.setManaged(false);
            seeIfLoggedIn.setVisible(true);
            seeIfLoggedIn.setManaged(true);
            seeIfLibrarianLoggedIn.setVisible(false);
            seeIfLibrarianLoggedIn.setManaged(false);
            userGreeting.setText(getGreetingMessage() + " " + subscriberController.getLoggedSubscriber().getFirstName() + " !");
        } else if (isLibrarianLoggedIn) {
            seeIfNotLoggedIn.setVisible(false);
            seeIfNotLoggedIn.setManaged(false);
            seeIfLoggedIn.setVisible(false);
            seeIfLoggedIn.setManaged(false);
            seeIfLibrarianLoggedIn.setVisible(true);
            seeIfLibrarianLoggedIn.setManaged(true);
            userGreeting2.setText(getGreetingMessage() + " " + subscriberController.getLoggedLibrarian().getName() + " !");
        } else {
            seeIfNotLoggedIn.setVisible(true);
            seeIfNotLoggedIn.setManaged(true);
            seeIfLoggedIn.setVisible(false);
            seeIfLoggedIn.setManaged(false);
            seeIfLibrarianLoggedIn.setVisible(false);
            seeIfLibrarianLoggedIn.setManaged(false);
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

        dashboardButton.setOnMouseEntered(event -> {
            dashboardImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/person_24dp_F86F03.png")));
        });
        dashboardButton.setOnMouseExited(event -> {
            dashboardImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/person_24dp_FFFFFF.png")));
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

        librarianDashButton.setOnMouseEntered(event -> {
            librarianDashImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/local_library_24dp_F86F03.png")));
        });
        librarianDashButton.setOnMouseExited(event -> {
            librarianDashImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/local_library_24dp_FFFFFF.png")));
        });

        newBorrowButton.setOnMouseEntered(event -> {
            newBorrowImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/post_add_24dp_525FE1.png")));
        });
        newBorrowButton.setOnMouseExited(event -> {
            newBorrowImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/post_add_24dp_FFFFFF.png")));
        });

        addSubscriberButton.setOnMouseEntered(event -> {
            addSubscriberImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/person_add_24dp_525FE1.png")));
        });
        addSubscriberButton.setOnMouseExited(event -> {
            addSubscriberImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/person_add_24dp_FFFFFF.png")));
        });

        reportsButton.setOnMouseEntered(event -> {
            reportsImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/bar_chart_24dp_525FE1.png")));
        });
        reportsButton.setOnMouseExited(event -> {
            reportsImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/bar_chart_24dp_FFFFFF.png")));
        });

        logoutButton2.setOnMouseEntered(event -> {
            logoutImageView2.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/logout_24dp_525FE1.png")));
        });
        logoutButton2.setOnMouseExited(event -> {
            logoutImageView2.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/logout_24dp_FFFFFF.png")));
        });

        homePageButton.setOnMouseEntered(event -> {
            homePageImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/home_24dp_525FE1.png")));
        });
        homePageButton.setOnMouseExited(event -> {
            homePageImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/home_24dp_FFFFFF.png")));
        });

        exitButton.setOnMouseEntered(event -> {
            exitImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/close_24dp_525FE1.png")));
        });
        exitButton.setOnMouseExited(event -> {
            exitImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/close_24dp_FFFFFF.png")));
        });
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
                toServer.add("category");
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

    @FXML
    private void validate_scanReaderCard_login() {
        // Create a custom Alert
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.initOwner(SceneManager.getStage());
        alert.setTitle("Scan Reader Card (Simulation)");
        alert.setHeaderText("Enter a reader card number to simulate scanning a card");

        // Create a TextField for input
        TextField inputField = new TextField();
        inputField.setPromptText("Enter reader card number (equivalent to User ID)");

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
                    Subscriber subscriber = subscriberController.attemptSubscriberLogInByCard(enteredId);
                    if (subscriber != null) {
                        SceneManager.switchScene("/gui/user/subscriberUI/SubscriberUI_UI.fxml", "BLib.4 - Braude Library Management");
                    } else {
                        showErrorAlert("Login By Reader Card Failed", "The account was not found. Please scan again. If the problem persists, please contact the librarian.");
                    }
                } else {
                    showErrorAlert("Invalid Input", "Please enter a valid User ID.");
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
     * Logs out the current user and navigates to the Home Page.
     */
    @FXML
    private void logout() {
        subscriberController.attemptLogOut();
    }

    @FXML
    private void goToDashboard() {
        if (isSubscriberLoggedIn) {
            SceneManager.switchScene("/gui/user/subscriberUI/SubscriberUI_UI.fxml", "BLib.4 - Braude Library Management");
        } else if (isLibrarianLoggedIn) {
            SceneManager.switchScene("/gui/librarian/librarianUI/LibrarianUI_UI.fxml", "BLib.4 - Braude Library Management");
        }
    }

    @FXML
    private void goToViewHistory() {
        SceneManager.switchScene("/gui/user/viewHistory/ViewHistory_UI.fxml", "BLib.4 - Braude Library Management");
    }

    @FXML
    private void goToEditProfile() {
        SceneManager.switchScene("/gui/user/editProfile/EditProfile_UI.fxml", "BLib.4 - Braude Library Management");
    }

    @FXML
    private void goToLibrarianDash() {
        SceneManager.switchScene("/gui/librarian/librarianUI/LibrarianUI_UI.fxml", "BLib.4 - Braude Library Management");
    }

    @FXML
    private void goToNewBorrow() {
        SceneManager.switchScene("/gui/librarian/newBorrow/NewBorrow_UI.fxml", "BLib.4 - Braude Library Management");
    }

    @FXML
    private void goToAddSubscriber() {
        SceneManager.switchScene("/gui/librarian/addSubscriber/AddSubscriber_UI.fxml", "BLib.4 - Braude Library Management");
    }

    @FXML
    private void goToLibraryReports() {
        SceneManager.switchScene("/gui/librarian/libraryReports/LibraryReports_UI.fxml", "BLib.4 - Braude Library Management");
    }

    @FXML
    private void goToHomePage() {
        SceneManager.switchScene("/gui/common/homePage/HomePage_UI.fxml", "BLib.4 - Braude Library Management");
    }

    /**
     * Exits the application by quitting the client connection.
     */
    @FXML
    private void exitApp() {
        ClientUI.chat.getClient().quit();
    }
}