package gui.user.viewHistory;

import entities.logic.MessageType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import logic.communication.ChatClient;
import logic.communication.ClientUI;
import logic.communication.SceneManager;
import logic.user.Subscriber_Controller;

import java.util.ArrayList;
import java.util.Arrays;

public class ViewHistory_Controller
{
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

    /**
     * Initializes the View History UI.
     */
    @FXML
    public void initialize()
    {
        // Get the singleton instance of Subscriber_Controller
        subscriberController = Subscriber_Controller.getInstance();

        // Set the greeting message
        userGreeting.setText(getGreetingMessage() + " " + subscriberController.getLoggedSubscriber().getFirstName() + " !");

        // Set the icons for the buttons
        homePageButton.setOnMouseEntered(event ->
        {
            homePageImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/home_24dp_525FE1.png")));
        });
        homePageButton.setOnMouseExited(event ->
        {
            homePageImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/home_24dp_FFFFFF.png")));
        });

        dashboardButton.setOnMouseEntered(event ->
        {
            dashboardImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/person_24dp_F86F03.png")));
        });
        dashboardButton.setOnMouseExited(event ->
        {
            dashboardImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/person_24dp_FFFFFF.png")));
        });

        searchBooksButton.setOnMouseEntered(event ->
        {
            searchBooksImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/search_24dp_525FE1.png")));
        });
        searchBooksButton.setOnMouseExited(event ->
        {
            searchBooksImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/search_24dp_FFFFFF.png")));
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

        exitButton.setOnMouseEntered(event ->
        {
            exitImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/close_24dp_525FE1.png")));
        });
        exitButton.setOnMouseExited(event ->
        {
            exitImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/close_24dp_FFFFFF.png")));
        });

        // Get the subscriber's activities
        ClientUI.chat.accept(new MessageType("112", subscriberController.getLoggedSubscriber().getId()));
        subscriberActivities = ChatClient.listOfActivities;

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
        activityDetailsColumn.setCellFactory(column ->
        {
            return new TableCell<ActivityEntry, String>()
            {
                private final Text text = new Text();

                @Override
                protected void updateItem(String item, boolean empty)
                {
                    super.updateItem(item, empty);
                    if (empty || item == null)
                    {
                        setGraphic(null);
                    }
                    else
                    {
                        text.setText(item);
                        text.wrappingWidthProperty().bind(activityDetailsColumn.widthProperty().subtract(10)); // Wrap text within column width
                        setGraphic(text);
                    }
                }
            };
        });
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

    /**
     * Loads the subscriber's activities data into the table.
     */
    private void loadActivitiesData()
    {
        for (ArrayList<String> activity : subscriberActivities)
        {
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
     * Logs out the current user and navigates to the Home Page.
     */
    @FXML
    private void logout()
    {
        subscriberController.attemptLogOut();
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
     * Navigates to the Dashboard.
     */
    @FXML
    private void goToDashboard()
    {
        SceneManager.switchScene("/gui/user/subscriberUI/SubscriberUI_UI.fxml", "BLib.4 - Braude Library Management");
    }

    /**
     * Navigates to the Search Books page.
     */
    @FXML
    private void goToSearch()
    {
        SceneManager.switchScene("/gui/common/search/Search_UI.fxml", "BLib.4 - Braude Library Management");
    }

    /**
     * Navigates to the Edit Profile page.
     */
    @FXML
    private void goToEditProfile()
    {
        SceneManager.switchScene("/gui/user/editProfile/EditProfile_UI.fxml", "BLib.4 - Braude Library Management");
    }

    /**
     * Exits the application.
     */
    @FXML
    private void exitApp()
    {
        ClientUI.chat.getClient().quit();
    }
}

