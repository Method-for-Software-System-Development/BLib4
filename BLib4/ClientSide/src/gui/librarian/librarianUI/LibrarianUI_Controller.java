package gui.librarian.librarianUI;

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

public class LibrarianUI_Controller
{
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

    // Messages FXML fields
    @FXML
    private Text numberOfMessages;
    @FXML
    private TableView<MessageEntry> messagesTable;
    @FXML
    private TableColumn<MessageEntry, String> messageIdColumn;
    @FXML
    private TableColumn<MessageEntry, String> messageDateTimeColumn;
    @FXML
    private TableColumn<MessageEntry, String> messageContentColumn;
    @FXML
    private TableColumn<MessageEntry, Void> confirmColumn;

    // Subscribers Table FXML fields
    @FXML
    private TableView<SubscriberEntry> subscribersTable;
    @FXML
    private TableColumn<SubscriberEntry, String> userIdColumn;
    @FXML
    private TableColumn<SubscriberEntry, String> firstNameColumn;
    @FXML
    private TableColumn<SubscriberEntry, String> lastNameColumn;
    @FXML
    private TableColumn<SubscriberEntry, String> statusColumn;
    @FXML
    private TableColumn<SubscriberEntry, Void> borrowsColumn;
    @FXML
    private TableColumn<SubscriberEntry, Void> readerCardColumn;

    // Fields
    private Subscriber_Controller subscriberController;
    private ArrayList<ArrayList<String>> messages;
    private final ObservableList<MessageEntry> messageEntries = FXCollections.observableArrayList(); // List of message entries (dynamic)
    private ArrayList<ArrayList<String>> subscribers;
    private final ObservableList<SubscriberEntry> subscriberEntries = FXCollections.observableArrayList(); // List of message entries (dynamic)subscribers;

