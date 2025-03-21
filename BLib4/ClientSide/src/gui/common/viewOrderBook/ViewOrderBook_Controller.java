package gui.common.viewOrderBook;

import entities.book.Book;
import entities.logic.MessageType;
import entities.user.Librarian;
import entities.user.Subscriber;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import logic.communication.ChatClient;
import logic.communication.ClientUI;
import logic.communication.DataReceiver;
import logic.communication.SceneManager;
import logic.user.BooksController;
import logic.user.Subscriber_Controller;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Controller class for the Search Page (Search_UI.fxml).
 * This class is responsible for handling user interactions on the search page,
 * including scanning a reader card, login functionality, and searching for books by various parameters.
 */
public class ViewOrderBook_Controller implements DataReceiver
{
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
    private Button searchBooksButton3;
    @FXML
    private ImageView searchBooksImageView3;
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
    private HBox viewBookHBox;
    @FXML
    private HBox availabilityHBox;

    // Fields
    private Subscriber_Controller subscriberController;
    private BooksController booksController;
    private boolean isSubscriberLoggedIn;
    private boolean isLibrarianLoggedIn;
    private Book book;
    private List<String> availability;
    private boolean canOrder;

    /**
     * Receives data from the server and processes it accordingly.
     *
     * @param data the data received from the server
     */
    @Override
    public void receiveData(Object data)
    {
        if (data instanceof Book)
        {
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
    public void initialize()
    {
        // Get the singleton instance of Subscriber_Controller
        subscriberController = Subscriber_Controller.getInstance();
        // Get the singleton instance of BooksController
        booksController = BooksController.getInstance();

        // Check if a Subscriber or Librarian is logged in
        isSubscriberLoggedIn = subscriberController.getLoggedSubscriber() != null;
        isLibrarianLoggedIn = subscriberController.getLoggedLibrarian() != null;

        // Show the appropriate VBox based on login status
        if (isSubscriberLoggedIn)
        {
            seeIfNotLoggedIn.setVisible(false);
            seeIfNotLoggedIn.setManaged(false);
            seeIfLoggedIn.setVisible(true);
            seeIfLoggedIn.setManaged(true);
            seeIfLibrarianLoggedIn.setVisible(false);
            seeIfLibrarianLoggedIn.setManaged(false);
            userGreeting.setText(getGreetingMessage() + " " + subscriberController.getLoggedSubscriber().getFirstName() + " !");
        }
        else if (isLibrarianLoggedIn)
        {
            seeIfNotLoggedIn.setVisible(false);
            seeIfNotLoggedIn.setManaged(false);
            seeIfLoggedIn.setVisible(false);
            seeIfLoggedIn.setManaged(false);
            seeIfLibrarianLoggedIn.setVisible(true);
            seeIfLibrarianLoggedIn.setManaged(true);
            userGreeting2.setText(getGreetingMessage() + " " + subscriberController.getLoggedLibrarian().getName() + " !");
        }
        else
        {
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
        scanReaderCardButton.setOnMouseEntered(event ->
        {
            scanReaderCardImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/qr_code_scanner_24dp_525FE1.png")));
        });
        scanReaderCardButton.setOnMouseExited(event ->
        {
            scanReaderCardImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/qr_code_scanner_24dp_FFFFFF.png")));
        });

        loginButton.setOnMouseEntered(event ->
        {
            loginImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/login_24dp_F86F03.png")));
        });
        loginButton.setOnMouseExited(event ->
        {
            loginImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/login_24dp_FFFFFF.png")));
        });

        searchBooksButton.setOnMouseEntered(event ->
        {
            searchBooksImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/search_24dp_525FE1.png")));
        });
        searchBooksButton.setOnMouseExited(event ->
        {
            searchBooksImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/search_24dp_FFFFFF.png")));
        });

        dashboardButton.setOnMouseEntered(event ->
        {
            dashboardImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/person_24dp_F86F03.png")));
        });
        dashboardButton.setOnMouseExited(event ->
        {
            dashboardImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/person_24dp_FFFFFF.png")));
        });

        searchBooksButton2.setOnMouseEntered(event ->
        {
            searchBooksImageView2.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/search_24dp_525FE1.png")));
        });
        searchBooksButton2.setOnMouseExited(event ->
        {
            searchBooksImageView2.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/search_24dp_FFFFFF.png")));
        });

        viewHistoryButton.setOnMouseEntered(event ->
        {
            viewHistoryImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/history_24dp_525FE1.png")));
        });
        viewHistoryButton.setOnMouseExited(event ->
        {
            viewHistoryImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/history_24dp_FFFFFF.png")));
        });

        editProfileButton.setOnMouseEntered(event ->
        {
            editProfileImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/edit_24dp_525FE1.png")));
        });
        editProfileButton.setOnMouseExited(event ->
        {
            editProfileImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/edit_24dp_FFFFFF.png")));
        });

        logoutButton.setOnMouseEntered(event ->
        {
            logoutImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/logout_24dp_525FE1.png")));
        });
        logoutButton.setOnMouseExited(event ->
        {
            logoutImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/logout_24dp_FFFFFF.png")));
        });

        librarianDashButton.setOnMouseEntered(event ->
        {
            librarianDashImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/local_library_24dp_F86F03.png")));
        });
        librarianDashButton.setOnMouseExited(event ->
        {
            librarianDashImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/local_library_24dp_FFFFFF.png")));
        });

        searchBooksButton3.setOnMouseEntered(event ->
        {
            searchBooksImageView3.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/search_24dp_525FE1.png")));
        });
        searchBooksButton3.setOnMouseExited(event ->
        {
            searchBooksImageView3.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/search_24dp_FFFFFF.png")));
        });

        newBorrowButton.setOnMouseEntered(event ->
        {
            newBorrowImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/post_add_24dp_525FE1.png")));
        });
        newBorrowButton.setOnMouseExited(event ->
        {
            newBorrowImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/post_add_24dp_FFFFFF.png")));
        });

        addSubscriberButton.setOnMouseEntered(event ->
        {
            addSubscriberImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/person_add_24dp_525FE1.png")));
        });
        addSubscriberButton.setOnMouseExited(event ->
        {
            addSubscriberImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/person_add_24dp_FFFFFF.png")));
        });

        reportsButton.setOnMouseEntered(event ->
        {
            reportsImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/bar_chart_24dp_525FE1.png")));
        });
        reportsButton.setOnMouseExited(event ->
        {
            reportsImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/bar_chart_24dp_FFFFFF.png")));
        });

        logoutButton2.setOnMouseEntered(event ->
        {
            logoutImageView2.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/logout_24dp_525FE1.png")));
        });
        logoutButton2.setOnMouseExited(event ->
        {
            logoutImageView2.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/logout_24dp_FFFFFF.png")));
        });

        homePageButton.setOnMouseEntered(event ->
        {
            homePageImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/home_24dp_525FE1.png")));
        });
        homePageButton.setOnMouseExited(event ->
        {
            homePageImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/home_24dp_FFFFFF.png")));
        });

        exitButton.setOnMouseEntered(event ->
        {
            exitImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/close_24dp_525FE1.png")));
        });
        exitButton.setOnMouseExited(event ->
        {
            exitImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/close_24dp_FFFFFF.png")));
        });
    }

    /**
     * Returns a greeting message based on the current time of day.
     *
     * @return the greeting message
     */
    private String getGreetingMessage()
    {
        int hour = java.time.LocalTime.now().getHour();

        if (hour >= 5 && hour < 12)
        {
            return "Good Morning";
        }
        else if (hour >= 12 && hour < 17)
        {
            return "Good Afternoon";
        }
        else if (hour >= 17 && hour < 21)
        {
            return "Good Evening";
        }
        else
        {
            return "Good Night";
        }
    }

    /**
     * The method is called when the user clicks on the show details button.
     *
     * @param book The book to show the details of.
     */
    private void showBookDetails(Book book)
    {
        GridPane bookGrid = new GridPane();
        bookGrid.getStyleClass().add("bookContainer"); // CSS class for book container
        bookGrid.setHgap(20); // Horizontal gap between elements
        bookGrid.setVgap(10); // Vertical gap between elements

        // Set GridPane to fill the width with the HBox
        bookGrid.setMaxWidth(Double.MAX_VALUE);

        // Ensure HBox children grow
        HBox.setHgrow(bookGrid, Priority.ALWAYS);

        // Check if the book has an image
        if (book.getImage() != null)
        {
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

        }
        else
        {
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); // Adjust a format as needed
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

        // Get the availability of the book
        ClientUI.chat.accept(new MessageType("123", String.valueOf(book.getBookId())));
        availability = ChatClient.availability;
        // Check if the book is available for borrowing
        if (availability.get(0).equals("Library"))
        {
            availabilityTitle.setText("The book is available for borrowing in the library. It's shelf location is:  ");
            locationOrDateTitle.setText(availability.get(1));
        }
        else
        {
            availabilityTitle.setText("The book is currently not available for borrowing. It's expected to return on:  ");
            locationOrDateTitle.setText(availability.get(1));
        }

        availabilityHBox.getChildren().addAll(availabilityTitle, locationOrDateTitle);

        // Check if the user can order the book
        ClientUI.chat.accept(new MessageType("124", String.valueOf(book.getBookId())));
        canOrder = ChatClient.serverResponse;

        if (canOrder && isSubscriberLoggedIn)
        { // only users can order books
            // to Create a spacer to push the order button to the right
            Region orderButtonSpacer = new Region();
            HBox.setHgrow(orderButtonSpacer, Priority.ALWAYS);
            // Create a Button to open the order dialog
            Button openOrderButton = new Button("Order");
            openOrderButton.getStyleClass().add("orange-orange-button");
            openOrderButton.setPrefHeight(50.0);
            openOrderButton.setPrefWidth(150.0);
            availabilityHBox.getChildren().addAll(orderButtonSpacer, openOrderButton);

            openOrderButton.setOnAction(event ->
            {
                if (isSubscriberLoggedIn)
                {
                    int result = booksController.addToWaitlist(String.valueOf(book.getBookId()), subscriberController.getLoggedSubscriber().getId());

                    if (result == 1)
                    {
                        showInformationAlert("Order Book", "The book has been successfully ordered."); // TRUE FALSE FALSE
                    }
                    else if (result == 2)
                    {
                        showErrorAlert("Order Book", "The book cannot be ordered at this time due to a high number of reservations exceeding the number of the book copies in the library. Please try again later."); // FALSE FALSE FALSE
                    }
                    else if (result == 3)
                    {
                        showErrorAlert("Order Book", "The book cant be ordered, order already exists for this book."); // FALSE FALSE TRUE
                    }
                    else if (result == 4)
                    {
                        showErrorAlert("Order Book", "Your account is frozen. Please contact the library staff for further information."); // FALSE TRUE FALSE
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
     * Validates the login form fields for user ID, password, and account type selection.
     * Attempts to log in as either a Subscriber or a Librarian based on the selected radio button.
     * If the login is successful, navigates to the corresponding UI screen; otherwise, shows an error alert.
     */
    @FXML
    private void validate_scanReaderCard_login()
    {
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
        alert.showAndWait().ifPresent(response ->
        {
            if (response == confirmButton)
            {
                String enteredId = inputField.getText();
                // Check if the ID is not empty
                if (enteredId != null && !enteredId.trim().isEmpty())
                {
                    Subscriber subscriber = subscriberController.attemptSubscriberLogInByCard(enteredId);
                    if (subscriber != null)
                    {
                        SceneManager.switchScene("/gui/user/subscriberUI/SubscriberUI_UI.fxml", "BLib.4 - Braude Library Management");
                    }
                    else
                    {
                        showErrorAlert("Login By Reader Card Failed", "The account was not found. Please scan again. If the problem persists, please contact the librarian.");
                    }
                }
                else
                {
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
    public void validate_login_form()
    {
        idTextField.getStyleClass().remove("error-text-field");
        passwordTextField.getStyleClass().remove("error-text-field");

        boolean isValid = true;

        String userID = idTextField.getText();
        String password = passwordTextField.getText();
        RadioButton selectedRadioButton = (RadioButton) accountTypeGroup.getSelectedToggle();

        if (selectedRadioButton == null)
        {
            isValid = false;
        }

        if (userID == null || userID.trim().isEmpty())
        {
            idTextField.setPromptText("ID is required");
            idTextField.getStyleClass().add("error-text-field");
            isValid = false;
        }

        if (password == null || password.trim().isEmpty())
        {
            passwordTextField.setPromptText("Password is required");
            passwordTextField.getStyleClass().add("error-text-field");
            isValid = false;
        }

        if (isValid)
        {
            String accountType = selectedRadioButton.getText();

            if (accountType.equals("Subscriber"))
            {
                Subscriber subscriber = subscriberController.attemptLoginAsSubscriber(userID, password);
                if (subscriber != null)
                {
                    SceneManager.switchScene("/gui/user/subscriberUI/SubscriberUI_UI.fxml", "BLib.4 - Braude Library Management");
                }
                else
                {
                    clearFields();
                    showErrorAlert("Login Failed", "The account was not found. Please verify that the details you entered are correct.");
                }
            }
            else if (accountType.equals("Librarian"))
            {
                Librarian librarian = subscriberController.attemptLoginAsLibrarian(userID, password);
                if (librarian != null)
                {
                    SceneManager.switchScene("/gui/librarian/librarianUI/LibrarianUI_UI.fxml", "BLib.4 - Braude Library Management");
                }
                else
                {
                    clearFields();
                    showErrorAlert("Login Failed", "The account was not found. Please verify that the details you entered are correct.");
                }
            }
        }
    }

    /**
     * Clears the ID and Password text fields.
     */
    private void clearFields()
    {
        idTextField.clear();
        passwordTextField.clear();
    }

    /**
     * Displays an error alert with the specified title and message.
     *
     * @param title   the title of the error alert
     * @param message the message displayed in the error alert
     */
    private void showErrorAlert(String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(SceneManager.getStage());
        alert.showAndWait();
    }

    /**
     * Displays an information alert with the specified title and message.
     *
     * @param title   the title of the information alert
     * @param message the message displayed in the information alert
     */
    private void showInformationAlert(String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
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
    private void logout()
    {
        subscriberController.attemptLogOut();
    }

    /**
     * Navigates to the Dashboard screen.
     */
    @FXML
    private void goToDashboard()
    {
        if (isSubscriberLoggedIn)
        {
            SceneManager.switchScene("/gui/user/subscriberUI/SubscriberUI_UI.fxml", "BLib.4 - Braude Library Management");
        }
        else if (isLibrarianLoggedIn)
        {
            SceneManager.switchScene("/gui/librarian/librarianUI/LibrarianUI_UI.fxml", "BLib.4 - Braude Library Management");
        }
    }

    /**
     * Navigates to the Search screen.
     */
    @FXML
    private void goToSearch()
    {
        SceneManager.switchScene("/gui/common/search/Search_UI.fxml", "BLib.4 - Braude Library Management");
    }

    /**
     * Navigates to the View History screen.
     */
    @FXML
    private void goToViewHistory()
    {
        SceneManager.switchScene("/gui/user/viewHistory/ViewHistory_UI.fxml", "BLib.4 - Braude Library Management");
    }

    /**
     * Navigates to the Edit Profile screen.
     */
    @FXML
    private void goToEditProfile()
    {
        SceneManager.switchScene("/gui/user/editProfile/EditProfile_UI.fxml", "BLib.4 - Braude Library Management");
    }

    /**
     * Navigates to the Librarian Dashboard screen.
     */
    @FXML
    private void goToLibrarianDash()
    {
        SceneManager.switchScene("/gui/librarian/librarianUI/LibrarianUI_UI.fxml", "BLib.4 - Braude Library Management");
    }

    /**
     * Navigates to the New Borrow screen.
     */
    @FXML
    private void goToNewBorrow()
    {
        SceneManager.switchScene("/gui/librarian/newBorrow/NewBorrow_UI.fxml", "BLib.4 - Braude Library Management");
    }

    /**
     * Navigates to the Add Subscriber screen.
     */
    @FXML
    private void goToAddSubscriber()
    {
        SceneManager.switchScene("/gui/librarian/addSubscriber/AddSubscriber_UI.fxml", "BLib.4 - Braude Library Management");
    }

    /**
     * Navigates to the Library Reports screen.
     */
    @FXML
    private void goToLibraryReports()
    {
        SceneManager.switchScene("/gui/librarian/libraryReports/LibraryReports_UI.fxml", "BLib.4 - Braude Library Management");
    }

    /**
     * Navigates to the Home Page.
     */
    @FXML
    private void goToHomePage()
    {
        SceneManager.switchScene("/gui/common/homePage/HomePage_UI.fxml", "BLib.4 - Braude Library Management");
    }

    /**
     * Exits the application by quitting the client connection.
     */
    @FXML
    private void exitApp()
    {
        ClientUI.chat.getClient().quit();
    }
}
