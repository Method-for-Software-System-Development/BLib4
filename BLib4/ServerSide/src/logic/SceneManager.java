package logic;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager
{

    private static Stage primaryStage;

    /**
     * Sets the primary stage for the application.
     *
     * @param stage The primary stage to set.
     */
    public static void setStage(Stage stage) {
        primaryStage = stage;

        // Set an initial empty scene to avoid resetting the stage on scene switch
        Scene initialScene = new Scene(new Parent() {});
        primaryStage.setScene(initialScene);
    }

    /**
     * Switches the scene of the primary stage without altering stage properties.
     *
     * @param fxmlPath The path to the FXML file.
     * @param title    The title for the new scene.
     */
    public static void switchScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();

            // Update the current scene's root without resetting the stage
            primaryStage.getScene().setRoot(root);
            primaryStage.setTitle(title);
        } catch (Exception e) {
            System.out.println("Error loading scene: " + e.getMessage());
        }
    }
}