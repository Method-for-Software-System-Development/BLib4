package gui.common.viewOrderBook;

import entities.book.Book;
import entities.logic.MessageType;
import entities.user.Librarian;
import entities.user.Subscriber;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import logic.communication.DataReceiver;
import logic.user.BooksController;
import logic.user.Subscriber_Controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for the Search Page (Search_UI.fxml).
 * This class is responsible for handling user interactions on the search page,
 * including scanning a reader card, login functionality, and searching for books by various parameters.
 */
public class ViewOrderBook_Controller implements DataReceiver {

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

    @FXML
    private HBox viewBookHBox;

    @FXML
    private HBox availabilityHBox;

    /**
     * The singleton controller for handling subscriber-related logic.
     */
    private Subscriber_Controller subscriberController;

    private BooksController booksController;

    /**
     * Indicates if a Subscriber is currently logged in.
     */
    private boolean isSubscriberLoggedIn;

    /**
     * Indicates if a Librarian is currently logged in.
     */
    private boolean isLibrarianLoggedIn;

    private Book book;
    private List<String> availability;
    private boolean canOrder;

    @Override
    public void receiveData(Object data) {
        if (data instanceof Book) {
            this.book = (Book) data;
            showBookDetails(book);
        }
    }

    /**
     * Initializes the Search_Controller.
     * Sets up event handlers for hover effects on buttons, determines if the user is logged in,
     * and adjusts the UI accordingly.
     */
    @FXML
    public void initialize() {
        // Get the singleton instance of Subscriber_Controller
        subscriberController = Subscriber_Controller.getInstance();
        // Get the singleton instance of BooksController
        booksController = BooksController.getInstance();

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

    private void showBookDetails(Book book)
    {
        GridPane bookGrid = new GridPane();
        bookGrid.getStyleClass().add("bookContainer"); // CSS class for book container
        bookGrid.setHgap(20); // Horizontal gap between elements
        bookGrid.setVgap(10); // Vertical gap between elements

        // Set GridPane to fill the width of the HBox
        bookGrid.setMaxWidth(Double.MAX_VALUE);

        // Ensure HBox children grow
        HBox.setHgrow(bookGrid, Priority.ALWAYS);

        // Check if the book has an image
        if (book.getImage() != null) {
            // Create ImageView for the book cover
            ImageView bookImageView = new ImageView(book.getImage());
            bookImageView.setFitHeight(480);
            bookImageView.setFitWidth(348);

            // Add rounded corners to the image
            Rectangle clip = new Rectangle(348, 480);
            clip.setArcWidth(15);
            clip.setArcHeight(15);
            bookImageView.setClip(clip);

            GridPane.setConstraints(bookImageView, 0, 0);
            GridPane.setRowSpan(bookImageView, 6);
            bookGrid.getChildren().add(bookImageView);

        } else {
            // Create a placeholder rectangle styled with CSS
            Rectangle placeholder = new Rectangle(348, 480);
            placeholder.setArcWidth(15);
            placeholder.setArcHeight(15);
            placeholder.getStyleClass().add("book-placeholder");
            Text placeholderText = new Text(book.getTitle());
            placeholderText.getStyleClass().add("small-grey-text");
            placeholderText.setWrappingWidth(308);
            placeholderText.setTextAlignment(TextAlignment.CENTER);
            GridPane.setConstraints(placeholder, 0, 0);
            GridPane.setRowSpan(placeholder, 6);
            bookGrid.getChildren().addAll(placeholder, placeholderText);
        }

        // Create Text for the book title with automatic wrapping
        Text bookTitle = new Text(book.getTitle());
        bookTitle.getStyleClass().add("book-title");
        GridPane.setConstraints(bookTitle, 1, 0);

        // Create Text for the book author
        Text bookAuthor = new Text("Author:  " + book.getAuthor());
        bookAuthor.getStyleClass().add("medium-bold-grey-text");
        GridPane.setConstraints(bookAuthor, 1, 1);
        GridPane.setMargin(bookAuthor, new Insets(0, 0, 25, 0));

        // Create Text for the subject title
        Text subjectTitle = new Text("Subject:  ");
        subjectTitle.getStyleClass().add("small-bold-grey-text");
        // Create Text for the book subject
        Text bookSubject = new Text(book.getSubject());
        bookSubject.getStyleClass().add("small-grey-text");
        // Combine the title and subject into an HBox
        HBox subjectHBox = new HBox();
        subjectHBox.getChildren().addAll(subjectTitle, bookSubject);
        // Add the HBox to the GridPane
        GridPane.setConstraints(subjectHBox, 1, 2);

        // Create Text for the edition title
        Text editionTitle = new Text("Edition Number:  ");
        editionTitle.getStyleClass().add("small-bold-grey-text");
        // Create Text for the book edition number
        Text bookEdition = new Text(String.valueOf(book.getEditionNum()));
        bookEdition.getStyleClass().add("small-grey-text");
        // Combine the title and edition into an HBox
        HBox editionHBox = new HBox();
        editionHBox.getChildren().addAll(editionTitle, bookEdition);
        // Add the HBox to the GridPane
        GridPane.setConstraints(editionHBox, 1, 3);

        // Create Text for the print date title
        Text printDateTitle = new Text("Print Date:  ");
        printDateTitle.getStyleClass().add("small-bold-grey-text");
        // Format the Date object to a String
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); // Adjust format as needed
        String formattedDate = dateFormat.format(book.getPrintDate());
        Text bookPrintDate = new Text(formattedDate);
        bookPrintDate.getStyleClass().add("small-grey-text");
        // Combine the title and date into an HBox
        HBox printDateHBox = new HBox();
        printDateHBox.getChildren().addAll(printDateTitle, bookPrintDate);
        GridPane.setConstraints(printDateHBox, 1, 4);

        // Create Text for the description title
        Text descriptionTitle = new Text("Description:");
        descriptionTitle.getStyleClass().add("small-bold-grey-text");
        // Create Text for the book description
        Text bookDescription = new Text(book.getDescription());
        bookDescription.getStyleClass().add("small-grey-text");
        bookDescription.setWrappingWidth(950);
        // Combine the title and description into an HBox
        VBox descriptionVBox = new VBox();
        descriptionVBox.getChildren().addAll(descriptionTitle, bookDescription);
        // Add the HBox to the GridPane
        GridPane.setConstraints(descriptionVBox, 1, 5);

        Text availabilityTitle = new Text();
        availabilityTitle.getStyleClass().add("small-bold-grey-text");
        Text locationOrDateTitle = new Text();
        locationOrDateTitle.getStyleClass().add("small-bold-purple-text");

        // *** TEMPORARY FOR GUI TESTING ***
        availabilityTitle.setText("The book is currently not available for borrowing. It's expected to return on:  ");
        locationOrDateTitle.setText("14/01/2025");

//        // *** NOT WORKING ***
//
//        // Get the availability of the book
//        ClientUI.chat.accept(new MessageType("123", book.getBookId()));
//        availability = ChatClient.availability;
//        // Check if the book is available for borrowing
//        if (availability.get(0).equals("0"))
//        {
//            availabilityTitle.setText("The book is available for borrowing in the library. It's shelf location is:  ");
//            locationOrDateTitle.setText(availability.get(1));
//        }
//        else
//        {
//            availabilityTitle.setText("The book is currently not available for borrowing. It's expected to return on:  ");
//            locationOrDateTitle.setText(availability.get(1));
//        }

        availabilityHBox.getChildren().addAll(availabilityTitle, locationOrDateTitle);

//        // *** NOT WORKING ***
//
//        // Check if the user can order the book
//        ClientUI.chat.accept(new MessageType("124", book.getBookId()));
//        canOrder = ChatClient.serverResponse;

        // *** TEMPORARY FOR GUI TESTING ***
        canOrder = true;

        if (canOrder)
        {
            // Create a spacer to push the order button to the right
            Region orderButtonSpacer = new Region();
            HBox.setHgrow(orderButtonSpacer, Priority.ALWAYS);
            // Create a Button to open the order dialog
            Button openOrderButton = new Button("Order");
            openOrderButton.getStyleClass().add("orange-orange-button");
            openOrderButton.setPrefHeight(50.0);
            openOrderButton.setPrefWidth(150.0);
            availabilityHBox.getChildren().addAll(orderButtonSpacer, openOrderButton);

            openOrderButton.setOnAction(event -> {
                if (isSubscriberLoggedIn)
                {
//                    // *** NOT WORKING ***
//                    int result = booksController.addToWaitlist(book.getBookId(), subscriberController.getLoggedSubscriber().getId());

                    // *** TEMPORARY FOR GUI TESTING ***
                    int result = 1;

                    if(result == 1)
                    {
                        showInformationAlert("Order Book", "The book has been successfully ordered.");
                    }
                    else if (result == 2)
                    {
                        showErrorAlert("Order Book", "The book is already in your order list.");
                    }
                    else if (result == 3)
                    {
                        showErrorAlert("Order Book", "The book cannot be ordered at this time due to a high number of reservations exceeding the number of the book copies in the library. Please try again later.");
                    }
                }
                else
                {
                    showErrorAlert("Order Book", "You must be logged in to order a book.");
                }
            });
        }

        // Add all elements to the GridPane
        bookGrid.getChildren().addAll(bookTitle, bookAuthor, subjectHBox, editionHBox, printDateHBox, descriptionVBox);

        // Add the GridPane to the target HBox
        viewBookHBox.getChildren().add(bookGrid);
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

    private void showInformationAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
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
