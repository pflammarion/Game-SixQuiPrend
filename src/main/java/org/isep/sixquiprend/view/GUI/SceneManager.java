package org.isep.sixquiprend.view.GUI;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class SceneManager {

    private final Stage stage;
    private final Map<String, Scene> scenes;
    private String currentSceneName;

    public SceneManager(Stage stage) {
        this.stage = stage;
        this.scenes = new HashMap<>();
    }

    public void addScene(String sceneName, Scene scene) {
        scenes.put(sceneName, scene);
    }

    public void switchToScene(String sceneName) {
        Scene scene = scenes.get(sceneName);

        if (scene == null) {
            throw new IllegalArgumentException("Scene not found: " + sceneName);
        }

        currentSceneName = sceneName;
        stage.setScene(scene);
    }

    public String getCurrentSceneName() {
        return currentSceneName;
    }

}
