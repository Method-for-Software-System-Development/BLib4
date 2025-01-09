package gui.common.homePage;

import entities.book.Book;
import entities.logic.MessageType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import logic.communication.ChatClient;
import logic.communication.ClientUI;

import java.util.List;

public class HomePage_Controller {

    @FXML
    private RadioButton subscriberRadioButton;
    @FXML
    private RadioButton librarianRadioButton;
    @FXML
    private ToggleGroup accountTypeGroup;
    @FXML
    private TextField idTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Button scanReaderCardButton;
    @FXML
    private ImageView scanReaderCardImageView;
    @FXML
    private Button loginButton;
    @FXML
    private ImageView loginImageView;
    @FXML
    private Button searchBooksButton;
    @FXML
    private ImageView searchBooksImageView;
    @FXML
    private Button exitButton;
    @FXML
    private ImageView exitImageView;
    @FXML
    private HBox newArrivalsHBox;
    @FXML
    private HBox mostBorrowedHBox;

    private List<Book> newArrivalBooks;
    private List<Book> mostBorrowedBooks;

    @FXML
    public void initialize() {
        accountTypeGroup = new ToggleGroup();
        subscriberRadioButton.setToggleGroup(accountTypeGroup);
        librarianRadioButton.setToggleGroup(accountTypeGroup);
        subscriberRadioButton.setSelected(true);

        scanReaderCardButton.setOnMouseEntered(event -> {
            scanReaderCardImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/qr_code_scanner_24dp_525FE1.png")));
        });

        scanReaderCardButton.setOnMouseExited(event -> {
            scanReaderCardImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/qr_code_scanner_24dp_FFFFFF.png")));
        });

        loginButton.setOnMouseEntered(event -> {
            loginImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/login_24dp_F86F03.png")));
        });

        loginButton.setOnMouseExited(event -> {
            loginImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/login_24dp_FFFFFF.png")));
        });

        searchBooksButton.setOnMouseEntered(event -> {
            searchBooksImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/search_24dp_525FE1.png")));
        });

        searchBooksButton.setOnMouseExited(event -> {
            searchBooksImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/search_24dp_FFFFFF.png")));
        });

        exitButton.setOnMouseEntered(event -> {
            exitImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/close_24dp_525FE1.png")));
        });

        exitButton.setOnMouseExited(event -> {
            exitImageView.setImage(new Image(getClass().getResourceAsStream("/gui/assets/icons/close_24dp_FFFFFF.png")));
        });

//        ClientUI.chat.accept(new MessageType("120", null));
//        newArrivalBooks = ChatClient.books;
//        populateBooksHBox(newArrivalsHBox, newArrivalBooks);
//
//        ClientUI.chat.accept(new MessageType("121", null));
//        mostBorrowedBooks = ChatClient.books;
//        populateBooksHBox(mostBorrowedHBox, mostBorrowedBooks);
    }

//    public void populateBooksHBox(HBox targetHBox, List<Book> books) {
//        targetHBox.getChildren().clear(); // Clear previous content
//
//        for (Book book : books) {
//            // Create a VBox for each book
//            VBox bookVBox = new VBox();
//            bookVBox.getStyleClass().add("book-box"); // CSS class for book container
//            bookVBox.setSpacing(10); // Spacing between elements
//
//            // Check if the book has an image
//            if (book.getImage() != null) {
//                // Create ImageView for the book cover
//                ImageView bookImageView = new ImageView(book.getImage());
//                bookImageView.setFitHeight(150); // Set image height
//                bookImageView.setFitWidth(100);  // Set image width
//                //bookImageView.setPreserveRatio(true);
//                bookVBox.getChildren().add(bookImageView);
//            } else {
//                // Create a placeholder rectangle styled with CSS
//                Rectangle placeholder = new Rectangle(100, 150); // Placeholder size
//                placeholder.getStyleClass().add("book-placeholder"); // CSS class for styling
//                Text placeholderText = new Text(book.getTitle());
//                placeholderText.getStyleClass().add("placeholder-text"); // CSS class for text inside placeholder
//                bookVBox.getChildren().addAll(placeholder, placeholderText);
//            }
//
//            // Create Text for the book title
//            Text bookTitle = new Text(book.getTitle());
//            bookTitle.getStyleClass().add("book-title");
//
//            // Create Text for the book author
//            Text bookAuthor = new Text(book.getAuthor());
//            bookTitle.getStyleClass().add("book-author");
//
//            // Create Text for the book subject
//            Text bookSubject = new Text(book.getSubject());
//            bookSubject.getStyleClass().add("book-subject");
//
//            // Add elements to the VBox
//            bookVBox.getChildren().addAll(bookTitle, bookAuthor, bookSubject);
//
//            // Add the VBox to the target HBox
//            targetHBox.getChildren().add(bookVBox);
//        }
//    }

    @FXML
    public void validate_login_form() {
        // Reset styles before validation
        idTextField.getStyleClass().remove("error-text-field");
        passwordTextField.getStyleClass().remove("error-text-field");

        boolean isValid = true;

        // Check if a radio button is selected
        RadioButton selectedRadioButton = (RadioButton) accountTypeGroup.getSelectedToggle();
        if (selectedRadioButton == null)
        {
            isValid = false;
        }

        // Check if ID field is empty
        if (idTextField.getText() == null || idTextField.getText().trim().isEmpty()) {
            idTextField.setPromptText("ID is required");
            idTextField.getStyleClass().add("error-text-field");
            isValid = false;
        }

        // Check if Password field is empty
        if (passwordTextField.getText() == null || passwordTextField.getText().trim().isEmpty()) {
            passwordTextField.setPromptText("Password is required");
            passwordTextField.getStyleClass().add("error-text-field");
            isValid = false;
        }

//        if (isValid)
//        {
//
//        }
    }
}