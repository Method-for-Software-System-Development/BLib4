package gui.librarian.readerCard;

import entities.logic.MessageType;
import gui.user.viewHistory.ActivityEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import logic.communication.ChatClient;
import logic.communication.ClientUI;
import logic.communication.DataReceiver;
import logic.communication.SceneManager;
import logic.user.Subscriber_Controller;

import java.util.ArrayList;

public class ReaderCard_Controller implements DataReceiver {

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
    @FXML
    private Text readerCardTitle;

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
    private String userId;
    private ArrayList<ArrayList<String>> subscriberActivities;
    private final ObservableList<ActivityEntry> activityEntries = FXCollections.observableArrayList(); // List of activity entries (dynamic)

    @Override
    public void receiveData(Object data) {
        if (data instanceof String) {
            this.userId = (String) data;
            readerCardTitle.setText("Activity History (Reader Card) of Subscriber " + userId + ":");
            // Populate the table
            loadActivitiesData(userId);
            activitiesTable.setItems(activityEntries);
        }
    }

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

        // Ensure the bookTitleColumn fills the remaining space
        activityDetailsColumn.prefWidthProperty().bind(
                activitiesTable.widthProperty()
                        .subtract(170 * 2) // Subtract the total width of fixed columns
                        .subtract(2) // Subtract the border width
                        .subtract(activitiesTable.getItems().size() > 14 ? 20 : 0) // Subtract 20 if more than 7 rows for the scrollbar
        );
    }

    private void loadActivitiesData(String userId) {
        // Get the subscriber's activities
        ClientUI.chat.accept(new MessageType("112", userId));
        subscriberActivities = ChatClient.listOfActivities;

        for (ArrayList<String> activity : subscriberActivities) {
            ActivityEntry entry = new ActivityEntry(
                    activity.get(0), // activity date
                    activity.get(1), // activity time
                    activity.get(2) // activity details
            );
            activityEntries.add(entry);
        }
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
    private void goToAddSubscriber() {
        SceneManager.switchScene("/gui/librarian/addSubscriber/AddSubscriber_UI.fxml", "BLib.4 - Braude Library Management");
    }

    @FXML
    private void exitApp() {
        ClientUI.chat.getClient().quit();
    }
}


