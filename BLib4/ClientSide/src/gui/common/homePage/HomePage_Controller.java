package gui.common.homePage;

import entities.book.Book;
import entities.logic.MessageType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import logic.communication.ChatClient;
import logic.communication.ClientUI;

import java.util.List;

public class HomePage_Controller {

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
    public void initialize() {
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
//
//        List<Book> newestBooks = ChatClient.books;
//
//        ClientUI.chat.accept(new MessageType("121", null));
//
//        List<Book> popularBooks = ChatClient.books;
    }
}