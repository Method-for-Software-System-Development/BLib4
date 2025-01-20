package gui.librarian.librarianUI;

import entities.logic.MessageType;
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

public class LibrarianUI_Controller {

    // FXML fields
    @FXML
    private Text userGreeting;
    @FXML
    private Button homePageButton;
    @FXML
    private ImageView homePageImageView;
    @FXML
    private Button searchBooksButton;
    @FXML
    private ImageView searchBooksImageView;
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
    private Button logoutButton;
    @FXML
    private ImageView logoutImageView;
    @FXML
    private Button exitButton;
    @FXML
    private ImageView exitImageView;

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