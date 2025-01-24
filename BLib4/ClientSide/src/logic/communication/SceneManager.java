package logic.communication;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * SceneManager is a utility class that handles switching
 * between different FXML views within the primary Stage.
 */
public class SceneManager {

    private static Stage primaryStage;

    /**
     * Holds a reference to the last loaded FXML Controller.
     */
    private static Object currentController;

    /**
     * Sets the primary stage for the application.
     * Initializes it with an empty scene to avoid resetting
     * the stage when switching scenes later.
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
     * Returns the primary stage of the application.
     *
     * @return The primary Stage object.
     */
    public static Stage getStage() {
        return primaryStage;
    }

    /**
     * Returns the current Scene of the primary stage.
     *
     * @return The current Scene object.
     */
    public static Scene getCurrentScene() {
        return primaryStage.getScene();
    }

    /**
     * Returns the last loaded FXML controller.
     * This can be used to check which controller is active
     * or to call methods on that controller if needed.
     *
     * @return The current controller object (can be cast to the appropriate controller class).
     */
    public static Object getCurrentController() {
        return currentController;
    }

    /**
     * Switches the scene of the primary stage without altering the stage properties,
     * and updates the current controller reference.
     *
     * @param fxmlPath The path to the FXML file (relative to the SceneManager class).
     * @param title    The window title for the new scene.
     */
    public static void switchScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();

            // Store the current controller
            currentController = loader.getController();

            // Update the current scene's root without creating a new Scene object
            primaryStage.getScene().setRoot(root);

            // Update the stage title
            primaryStage.setTitle(title);

        } catch (Exception e) {
            System.err.println("Error loading scene: " + e.getMessage());
        }
    }

    /**
     * Switches the scene of the primary stage and passes data to the new controller.
     *
     * @param fxmlPath The path to the FXML file (relative to the SceneManager class).
     * @param title    The window title for the new scene.
     * @param data     The data to pass to the new controller (if the controller implements DataReceiver).
     */
    public static void switchSceneWithData(String fxmlPath, String title, Object data) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();

            // Store the current controller
            currentController = loader.getController();

            // Pass data to the controller if it implements DataReceiver
            if (currentController instanceof DataReceiver) {
                ((DataReceiver) currentController).receiveData(data);
            }

            // Update the current scene's root
            primaryStage.getScene().setRoot(root);

            // Update the stage title
            primaryStage.setTitle(title);

        } catch (Exception e) {
            System.err.println("Error loading scene with data: " + e.getMessage());
        }
    }
}