    /**
     * Initializes the Librarian UI scene.
     */
    @FXML
    public void initialize()
    {
        // Get the singleton instance of Subscriber_Controller
        subscriberController = Subscriber_Controller.getInstance();

        // Set the greeting message
        userGreeting.setText(getGreetingMessage() + " " + subscriberController.getLoggedLibrarian().getName() + " !");

        // Set the icons for the buttons
        homePageButton.setOnMouseEntered(event ->
        {
            homePageImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/home_24dp_525FE1.png")));
        });
        homePageButton.setOnMouseExited(event ->
        {
            homePageImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/home_24dp_FFFFFF.png")));
        });

        searchBooksButton.setOnMouseEntered(event ->
        {
            searchBooksImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/search_24dp_525FE1.png")));
        });
        searchBooksButton.setOnMouseExited(event ->
        {
            searchBooksImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/search_24dp_FFFFFF.png")));
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

        ClientUI.chat.accept(new MessageType("128", null));
        messages = ChatClient.listOfMessages;

        if (messages.isEmpty())
        {
            numberOfMessages.setText("No new unapproved messages.");
            messagesTable.setVisible(false);
            messagesTable.setManaged(false);
        }
        else
        {
            numberOfMessages.setText("You have " + messages.size() + " new messages:");
        }

        // ********************************** SETUP MESSAGES TABLE **********************************

        // Initialize table columns
        messageIdColumn.setCellValueFactory(data -> data.getValue().messageIdProperty());
        messageDateTimeColumn.setCellValueFactory(data -> data.getValue().messageDateTimeProperty());
        messageContentColumn.setCellValueFactory(data -> data.getValue().messageContentProperty());

        // Add Confirm button
        confirmColumn.setCellFactory(param -> new TableCell<MessageEntry, Void>()
        {
            private final Button confirmButton = new Button("Confirm");

            {
                // Set the CSS class for the button
                confirmButton.getStyleClass().add("small-orange-orange-button");

                // Set preferred size for the button
                confirmButton.setPrefHeight(30.0);
                confirmButton.setPrefWidth(90.0);

                // Set the action for the button
                confirmButton.setOnAction(event ->
                {
                    MessageEntry entry = getTableView().getItems().get(getIndex());
                    String messageId = entry.getMessageId();
                    ClientUI.chat.accept(new MessageType("129", messageId));
                    if (ChatClient.serverResponse)
                    {
                        refreshMessagesTable();
                    }
                    else
                    {
                        showErrorAlert("Error", "Failed to confirm message.");
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty)
            {
                super.updateItem(item, empty);
                if (empty)
                {
                    setGraphic(null); // Clear the cell for empty rows
                }
                else
                {
                    setGraphic(confirmButton); // Add the button for non-empty rows
                }
            }
        });

        messagesTable.setFixedCellSize(35); // Set the row height
        messagesTable.setSelectionModel(null); // Disable row selection

        // Hide the table header
        messagesTable.widthProperty().addListener((obs, oldVal, newVal) ->
        {
            messagesTable.lookup("TableHeaderRow").setVisible(false);
        });

        // Hide the first column
        messageIdColumn.setVisible(false);

        // Populate the table
        loadMessagesData();


        // Ensure the messageContentColumn fills the remaining space
        messageContentColumn.prefWidthProperty().bind(
                messagesTable.widthProperty()
                        .subtract(220) // Subtract the total width of fixed columns
                        .subtract(110)
                        .subtract(2) // Subtract the border width
                        .subtract(messagesTable.getItems().size() > 7 ? 20 : 0) // Subtract 20 if more than 7 rows for the scrollbar
        );

        // ********************************** SETUP SUBSCRIBERS TABLE **********************************

        ClientUI.chat.accept(new MessageType("115", null));
        subscribers = ChatClient.listOfSubscribersForLibrarian;

        // Add rounded corners to the TableView
        Rectangle clip = new Rectangle();
        clip.setArcWidth(20);  // Set horizontal corner radius
        clip.setArcHeight(20); // Set vertical corner radius
        // Bind the clip's size to the TableView's size
        clip.widthProperty().bind(subscribersTable.widthProperty());
        clip.heightProperty().bind(subscribersTable.heightProperty());
        // Apply the clip to the TableView
        subscribersTable.setClip(clip);

        // Initialize table columns
        userIdColumn.setCellValueFactory(data -> data.getValue().userIdProperty());
        firstNameColumn.setCellValueFactory(data -> data.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(data -> data.getValue().lastNameProperty());
        statusColumn.setCellValueFactory(data -> data.getValue().statusProperty());

        // Add Borrows button
        borrowsColumn.setCellFactory(param -> new TableCell<SubscriberEntry, Void>()
        {
            private final Button borrowsButton = new Button("Active Borrows");

            {
                // Set the CSS class for the button
                borrowsButton.getStyleClass().add("small-purple-transparent-button");

                // Set preferred size for the button
                borrowsButton.setPrefHeight(30.0);
                borrowsButton.setPrefWidth(150.0);

                // Set the action for the button
                borrowsButton.setOnAction(event ->
                {
                    SubscriberEntry entry = getTableView().getItems().get(getIndex());
                    String userId = entry.getUserId();
                    SceneManager.switchSceneWithData("/gui/librarian/subscriberBorrows/SubscriberBorrows_UI.fxml", "BLib.4 - Braude Library Management", userId);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty)
            {
                super.updateItem(item, empty);
                if (empty)
                {
                    setGraphic(null); // Clear the cell for empty rows
                }
                else
                {
                    setGraphic(borrowsButton); // Add the button for non-empty rows
                }
            }
        });

        // Add Reader Card button
        readerCardColumn.setCellFactory(param -> new TableCell<SubscriberEntry, Void>()
        {
            private final Button readerCardButton = new Button("Reader Card");

            {
                // Set the CSS class for the button
                readerCardButton.getStyleClass().add("small-purple-transparent-button");

                // Set preferred size for the button
                readerCardButton.setPrefHeight(30.0);
                readerCardButton.setPrefWidth(150.0);

                // Set the action for the button
                readerCardButton.setOnAction(event ->
                {
                    SubscriberEntry entry = getTableView().getItems().get(getIndex());
                    String userId = entry.getUserId();
                    SceneManager.switchSceneWithData("/gui/librarian/readerCard/ReaderCard_UI.fxml", "BLib.4 - Braude Library Management", userId);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty)
            {
                super.updateItem(item, empty);
                if (empty)
                {
                    setGraphic(null); // Clear the cell for empty rows
                }
                else
                {
                    setGraphic(readerCardButton); // Add the button for non-empty rows
                }
            }
        });

        subscribersTable.setPlaceholder(new Text("No subscribers to display.")); // Set the placeholder text
        subscribersTable.setFixedCellSize(35); // Set the row height
        subscribersTable.setSelectionModel(null); // Disable row selection

        // Populate the table
        loadSubscribersData();
        subscribersTable.setItems(subscriberEntries);

        subscribersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /**
     * Loads the message data into the message table.
     */
    private void loadMessagesData()
    {
        for (ArrayList<String> message : messages)
        {
            MessageEntry entry = new MessageEntry(
                    message.get(0), // Message ID
                    message.get(1), // Message Date
                    message.get(2)  // Message Content
            );
            messageEntries.add(entry);
        }
        messagesTable.setItems(messageEntries);
    }

    /**
     * Loads the subscriber data into the subscriber table.
     */
    private void loadSubscribersData()
    {
        for (ArrayList<String> subscriber : subscribers)
        {
            SubscriberEntry entry = new SubscriberEntry(
                    subscriber.get(0), // User ID
                    subscriber.get(1), // First Name
                    subscriber.get(2), // Last Name
                    subscriber.get(3)  // Status
            );
            subscriberEntries.add(entry);
        }
    }

    /**
     * Refreshes the message table by reloading the scene.
     */
    public void refreshMessagesTable()
    {
        // Refresh the message table by reloading the scene
        SceneManager.switchScene("/gui/librarian/librarianUI/LibrarianUI_UI.fxml", "BLib.4 - Braude Library Management");
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
     * Navigates to the Search Books page.
     */
    @FXML
    private void goToSearch()
    {
        SceneManager.switchScene("/gui/common/search/Search_UI.fxml", "BLib.4 - Braude Library Management");
    }

    /**
     * Navigates to the New Borrow page.
     */
    @FXML
    private void goToNewBorrow()
    {
        SceneManager.switchScene("/gui/librarian/newBorrow/NewBorrow_UI.fxml", "BLib.4 - Braude Library Management");
    }

    /**
     * Navigates to the Add Subscriber page.
     */
    @FXML
    private void goToAddSubscriber()
    {
        SceneManager.switchScene("/gui/librarian/addSubscriber/AddSubscriber_UI.fxml", "BLib.4 - Braude Library Management");
    }

    /**
     * Navigates to the Library Reports page.
     */
    @FXML
    private void goToLibraryReports()
    {
        SceneManager.switchScene("/gui/librarian/libraryReports/LibraryReports_UI.fxml", "BLib.4 - Braude Library Management");
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