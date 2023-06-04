package org.isep.sixquiprend;

import javafx.application.Application;
import javafx.stage.Stage;
import org.isep.sixquiprend.controller.GameController;
import org.isep.sixquiprend.view.GUI.SceneManager;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        SceneManager sceneManager = new SceneManager(stage);
        GameController game = new GameController(sceneManager);
        sceneManager.switchToScene("welcome");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
