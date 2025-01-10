package logic.communication;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {

    private static Stage primaryStage;

    /**
     * Sets the primary stage for the application.
     *
     * @param stage The primary stage to set.
     */
    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    /**
     * Returns the primary stage of the application.
     *
     * @return The primary stage.
     */
    public static Stage getStage() {
        return primaryStage;
    }

    /**
     * Switches the scene of the primary stage.
     *
     * @param fxmlPath The path to the FXML file.
     * @param title    The title for the new scene.
     */
    public static void switchScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            primaryStage.setScene(scene);
            primaryStage.setTitle(title);
            primaryStage.show();
        } catch (Exception e) {
            System.out.println("Error loading scene: " + e.getMessage());
        }
    }
}