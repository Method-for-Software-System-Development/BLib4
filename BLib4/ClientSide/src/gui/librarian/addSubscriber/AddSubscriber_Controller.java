package gui.librarian.addSubscriber;

import entities.logic.MessageType;
import entities.user.Librarian;
import entities.user.Subscriber;
import gui.user.subscriberUI.BorrowEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import logic.communication.ChatClient;
import logic.communication.ClientUI;
import logic.communication.SceneManager;
import logic.user.Subscriber_Controller;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class AddSubscriber_Controller {

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
    private Button newBorrowButton;
    @FXML
    private ImageView newBorrowImageView;
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
    private TextField userIdField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;

    // Fields
    private Subscriber_Controller subscriberController;

    @FXML
    public void initialize() {
        // Get the singleton instance of Subscriber_Controller
        subscriberController = Subscriber_Controller.getInstance();

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

        newBorrowButton.setOnMouseEntered(event -> {
            newBorrowImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/post_add_24dp_525FE1.png")));
        });
        newBorrowButton.setOnMouseExited(event -> {
            newBorrowImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/post_add_24dp_FFFFFF.png")));
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

    @FXML
    public void validate_addSubscriber_form() {
        // Reset styles before validation
        userIdField.getStyleClass().remove("error-text-field");
        firstNameField.getStyleClass().remove("error-text-field");
        lastNameField.getStyleClass().remove("error-text-field");
        phoneNumberField.getStyleClass().remove("error-text-field");
        emailField.getStyleClass().remove("error-text-field");
        passwordField.getStyleClass().remove("error-text-field");
        confirmPasswordField.getStyleClass().remove("error-text-field");

        boolean isValid = true;

        // Store values in variables
        String userID = userIdField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String phoneNumber = phoneNumberField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        // Validate user ID
        if (userID == null || userID.trim().isEmpty()) {
            userIdField.setPromptText("ID is required");
            userIdField.getStyleClass().add("error-text-field");
            isValid = false;
        } else if (!userID.matches("\\d{9}")) {
            userIdField.clear();
            userIdField.setPromptText("ID must be 9 digits");
            userIdField.getStyleClass().add("error-text-field");
            isValid = false;
        }

        // Validate first name
        if (firstName == null || firstName.trim().isEmpty()) {
            firstNameField.setPromptText("First name is required");
            firstNameField.getStyleClass().add("error-text-field");
            isValid = false;
        } else if (!firstName.matches("[a-zA-Z]+")) {
            firstNameField.clear();
            firstNameField.setPromptText("First name must contain only letters");
            firstNameField.getStyleClass().add("error-text-field");
            isValid = false;
        }

        // Validate last name
        if (lastName == null || lastName.trim().isEmpty()) {
            lastNameField.setPromptText("Last name is required");
            lastNameField.getStyleClass().add("error-text-field");
            isValid = false;
        } else if (!lastName.matches("[a-zA-Z]+")) {
            lastNameField.clear();
            lastNameField.setPromptText("Last name must contain only letters");
            lastNameField.getStyleClass().add("error-text-field");
            isValid = false;
        }

        // Validate phone number
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            phoneNumberField.setPromptText("Phone number is required");
            phoneNumberField.getStyleClass().add("error-text-field");
            isValid = false;
        } else if (!phoneNumber.matches("\\d{10}")) {
            phoneNumberField.clear();
            phoneNumberField.setPromptText("Phone number must be 10 digits");
            phoneNumberField.getStyleClass().add("error-text-field");
            isValid = false;
        }

        // Validate email
        if (email == null || email.trim().isEmpty()) {
            emailField.setPromptText("Email is required");
            emailField.getStyleClass().add("error-text-field");
            isValid = false;
        } else if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            emailField.clear();
            emailField.setPromptText("Invalid email format");
            emailField.getStyleClass().add("error-text-field");
            isValid = false;
        }

        // Validate password
        if (password == null || password.trim().isEmpty()) {
            passwordField.setPromptText("Password is required");
            passwordField.getStyleClass().add("error-text-field");
            isValid = false;
        } else if (!password.equals(confirmPasswordField.getText())) {
            passwordField.clear();
            confirmPasswordField.clear();
            passwordField.setPromptText("Passwords do not match");
            passwordField.getStyleClass().add("error-text-field");
            confirmPasswordField.setPromptText("Passwords do not match");
            confirmPasswordField.getStyleClass().add("error-text-field");
            isValid = false;
        }

        if (isValid) {
            boolean successful = subscriberController.addNewSubscriber(userID, firstName, lastName, phoneNumber, email, password);

            if (successful) {
                showInformationAlert("Success", "Subscriber added successfully.");
                clear_form();
            } else {
                showErrorAlert("Error", "Failed to add subscriber. Please try again.");
            }
        }
    }

    @FXML
    public void clear_form() {
        userIdField.clear();
        firstNameField.clear();
        lastNameField.clear();
        phoneNumberField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        userIdField.getStyleClass().remove("error-text-field");
        firstNameField.getStyleClass().remove("error-text-field");
        lastNameField.getStyleClass().remove("error-text-field");
        phoneNumberField.getStyleClass().remove("error-text-field");
        emailField.getStyleClass().remove("error-text-field");
        passwordField.getStyleClass().remove("error-text-field");
        confirmPasswordField.getStyleClass().remove("error-text-field");
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
    private void goToNewBorrow() {
        SceneManager.switchScene("/gui/librarian/newBorrow/NewBorrow_UI.fxml", "BLib.4 - Braude Library Management");
    }

    @FXML
    private void goToLibraryReports() {
        SceneManager.switchScene("/gui/librarian/libraryReports/LibraryReports_UI.fxml", "BLib.4 - Braude Library Management");
    }

    @FXML
    private void exitApp() {
        ClientUI.chat.getClient().quit();
    }
}