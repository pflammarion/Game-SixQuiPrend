package org.isep.sixquiprend;

import javafx.application.Application;
import javafx.stage.Stage;

public class Start extends Application {

    // This start should launch the Introduction scene
    @Override
    public void start(Stage primaryStage) {
        // Add scene in the function: SceneManager scnManager = new SceneManager(primaryStage);

        // Add scene controller: new CounterController(scnManager)

        // Scene switch by writing name: scnManager.switchToScene("Introduction");

        primaryStage.show();
    }

    // Keep this.
    public static void main(String[] args) {
        launch(args);
    }
}
