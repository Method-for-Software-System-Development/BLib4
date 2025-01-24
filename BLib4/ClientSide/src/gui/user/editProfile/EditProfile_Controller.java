package gui.user.editProfile;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import logic.communication.ClientUI;
import logic.communication.SceneManager;
import logic.user.Subscriber_Controller;

import java.util.regex.Pattern;

public class EditProfile_Controller {

    // FXML fields
    @FXML
    private Text userGreeting;
    @FXML
    private Button homePageButton;
    @FXML
    private ImageView homePageImageView;
    @FXML
    private Button dashboardButton;
    @FXML
    private ImageView dashboardImageView;
    @FXML
    private Button searchBooksButton;
    @FXML
    private ImageView searchBooksImageView;
    @FXML
    private Button viewHistoryButton;
    @FXML
    private ImageView viewHistoryImageView;
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
        userGreeting.setText(getGreetingMessage() + " " + subscriberController.getLoggedSubscriber().getFirstName() + " !");

        // Set the icons for the buttons
        homePageButton.setOnMouseEntered(event -> {
            homePageImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/home_24dp_525FE1.png")));
        });
        homePageButton.setOnMouseExited(event -> {
            homePageImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/home_24dp_FFFFFF.png")));
        });

        dashboardButton.setOnMouseEntered(event -> {
            dashboardImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/person_24dp_F86F03.png")));
        });
        dashboardButton.setOnMouseExited(event -> {
            dashboardImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/person_24dp_FFFFFF.png")));
        });

        searchBooksButton.setOnMouseEntered(event -> {
            searchBooksImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/search_24dp_525FE1.png")));
        });
        searchBooksButton.setOnMouseExited(event -> {
            searchBooksImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/search_24dp_FFFFFF.png")));
        });

        viewHistoryButton.setOnMouseEntered(event -> {
            viewHistoryImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/history_24dp_525FE1.png")));
        });
        viewHistoryButton.setOnMouseExited(event -> {
            viewHistoryImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/history_24dp_FFFFFF.png")));
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

        // Set the form fields
        userIdField.setText(subscriberController.getLoggedSubscriber().getId());
        userIdField.setDisable(true);
        firstNameField.setText(subscriberController.getLoggedSubscriber().getFirstName());
        firstNameField.setDisable(true);
        lastNameField.setText(subscriberController.getLoggedSubscriber().getLastName());
        lastNameField.setDisable(true);
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
    public void validate_editProfile_form() {
        // Initialize variables for updated fields
        String phoneNumber = "";
        String email = "";
        String password = "";

        // Validate phone number
        if (phoneNumberField != null && phoneNumberField.getText() != null && !phoneNumberField.getText().isEmpty()) {
            if (phoneNumberField.getText().matches("\\d{10}") && (((String) phoneNumberField.getText()).startsWith("05"))) {
                phoneNumber = phoneNumberField.getText();
            } else {
                showErrorAlert("Validation Error", "Please enter a valid phone number (10 digits).\n The number must start at 05-.");
                clear_form();
                return;
            }
        }

        // Validate email
        if (emailField != null && emailField.getText() != null && !emailField.getText().isEmpty()) {
            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
            if (Pattern.matches(emailRegex, emailField.getText())) {
                email = emailField.getText();
            } else {
                showErrorAlert("Validation Error", "Please enter a valid email address.");
                clear_form();
                return;
            }
        }

        // Validate password and confirm password
        if (passwordField != null && passwordField.getText() != null && !passwordField.getText().isEmpty()) {
            if (passwordField.getText().equals(confirmPasswordField.getText())) {
                password = passwordField.getText();
            } else {
                showErrorAlert("Validation Error", "Passwords do not match.");
                clear_form();
                return;
            }
        }

        // Check if at least one field is filled for updating
        if (phoneNumber.isEmpty() && email.isEmpty() && password.isEmpty()) {
            showErrorAlert("Validation Error", "Please fill in at least one field to update.");
            return;
        }

        // Use existing values for fields not being updated
        if (phoneNumber.isEmpty()) {
            phoneNumber = subscriberController.getLoggedSubscriber().getPhone();
        }
        if (email.isEmpty()) {
            email = subscriberController.getLoggedSubscriber().getEmail();
        }

        // Perform the updates
        boolean successfulDetailsUpdate = true;
        boolean successfulPasswordUpdate = true;

        // Update details only if phone or email was changed
        if (!phoneNumber.equals(subscriberController.getLoggedSubscriber().getPhone()) ||
                !email.equals(subscriberController.getLoggedSubscriber().getEmail())) {
            successfulDetailsUpdate = subscriberController.updateSubscriberDetails(
                    subscriberController.getLoggedSubscriber().getId(),
                    subscriberController.getLoggedSubscriber().getFirstName(),
                    subscriberController.getLoggedSubscriber().getLastName(),
                    phoneNumber,
                    email,
                    subscriberController.getLoggedSubscriber().getStatus());
        }

        // Update password if a new password was provided
        if (!password.isEmpty()) {
            successfulPasswordUpdate = subscriberController.updateSubscriberPassword(
                    subscriberController.getLoggedSubscriber().getId(),
                    password);
        }

        // Show success or error message based on the results
        if (successfulDetailsUpdate && successfulPasswordUpdate) {
            showInformationAlert("Success", "Profile updated successfully.");
            clear_form();
        } else {
            showErrorAlert("Error", "Failed to update profile. Please try again.");
            clear_form();
        }
    }

    @FXML
    public void clear_form() {
        phoneNumberField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
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
    private void goToDashboard() {
        SceneManager.switchScene("/gui/user/subscriberUI/SubscriberUI_UI.fxml", "BLib.4 - Braude Library Management");
    }

    @FXML
    private void goToSearch() {
        SceneManager.switchScene("/gui/common/search/Search_UI.fxml", "BLib.4 - Braude Library Management");
    }

    @FXML
    private void goToViewHistory() {
        SceneManager.switchScene("/gui/user/viewHistory/ViewHistory_UI.fxml", "BLib.4 - Braude Library Management");
    }

    @FXML
    private void exitApp() {
        ClientUI.chat.getClient().quit();
    }
}


