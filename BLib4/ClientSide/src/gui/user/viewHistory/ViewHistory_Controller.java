package gui.user.viewHistory;

import entities.logic.MessageType;
import gui.user.subscriberUI.BorrowEntry;
import gui.user.viewHistory.ActivityEntry;
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
import java.util.Arrays;

public class ViewHistory_Controller {

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
    private Button editProfileButton;
    @FXML
    private ImageView editProfileImageView;
    @FXML
    private Button logoutButton;
    @FXML
    private ImageView logoutImageView;
    @FXML
    private Button exitButton;
    @FXML
    private ImageView exitImageView;

    // Activities Table FXML fields
    @FXML
    private TableView<ActivityEntry> activitiesTable;
    @FXML
    private TableColumn<ActivityEntry, String> activityDateColumn;
    @FXML
    private TableColumn<ActivityEntry, String> activityTimeColumn;
    @FXML
    private TableColumn<ActivityEntry, String> activityDetailsColumn;

    // Fields
    private Subscriber_Controller subscriberController;
    private ArrayList<ArrayList<String>> subscriberActivities;
    private final ObservableList<ActivityEntry> activityEntries = FXCollections.observableArrayList(); // List of activity entries (dynamic)

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

//        // Get the subscriber's activities
//        ClientUI.chat.accept(new MessageType("112", subscriberController.getLoggedSubscriber().getId()));
//        subscriberActivities = ChatClient.listOfActivities;

        // FOR TESTING PURPOSES
        initializeSubscriberActivitiesForTesting();

        // Add rounded corners to the TableView
        Rectangle clip = new Rectangle();
        clip.setArcWidth(20);  // Set horizontal corner radius
        clip.setArcHeight(20); // Set vertical corner radius
        // Bind the clip's size to the TableView's size
        clip.widthProperty().bind(activitiesTable.widthProperty());
        clip.heightProperty().bind(activitiesTable.heightProperty());
        // Apply the clip to the TableView
        activitiesTable.setClip(clip);

        // Initialize table columns
        activityDateColumn.setCellValueFactory(data -> data.getValue().activityDateProperty());
        activityTimeColumn.setCellValueFactory(data -> data.getValue().activityTimeProperty());
        activityDetailsColumn.setCellValueFactory(data -> data.getValue().activityDetailsProperty());

        activitiesTable.setPlaceholder(new Text("No activities to display.")); // Set the placeholder text
        activitiesTable.setFixedCellSize(35); // Set the row height
        activitiesTable.setSelectionModel(null); // Disable row selection

        // Populate the table
        loadActivitiesData();
        activitiesTable.setItems(activityEntries);

        // Ensure the bookTitleColumn fills the remaining space
        activityDetailsColumn.prefWidthProperty().bind(
                activitiesTable.widthProperty()
                        .subtract(170 * 2) // Subtract the total width of fixed columns
                        .subtract(2) // Subtract the border width
                        .subtract(activitiesTable.getItems().size() > 14 ? 20 : 0) // Subtract 20 if more than 7 rows for the scrollbar
        );
    }

    private void loadActivitiesData() {
        for (ArrayList<String> activity : subscriberActivities) {
            ActivityEntry entry = new ActivityEntry(
                    activity.get(0), // activity date
                    activity.get(1), // activity time
                    activity.get(2) // activity details
            );
            activityEntries.add(entry);
        }
    }

    private void initializeSubscriberActivitiesForTesting() {
        subscriberActivities = new ArrayList<>();

        // Adding 20 example records
        subscriberActivities.add(new ArrayList<>(Arrays.asList("01/01/2025", "09:30", "Checked out book")));
        subscriberActivities.add(new ArrayList<>(Arrays.asList("02/01/2025", "14:45", "Returned book")));
        subscriberActivities.add(new ArrayList<>(Arrays.asList("03/01/2025", "11:15", "Renewed borrow")));
        subscriberActivities.add(new ArrayList<>(Arrays.asList("04/01/2025", "16:50", "Checked out book")));
        subscriberActivities.add(new ArrayList<>(Arrays.asList("05/01/2025", "10:00", "Reservation made")));
        subscriberActivities.add(new ArrayList<>(Arrays.asList("06/01/2025", "13:20", "Reservation canceled")));
        subscriberActivities.add(new ArrayList<>(Arrays.asList("07/01/2025", "08:10", "Checked out book")));
        subscriberActivities.add(new ArrayList<>(Arrays.asList("08/01/2025", "15:00", "Fine paid")));
        subscriberActivities.add(new ArrayList<>(Arrays.asList("09/01/2025", "12:45", "Checked out book")));
        subscriberActivities.add(new ArrayList<>(Arrays.asList("10/01/2025", "09:00", "Reservation made")));
        subscriberActivities.add(new ArrayList<>(Arrays.asList("11/01/2025", "17:30", "Returned book")));
        subscriberActivities.add(new ArrayList<>(Arrays.asList("12/01/2025", "14:15", "Renewed borrow")));
        subscriberActivities.add(new ArrayList<>(Arrays.asList("13/01/2025", "13:05", "Checked out book")));
        subscriberActivities.add(new ArrayList<>(Arrays.asList("14/01/2025", "10:50", "Fine paid")));
        subscriberActivities.add(new ArrayList<>(Arrays.asList("15/01/2025", "16:40", "Reservation made")));
        subscriberActivities.add(new ArrayList<>(Arrays.asList("16/01/2025", "08:30", "Returned book")));
        subscriberActivities.add(new ArrayList<>(Arrays.asList("17/01/2025", "12:00", "Checked out book")));
        subscriberActivities.add(new ArrayList<>(Arrays.asList("18/01/2025", "09:45", "Reservation canceled")));
        subscriberActivities.add(new ArrayList<>(Arrays.asList("19/01/2025", "15:20", "Fine paid")));
        subscriberActivities.add(new ArrayList<>(Arrays.asList("20/01/2025", "10:15", "Checked out book")));
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
    private void goToDashboard() {
        SceneManager.switchScene("/gui/user/subscriberUI/SubscriberUI_UI.fxml", "BLib.4 - Braude Library Management");
    }

    @FXML
    private void goToSearch() {
        SceneManager.switchScene("/gui/common/search/Search_UI.fxml", "BLib.4 - Braude Library Management");
    }

    @FXML
    private void exitApp() {
        ClientUI.chat.getClient().quit();
    }
}

