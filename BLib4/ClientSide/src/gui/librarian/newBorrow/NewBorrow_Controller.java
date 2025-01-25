package gui.librarian.newBorrow;

import entities.logic.MessageType;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import logic.communication.ChatClient;
import logic.communication.ClientUI;
import logic.communication.SceneManager;
import logic.librarian.BorrowController;
import logic.user.BooksController;
import logic.user.Subscriber_Controller;

import java.time.LocalDate;

public class NewBorrow_Controller {

    // FXML fields
    @FXML
    private Text userGreeting;
    @FXML
    private Button homePageButton;
    @FXML
    private ImageView homePageImageView;
    @FXML
    private Button librarianDashButton;
    @FXML
    private ImageView librarianDashImageView;
    @FXML
    private Button searchBooksButton;
    @FXML
    private ImageView searchBooksImageView;
    @FXML
    private Button addSubscriberButton;
    @FXML
    private ImageView addSubscriberImageView;
    @FXML
    private Button reportsButton;
    @FXML
    private ImageView reportsImageView;
    @FXML
    private Button logoutButton;
    @FXML
    private ImageView logoutImageView;
    @FXML
    private Button exitButton;
    @FXML
    private ImageView exitImageView;

    // Form FXML fields
    @FXML
    private Text copyIdText;
    @FXML
    private TextField copyIdField;
    @FXML
    private Button scanBarcodeButton;
    @FXML
    private Button nextButton1;
    @FXML
    private Text userIdText;
    @FXML
    private TextField userIdField;
    @FXML
    private Button checkButton;
    @FXML
    private Button nextButton2;
    @FXML
    private Text returnDateText;
    @FXML
    private DatePicker returnDatePicker;
    @FXML
    private Button twoWeeksButton;
    @FXML
    private Button submitButton;
    @FXML
    private Button resetButton;

    // Fields
    private Subscriber_Controller subscriberController;
    private BooksController booksController;
    private BorrowController borrowController;
    private int availability;

    @FXML
    public void initialize() {
        // Get the singleton instance of Subscriber_Controller
        subscriberController = Subscriber_Controller.getInstance();
        // Get the singleton instance of BooksController
        booksController = BooksController.getInstance();
        // Get the singleton instance of BorrowController
        borrowController = BorrowController.getInstance();

        // Set the greeting message
        userGreeting.setText(getGreetingMessage() + " " + subscriberController.getLoggedLibrarian().getName() + " !");

        // Set the icons for the buttons
        homePageButton.setOnMouseEntered(event -> {
            homePageImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/home_24dp_525FE1.png")));
        });
        homePageButton.setOnMouseExited(event -> {
            homePageImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/home_24dp_FFFFFF.png")));
        });

        librarianDashButton.setOnMouseEntered(event -> {
            librarianDashImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/local_library_24dp_F86F03.png")));
        });
        librarianDashButton.setOnMouseExited(event -> {
            librarianDashImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/local_library_24dp_FFFFFF.png")));
        });

        searchBooksButton.setOnMouseEntered(event -> {
            searchBooksImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/search_24dp_525FE1.png")));
        });
        searchBooksButton.setOnMouseExited(event -> {
            searchBooksImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/search_24dp_FFFFFF.png")));
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

        // Set the factory for customizing day cells
        returnDatePicker.setDayCellFactory(datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                // Disable dates before today and two weeks from now
                if (item.isBefore(LocalDate.now()) || item.isAfter(LocalDate.now().plusWeeks(2))) {
                    setDisable(true);
                    setStyle("-fx-background-color: #EEEEEE; -fx-text-fill: #999999;");
                }
            }
        });
    }

