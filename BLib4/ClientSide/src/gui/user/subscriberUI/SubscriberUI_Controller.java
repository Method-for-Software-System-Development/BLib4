package gui.user.subscriberUI;

import entities.logic.MessageType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import logic.communication.ChatClient;
import logic.communication.ClientUI;
import logic.communication.SceneManager;
import logic.user.Subscriber_Controller;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class SubscriberUI_Controller
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
    @FXML
    private Button exitButton;
    @FXML
    private ImageView exitImageView;
    @FXML
    private HBox extendFormHBox;
    @FXML
    private Text extendBookTitle;
    @FXML
    private DatePicker newReturnDatePicker;

    // Borrows Table FXML fields
    @FXML
    private TableView<BorrowEntry> borrowsTable;
    @FXML
    private TableColumn<BorrowEntry, String> borrowIdColumn;
    @FXML
    private TableColumn<BorrowEntry, String> copyIdColumn;
    @FXML
    private TableColumn<BorrowEntry, String> bookTitleColumn;
    @FXML
    private TableColumn<BorrowEntry, String> borrowDateColumn;
    @FXML
    private TableColumn<BorrowEntry, String> dueDateColumn;
    @FXML
    private TableColumn<BorrowEntry, Void> extendColumn;

    // Order Table FXML fields
    @FXML
    private Text activeOrdersTitle;
    @FXML
    private TableView<OrderEntry> orderTable;
    @FXML
    private TableColumn<OrderEntry, String> orderIdColumn;
    @FXML
    private TableColumn<OrderEntry, String> orderBookIdColumn;
    @FXML
    private TableColumn<OrderEntry, String> orderBookTitleColumn;
    @FXML
    private TableColumn<OrderEntry, String> orderDateColumn;

    // Fields
    private Subscriber_Controller subscriberController;
    private ArrayList<ArrayList<String>> subscriberBorrows;
    private ArrayList<ArrayList<String>> subscriberOrders;
    private final ObservableList<BorrowEntry> borrowEntries = FXCollections.observableArrayList(); // List of borrow entries (dynamic)
    private final ObservableList<OrderEntry> orderEntries = FXCollections.observableArrayList(); // List of order entries (dynamic)
    private String selectedBorrowId;
    private String selectedDueDate;

    /**
     * Initializes the Subscriber UI.
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

        searchBooksButton.setOnMouseEntered(event ->
        {
            searchBooksImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/search_24dp_525FE1.png")));
        });
        searchBooksButton.setOnMouseExited(event ->
        {
            searchBooksImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/search_24dp_FFFFFF.png")));
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

        exitButton.setOnMouseEntered(event ->
        {
            exitImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/close_24dp_525FE1.png")));
        });
        exitButton.setOnMouseExited(event ->
        {
            exitImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/close_24dp_FFFFFF.png")));
        });

        // Get the subscriber's borrows
        ClientUI.chat.accept(new MessageType("110", subscriberController.getLoggedSubscriber().getId()));
        subscriberBorrows = ChatClient.listOfBorrows;

        // Add rounded corners to the TableView
        Rectangle clip = new Rectangle();
        clip.setArcWidth(20);  // Set horizontal corner radius
        clip.setArcHeight(20); // Set vertical corner radius
        // Bind the clip's size to the TableView's size
        clip.widthProperty().bind(borrowsTable.widthProperty());
        clip.heightProperty().bind(borrowsTable.heightProperty());
        // Apply the clip to the TableView
        borrowsTable.setClip(clip);

        // Initialize table columns
        borrowIdColumn.setCellValueFactory(data -> data.getValue().borrowIdProperty());
        copyIdColumn.setCellValueFactory(data -> data.getValue().copyIdProperty());
        bookTitleColumn.setCellValueFactory(data -> data.getValue().bookTitleProperty());
        borrowDateColumn.setCellValueFactory(data -> data.getValue().borrowDateProperty());
        dueDateColumn.setCellValueFactory(data -> data.getValue().dueDateProperty());

        // Add Extend button
        extendColumn.setCellFactory(param -> new TableCell<BorrowEntry, Void>()
        {
            private final Button extendButton = new Button("Extend");

            {
                // Set the CSS class for the button
                extendButton.getStyleClass().add("orange-orange-button");

                // Set preferred size for the button
                extendButton.setPrefHeight(50.0);
                extendButton.setPrefWidth(150.0);

                // Set the action for the button
                extendButton.setOnAction(event ->
                {
                    BorrowEntry entry = getTableView().getItems().get(getIndex());
                    LocalDate dueDate = LocalDate.parse(entry.getDueDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    LocalDate today = LocalDate.now();

                    // Check if the due date is more than a week from today
                    if (dueDate.isAfter(today.plusWeeks(1)))
                    {
                        showErrorAlert("Extend Not Allowed", "Extensions can only be requested within a week of the due date.");
                    }
                    else
                    {
                        handleExtendAction(entry);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty)
            {
                super.updateItem(item, empty);

                if (empty)
                {
                    setGraphic(null); // Remove the button for empty rows
                }
                else
                {
                    BorrowEntry entry = getTableView().getItems().get(getIndex());
                    LocalDate dueDate = LocalDate.parse(entry.getDueDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    LocalDate today = LocalDate.now();

                    // Disable and style the button if the due date is more than a week from today
                    if (dueDate.isAfter(today.plusWeeks(1)))
                    {
                        extendButton.getStyleClass().add("disabled-button");
                    }
                    else
                    {
                        extendButton.getStyleClass().remove("disabled-button");
                    }
                    setGraphic(extendButton); // Add the button for non-empty rows
                }
            }
        });

        borrowsTable.setPlaceholder(new Text("No borrows to display.")); // Set the placeholder text

        // Set custom cell factory for the bookTitleColumn
        bookTitleColumn.setCellFactory(column ->
        {
            return new TableCell<BorrowEntry, String>()
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
                        text.setTextAlignment(TextAlignment.CENTER);
                        text.wrappingWidthProperty().bind(bookTitleColumn.widthProperty().subtract(10)); // Wrap text within column width
                        setGraphic(text);
                    }
                }
            };
        });

        borrowsTable.setFixedCellSize(70); // Set the row height
        borrowsTable.setSelectionModel(null); // Disable row selection
        extendColumn.setSortable(false); // Disable sorting for the "Extend" column

        // Highlight rows based on due date
        borrowsTable.setRowFactory(tv -> new TableRow<BorrowEntry>()
        {
            @Override
            protected void updateItem(BorrowEntry item, boolean empty)
            {
                super.updateItem(item, empty);

                // Reset the style if the row is empty or null
                if (empty || item == null)
                {
                    setStyle("");
                }
                else
                {
                    // Parse the due date
                    LocalDate dueDate = LocalDate.parse(item.getDueDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    LocalDate today = LocalDate.now();

                    // Apply styles based on due date conditions
                    if (dueDate.isBefore(today))
                    {
                        // Hightlight the row if due date is in the past with a light red background
                        setStyle("-fx-background-color: #FFCCCC;");
                    }
                    else
                    {
                        // Reset to default if due date is in the future
                        setStyle("");
                    }
                }
            }
        });

        // Set custom cell factory for the dueDateColumn
        dueDateColumn.setCellFactory(column -> new TableCell<BorrowEntry, String>()
        {
            @Override
            protected void updateItem(String dueDate, boolean empty)
            {
                super.updateItem(dueDate, empty);

                // Clear the style for empty cells
                if (empty || dueDate == null)
                {
                    setText(null);
                    setStyle("");
                }
                else
                {
                    setText(dueDate);

                    // Parse the due date and compare it with the current date
                    LocalDate parsedDueDate = LocalDate.parse(dueDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    LocalDate today = LocalDate.now();

                    if (parsedDueDate.isEqual(today))
                    {
                        // Highlight for due today
                        setStyle("-fx-font-weight: bold; -fx-text-fill: #525FE1;");
                    }
                    else if (parsedDueDate.isBefore(today))
                    {
                        // Highlight for overdue
                        setStyle("-fx-font-weight: bold; -fx-text-fill: #E74C3C;");
                    }
                    else
                    {
                        // Reset to default if due date is in the future
                        setStyle("");
                    }
                }
            }
        });

        // Populate the table
        loadBorrowsData();
        borrowsTable.setItems(borrowEntries);

        // Ensure the bookTitleColumn fills the remaining space
        bookTitleColumn.prefWidthProperty().bind(
                borrowsTable.widthProperty()
                        .subtract(170 * 5) // Subtract the total width of fixed columns
                        .subtract(2) // Subtract the border width
                        .subtract(borrowsTable.getItems().size() > 4 ? 20 : 0) // Subtract 20 if more than 4 rows for the scrollbar
        );

        // Set the factory for customizing day cells
        newReturnDatePicker.setDayCellFactory(datePicker -> new DateCell()
        {
            @Override
            public void updateItem(LocalDate item, boolean empty)
            {
                super.updateItem(item, empty);

                // Disable dates before the current return date and two weeks after
                if (item.isBefore(LocalDate.parse(selectedDueDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")).plusDays(1)) || item.isAfter(LocalDate.parse(selectedDueDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")).plusWeeks(2)))
                {
                    setDisable(true);
                    setStyle("-fx-background-color: #EEEEEE; -fx-text-fill: #999999;");
                }
            }
        });

        // *** Order Table ***

        // Get the subscriber's orders
        ClientUI.chat.accept(new MessageType("119", subscriberController.getLoggedSubscriber().getId()));
        subscriberOrders = ChatClient.listOfOrders;

        // Add rounded corners to the TableView
        Rectangle clip1 = new Rectangle();
        clip1.setArcWidth(20);  // Set horizontal corner radius
        clip1.setArcHeight(20); // Set vertical corner radius
        // Bind the clip's size to the TableView's size
        clip1.widthProperty().bind(orderTable.widthProperty());
        clip1.heightProperty().bind(orderTable.heightProperty());
        // Apply the clip to the TableView
        orderTable.setClip(clip1);

        // Initialize table columns
        orderIdColumn.setCellValueFactory(data -> data.getValue().orderIdProperty());
        orderBookIdColumn.setCellValueFactory(data -> data.getValue().bookIdProperty());
        orderBookTitleColumn.setCellValueFactory(data -> data.getValue().bookTitleProperty());
        orderDateColumn.setCellValueFactory(data -> data.getValue().orderDateProperty());

        orderTable.setPlaceholder(new Text("No orders to display.")); // Set the placeholder text

        // Set custom cell factory for the bookTitleColumn
        orderBookTitleColumn.setCellFactory(column ->
        {
            return new TableCell<OrderEntry, String>()
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
                        text.setTextAlignment(TextAlignment.CENTER);
                        text.wrappingWidthProperty().bind(orderBookTitleColumn.widthProperty().subtract(10)); // Wrap text within column width
                        setGraphic(text);
                    }
                }
            };
        });

        orderTable.setFixedCellSize(70); // Set the row height
        orderTable.setSelectionModel(null); // Disable row selection

        // Populate the table
        loadOrderData();
        orderTable.setItems(orderEntries);

        // Ensure the bookTitleColumn fills the remaining space
        orderBookTitleColumn.prefWidthProperty().bind(
                orderTable.widthProperty()
                        .subtract(170 * 2) // Subtract the total width of fixed columns
                        .subtract(220)
                        .subtract(2) // Subtract the border width
                        .subtract(orderTable.getItems().size() > 3 ? 20 : 0) // Subtract 20 if more than 3 rows for the scrollbar
        );
    }

    /**
     * Loads the borrow data into the table.
     */
    private void loadBorrowsData()
    {
        for (ArrayList<String> borrow : subscriberBorrows)
        {
            BorrowEntry entry = new BorrowEntry(
                    borrow.get(0), // borrow id
                    borrow.get(1), // copy id
                    borrow.get(2), // book title
                    borrow.get(3), // borrow date
                    borrow.get(4)  // due date
            );
            borrowEntries.add(entry);
        }
    }

    /**
     * Loads the order data into the table.
     */
    private void loadOrderData()
    {
        for (ArrayList<String> order : subscriberOrders)
        {
            OrderEntry entry = new OrderEntry(
                    order.get(0), // order id
                    order.get(1), // book id
                    order.get(2), // book title
                    order.get(3)  // order date
            );
            orderEntries.add(entry);
        }
    }

    /**
     * Handles the extent action for a borrow entry.
     *
     * @param entry The borrow entry to extend.
     */
    private void handleExtendAction(BorrowEntry entry)
    {
        extendFormHBox.setVisible(true);
        extendFormHBox.setManaged(true);
        activeOrdersTitle.setVisible(false);
        orderTable.setVisible(false);
        orderTable.setManaged(false);

        extendBookTitle.setText("Select a new return date for " +
                entry.getBookTitle() + " (Copy ID: " + entry.getCopyId() + "):    ");
        selectedBorrowId = entry.getBorrowId();
        selectedDueDate = entry.getDueDate();
    }

    /**
     * Handles the submit action for extending a borrow.
     */
    @FXML
    private void handleSubmitAction()
    {
        // Retrieve the selected date from the DatePicker
        LocalDate newReturnDate = newReturnDatePicker.getValue();
        if (newReturnDate == null)
        {
            // Show an error alert if no date is selected
            showErrorAlert("Invalid Date", "Please select a new return date.");
            return;
        }

        // Convert the selected LocalDate to java.sql.Date
        Date sqlNewReturnDate = Date.valueOf(newReturnDate);

        // Attempt to extend the borrow via the subscriberController
        boolean isExtended = subscriberController.extendBorrowBySubscriber(selectedBorrowId, sqlNewReturnDate);

        if (isExtended)
        {
            // Show a success message if the extension is approved
            showInformationAlert("Extension Approved", "The extension has been approved.");
            refreshBorrowsTable();
        }
        else
        {
            // Show an error message if the extension is denied
            showErrorAlert("Extension Denied", "The extension cannot be processed as there are previous reservations for the book. Please return the copy on the due date.");
        }

        extendFormHBox.setVisible(false);
        extendFormHBox.setManaged(false);
        activeOrdersTitle.setVisible(true);
        orderTable.setVisible(true);
        orderTable.setManaged(true);
    }

    /**
     * Refreshes the borrow table with updated data.
     */
    private void refreshBorrowsTable()
    {
        // Refresh the borrows table by reloading the scene
        SceneManager.switchScene("/gui/user/subscriberUI/SubscriberUI_UI.fxml", "BLib.4 - Braude Library Management");
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
     * Navigates to the Search Page.
     */
    @FXML
    private void goToSearch()
    {
        SceneManager.switchScene("/gui/common/search/Search_UI.fxml", "BLib.4 - Braude Library Management");
    }

    /**
     * Navigates to the View History Page.
     */
    @FXML
    private void goToViewHistory()
    {
        SceneManager.switchScene("/gui/user/viewHistory/ViewHistory_UI.fxml", "BLib.4 - Braude Library Management");
    }

    /**
     * Navigates to the Edit Profile Page.
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
