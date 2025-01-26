package gui.librarian.subscriberBorrows;

import entities.logic.MessageType;
import gui.user.subscriberUI.BorrowEntry;
import gui.user.subscriberUI.OrderEntry;
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
import logic.communication.DataReceiver;
import logic.communication.SceneManager;
import logic.librarian.BorrowController;
import logic.user.Subscriber_Controller;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class SubscriberBorrows_Controller implements DataReceiver {

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
    private Text activeBorrowsTitle;
    @FXML
    private Text activeOrdersTitle;
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
    private TableColumn<BorrowEntry, Void> returnColumn;
    @FXML
    private TableColumn<BorrowEntry, Void> extendColumn;
    @FXML
    private TableColumn<BorrowEntry, Void> lostColumn;

    // Order Table FXML fields
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
    private BorrowController borrowController;
    private String userId;
    private ArrayList<ArrayList<String>> subscriberBorrows;
    private final ObservableList<BorrowEntry> borrowEntries = FXCollections.observableArrayList(); // List of borrow entries (dynamic)
    private final ObservableList<OrderEntry> orderEntries = FXCollections.observableArrayList(); // List of order entries (dynamic)
    private String selectedBorrowId;
    private String selectedDueDate;

    @Override
    public void receiveData(Object data) {
        if (data instanceof String) {
            this.userId = (String) data;
            activeBorrowsTitle.setText("Active Borrows of Subscriber " + userId + ":");
            // Populate the table
            loadBorrowsData(userId);
            borrowsTable.setItems(borrowEntries);

            activeOrdersTitle.setText("Active Orders of Subscriber " + userId + ":");
            // Populate the table
            loadOrderData(userId);
            orderTable.setItems(orderEntries);
        }
    }

    @FXML
    public void initialize() {
        // Get the singleton instance of Subscriber_Controller
        subscriberController = Subscriber_Controller.getInstance();
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

        // Add Return button
        returnColumn.setCellFactory(param -> new TableCell<BorrowEntry, Void>() {
            private final Button returnButton = new Button("Return");

            {
                // Set the CSS class for the button
                returnButton.getStyleClass().add("orange-orange-button");

                // Set preferred size for the button
                returnButton.setPrefHeight(50.0);
                returnButton.setPrefWidth(150.0);

                // Set the action for the button
                returnButton.setOnAction(event -> {
                    BorrowEntry entry = getTableView().getItems().get(getIndex());
                    String borrowId = entry.getBorrowId();
                    handleReturnAction(borrowId);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null); // Clear the cell for empty rows
                } else {
                    setGraphic(returnButton); // Add the button for non-empty rows
                }
            }
        });

        // Add Extend button
        extendColumn.setCellFactory(param -> new TableCell<BorrowEntry, Void>() {
            private final Button extendButton = new Button("Extend");

            {
                // Set the CSS class for the button
                extendButton.getStyleClass().add("orange-orange-button");

                // Set preferred size for the button
                extendButton.setPrefHeight(50.0);
                extendButton.setPrefWidth(150.0);

                // Set the action for the button
                extendButton.setOnAction(event -> {
                    BorrowEntry entry = getTableView().getItems().get(getIndex());
                    handleExtendAction(entry);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null); // Clear the cell for empty rows
                } else {
                    setGraphic(extendButton); // Add the button for non-empty rows
                }
            }
        });

        // Add Lost button
        lostColumn.setCellFactory(param -> new TableCell<BorrowEntry, Void>() {
            private final Button lostButton = new Button("Lost");

            {
                // Set the CSS class for the button
                lostButton.getStyleClass().add("orange-orange-button");

                // Set preferred size for the button
                lostButton.setPrefHeight(50.0);
                lostButton.setPrefWidth(150.0);

                // Set the action for the button
                lostButton.setOnAction(event -> {
                    BorrowEntry entry = getTableView().getItems().get(getIndex());
                    String borrowId = entry.getBorrowId();
                    handleLostAction(borrowId);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null); // Clear the cell for empty rows
                } else {
                    setGraphic(lostButton); // Add the button for non-empty rows
                }
            }
        });

        borrowsTable.setPlaceholder(new Text("No borrows to display.")); // Set the placeholder text
        bookTitleColumn.setCellFactory(column -> {
            return new TableCell<BorrowEntry, String>() {
                private final Text text = new Text();

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                    } else {
                        text.setText(item);
                        text.wrappingWidthProperty().bind(bookTitleColumn.widthProperty().subtract(10)); // Wrap text within column width
                        setGraphic(text);
                    }
                }
            };
        });

        borrowsTable.setFixedCellSize(70); // Set the row height
        borrowsTable.setSelectionModel(null); // Disable row selection
        extendColumn.setSortable(false); // Disable sorting for the "Extend" column
        returnColumn.setSortable(false); // Disable sorting for the "Return" column
        lostColumn.setSortable(false); // Disable sorting for the "Lost" column

        // Highlight rows based on due date
        borrowsTable.setRowFactory(tv -> new TableRow<BorrowEntry>() {
            @Override
            protected void updateItem(BorrowEntry item, boolean empty) {
                super.updateItem(item, empty);

                // Reset the style if the row is empty or null
                if (empty || item == null) {
                    setStyle("");
                } else {
                    // Parse the due date
                    LocalDate dueDate = LocalDate.parse(item.getDueDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    LocalDate today = LocalDate.now();

                    // Apply styles based on due date conditions
                    if (dueDate.isBefore(today)) {
                        // Hightlight the row if due date is in the past with a light red background
                        setStyle("-fx-background-color: #FFCCCC;");
                    } else {
                        // Reset to default if due date is in the future
                        setStyle("");
                    }
                }
            }
        });

        // Set custom cell factory for the dueDateColumn
        dueDateColumn.setCellFactory(column -> new TableCell<BorrowEntry, String>() {
            @Override
            protected void updateItem(String dueDate, boolean empty) {
                super.updateItem(dueDate, empty);

                // Clear the style for empty cells
                if (empty || dueDate == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(dueDate);

                    // Parse the due date and compare it with the current date
                    LocalDate parsedDueDate = LocalDate.parse(dueDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    LocalDate today = LocalDate.now();

                    if (parsedDueDate.isEqual(today)) {
                        // Highlight for due today
                        setStyle("-fx-font-weight: bold; -fx-text-fill: #525FE1;");
                    } else if (parsedDueDate.isBefore(today)) {
                        // Highlight for overdue
                        setStyle("-fx-font-weight: bold; -fx-text-fill: #E74C3C;");
                    } else {
                        // Reset to default if due date is in the future
                        setStyle("");
                    }
                }
            }
        });

        // Ensure the bookTitleColumn fills the remaining space
        bookTitleColumn.prefWidthProperty().bind(
                borrowsTable.widthProperty()
                        .subtract(170 * 4) // Subtract the total width of fixed columns
                        .subtract(140 * 3)
                        .subtract(2) // Subtract the border width
                        .subtract(borrowsTable.getItems().size() > 7 ? 20 : 0) // Subtract 20 if more than 7 rows for the scrollbar
        );

        // Set the factory for customizing day cells
        newReturnDatePicker.setDayCellFactory(datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

             // Disable dates before the current return date
                if (item.isBefore(LocalDate.parse(selectedDueDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")).plusDays(1))) {
                    setDisable(true);
                    setStyle("-fx-background-color: #EEEEEE; -fx-text-fill: #999999;");
                }
            }
        });

        // Order Table

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
        orderBookTitleColumn.setCellFactory(column -> {
            return new TableCell<OrderEntry, String>() {
                private final Text text = new Text();

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                    } else {
                        text.setText(item);
                        text.wrappingWidthProperty().bind(orderBookTitleColumn.widthProperty().subtract(10)); // Wrap text within column width
                        setGraphic(text);
                    }
                }
            };
        });

        orderTable.setFixedCellSize(70); // Set the row height
        orderTable.setSelectionModel(null); // Disable row selection

        //ToDo: fix the space between the columns
        // Ensure the bookTitleColumn fills the remaining space
        orderBookTitleColumn.prefWidthProperty().bind(
                orderTable.widthProperty()
                        .subtract(200 * 2) // Subtract the total width of fixed columns
                        .subtract(300)
                        .subtract(2) // Subtract the border width
                        .subtract(orderTable.getItems().size() > 7 ? 20 : 0) // Subtract 20 if more than 7 rows for the scrollbar
        );

    }

    private void loadBorrowsData(String userId) {
        // Get the subscriber's borrows
        ClientUI.chat.accept(new MessageType("110", userId));
        subscriberBorrows = ChatClient.listOfBorrows;

        for (ArrayList<String> borrow : subscriberBorrows) {
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

    private void loadOrderData(String userId)
    {
        // Get the subscriber's orders
        ClientUI.chat.accept(new MessageType("119", userId));

        ArrayList<ArrayList<String>> subscriberOrders = ChatClient.listOfOrders;

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

    private void handleReturnAction(String borrowId) {
        int returnOutcome = borrowController.returnBorrow(borrowId);
        if (returnOutcome == 1) {
            showInformationAlert("Return Successful", "The book has been returned successfully.");
            refreshBorrowsTable();
        } else if (returnOutcome == 2) {
            showWarningAlert("Return Successful", "The book has been returned successfully. However, the subscriber is now frozen because the return was more than a week overdue.");
            refreshBorrowsTable();
        } else if (returnOutcome == 3) {
            showErrorAlert("Return Error", "An error occurred while processing the return. Please try again.");
        }
    }

    private void handleExtendAction(BorrowEntry entry) {
        extendFormHBox.setVisible(true);
        extendBookTitle.setText("Select a new return date for " +
                entry.getBookTitle() + " (Copy ID: " + entry.getCopyId() + "):    ");
        selectedDueDate = entry.getDueDate();  
        selectedBorrowId = entry.getBorrowId();
    }

    private void handleLostAction(String borrowId) {
        if (borrowController.lostCopy(borrowId)) {
            showInformationAlert("Lost Successful", "The book has been marked as lost.");
            refreshBorrowsTable();
        } else {
            showErrorAlert("Lost Error", "An error occurred while marking the book as lost. Please try again.");
        }
    }

    /**
     * Handles the submit action for extending a borrow.
     */
    @FXML
    private void handleSubmitAction() {
        // Retrieve the selected date from the DatePicker
        LocalDate newReturnDate = newReturnDatePicker.getValue();
        if (newReturnDate == null) {
            // Show an error alert if no date is selected
            showErrorAlert("Invalid Date", "Please select a new return date.");
            return;
        }

        // Attempt to extend the borrow via the BorrowController
        boolean isExtended = borrowController.extendBorrow(selectedBorrowId, newReturnDate.toString());

        if (isExtended) {
            showInformationAlert("Extension Successful", "The borrow has been extended successfully.");
            refreshBorrowsTable();
            
        } else {
            showErrorAlert("Extension Denied", "The extension cannot be processed as there are previous reservations for the book.");
        }

        extendFormHBox.setVisible(false);
    }

    /**
     * Refreshes the borrow table with updated data.
     */
    private void refreshBorrowsTable() {
    	// Clear the previous entries before updating the table
        borrowEntries.clear(); 
        
        // Reload the borrows data
        loadBorrowsData(userId);
        loadOrderData(userId);
        
        // Set the updated list of borrows in the table
        borrowsTable.setItems(borrowEntries);
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

    private void showWarningAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
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
    private void goToLibraryReports() {
        SceneManager.switchScene("/gui/librarian/libraryReports/LibraryReports_UI.fxml", "BLib.4 - Braude Library Management");
    }

    @FXML
    private void exitApp() {
        ClientUI.chat.getClient().quit();
    }
}