    @FXML
    private void scanBarcode() {
        // Create a custom Alert
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.initOwner(SceneManager.getStage());
        alert.setTitle("Scan Copy's Barcode (Simulation)");
        alert.setHeaderText("Enter a book copy number to simulate scanning a barcode");

        // Create a TextField for input
        TextField inputField = new TextField();
        inputField.setPromptText("Enter book copy number (equivalent to Copy ID)");

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
                    copyIdField.setText(enteredId);
                } else {
                    showErrorAlert("Invalid Input", "Please enter a valid Copy ID.");
                }
            }
        });
    }

    @FXML
    private void step1() {
        // Reset styles before validation
        copyIdField.getStyleClass().remove("error-text-field");

        // Store values in variables
        String copyID = copyIdField.getText();

        // Validate copy ID
        if (copyID == null || copyID.trim().isEmpty()) {
            copyIdField.setPromptText("Copy ID is required");
            copyIdField.getStyleClass().add("error-text-field");
        } else if (!copyID.matches("\\d+")) {
            copyIdField.clear();
            copyIdField.setPromptText("Copy ID must contain only digits");
            copyIdField.getStyleClass().add("error-text-field");
        } else {
            availability = booksController.checkAvailability(copyID);

            if (availability == 0) {
                userIdText.setVisible(true);
                userIdText.setManaged(true);
                userIdField.setVisible(true);
                userIdField.setManaged(true);
                checkButton.setVisible(true);
                checkButton.setManaged(true);
                nextButton2.setVisible(true);
                nextButton2.setManaged(true);
                copyIdText.setDisable(true);
                copyIdField.setDisable(true);
                scanBarcodeButton.setDisable(true);
                scanBarcodeButton.getStyleClass().add("disabled-button");
                nextButton1.setDisable(true);
                nextButton1.getStyleClass().add("disabled-button");
            } else if (availability == 1) {
                showInformationAlert("Book Reserved", "Notice: All copies of the book are reserved. Only a subscriber who has received a notification that the copy is ready for borrowing at the library will be allowed to proceed with the process.");
                userIdText.setVisible(true);
                userIdText.setManaged(true);
                userIdField.setVisible(true);
                userIdField.setManaged(true);
                checkButton.setVisible(true);
                checkButton.setManaged(true);
                nextButton2.setVisible(true);
                nextButton2.setManaged(true);
                copyIdText.setDisable(true);
                copyIdField.setDisable(true);
                scanBarcodeButton.setDisable(true);
                scanBarcodeButton.getStyleClass().add("disabled-button");
                nextButton1.setDisable(true);
                nextButton1.getStyleClass().add("disabled-button");
            } else if (availability == 2) {
                showErrorAlert("Error", "The book copy already borrowed.");
            }
            else showErrorAlert("Error", "The book copy does not exist in the database.");
        }
    }

    @FXML
    private void checkUser() {
        // Reset styles before validation
        userIdField.getStyleClass().remove("error-text-field");

        // Store values in variables
        String userID = userIdField.getText();

        // Validate user ID
        if (userID == null || userID.trim().isEmpty()) {
            userIdField.setPromptText("User ID is required");
            userIdField.getStyleClass().add("error-text-field");
        } else if (!userID.matches("\\d{9}")) {
            userIdField.clear();
            userIdField.setPromptText("User ID must be 9 digits");
            userIdField.getStyleClass().add("error-text-field");
        } else {
            ClientUI.chat.accept(new MessageType("116", userID));
            int subscriberStatus = ChatClient.subscriberStatus;
            if (subscriberStatus == 0) {
                showInformationAlert("Success", "The subscriber is in the database and active.");
            } else if (subscriberStatus == 1) {
                showErrorAlert("Error", "The subscriber is not in the database.");
            } else if (subscriberStatus == 2) {
                showErrorAlert("Subscriber is Frozen", "The subscriber is in the database but is frozen.");
            } else {
                showErrorAlert("Error", "An error occurred while checking the subscriber.");
            }
        }
    }

    @FXML
    private void step2() {
        // Reset styles before validation
        userIdField.getStyleClass().remove("error-text-field");

        // Store values in variables
        String userID = userIdField.getText();

        // Validate user ID
        if (userID == null || userID.trim().isEmpty()) {
            userIdField.setPromptText("User ID is required");
            userIdField.getStyleClass().add("error-text-field");
        } else if (!userID.matches("\\d{9}")) {
            userIdField.clear();
            userIdField.setPromptText("User ID must be 9 digits");
            userIdField.getStyleClass().add("error-text-field");
        } else {
            ClientUI.chat.accept(new MessageType("116", userID));
            int subscriberStatus = ChatClient.subscriberStatus;
            if (subscriberStatus == 0) {
                if (availability == 0 || (availability == 1 && borrowController.isSubscriberInWaitList(userID))) {
                    returnDateText.setVisible(true);
                    returnDateText.setManaged(true);
                    returnDatePicker.setVisible(true);
                    returnDatePicker.setManaged(true);
                    twoWeeksButton.setVisible(true);
                    twoWeeksButton.setManaged(true);
                    submitButton.setVisible(true);
                    submitButton.setManaged(true);
                    userIdText.setDisable(true);
                    userIdField.setDisable(true);
                    checkButton.setDisable(true);
                    checkButton.getStyleClass().add("disabled-button");
                    nextButton2.setDisable(true);
                    nextButton2.getStyleClass().add("disabled-button");
                } else {
                    showErrorAlert("Unable to Proceed", "The subscriber has not reserved the book or his turn in the waiting list hasn't arrived yet.");
                }
            } else if (subscriberStatus == 1) {
                showErrorAlert("Unable to proceed", "The subscriber is not in the database. Unable to proceed.");
            } else if (subscriberStatus == 2) {
                showErrorAlert("Unable to proceed", "The subscriber is frozen. Unable to proceed.");
            } else {
                showErrorAlert("Error", "An error occurred while checking the subscriber.");
            }
        }
    }

    @FXML
    private void setInTwoWeeks() {
        returnDatePicker.setValue(LocalDate.now().plusWeeks(2));
    }

    @FXML
    private void submitNewBorrow() {
        // Reset styles before validation
        returnDatePicker.getStyleClass().remove("error-text-field");

        // Store values in variables
        LocalDate returnDate = returnDatePicker.getValue();

        // Validate return date
        if (returnDate == null) {
            returnDatePicker.setPromptText("Return date is required");
            returnDatePicker.getStyleClass().add("error-text-field");
        } else {
            String copyID = copyIdField.getText();
            String userID = userIdField.getText();

            if (borrowController.createNewBorrow(userID, copyID, returnDate.toString())) {
                showInformationAlert("Success", "The borrow has been successfully created.");
                //back to the start of new borrow
                SceneManager.switchScene("/gui/librarian/newBorrow/NewBorrow_UI.fxml", "BLib.4 - Braude Library Management");
            }
            else
                showErrorAlert("Error", "Failed to create the borrow. Please try again.");
        }
    }
    
    @FXML
    private void reset_form() {
        // Reset the form by reloading the scene
        SceneManager.switchScene("/gui/librarian/newBorrow/NewBorrow_UI.fxml", "BLib.4 - Braude Library Management");
    }

    /**
     * Returns a greeting message based on the current time of the day.
     *
     * @return A greeting message.
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
     * Logs out the current user and navigates to the Home Page.
     */
    @FXML
    private void logout() {
        subscriberController.attemptLogOut();
    }

    @FXML
    private void goToHomePage() {
        SceneManager.switchScene("/gui/common/homePage/HomePage_UI.fxml", "BLib.4 - Braude Library Management");
    }

    @FXML
    private void goToLibrarianDash() {
        SceneManager.switchScene("/gui/librarian/librarianUI/LibrarianUI_UI.fxml", "BLib.4 - Braude Library Management");
    }

    @FXML
    private void goToSearch() {
        SceneManager.switchScene("/gui/common/search/Search_UI.fxml", "BLib.4 - Braude Library Management");
    }

    @FXML
    private void goToAddSubscriber() {
        SceneManager.switchScene("/gui/librarian/addSubscriber/AddSubscriber_UI.fxml", "BLib.4 - Braude Library Management");
    }

    @FXML
    private void exitApp() {
        ClientUI.chat.getClient().quit();
    }
}
