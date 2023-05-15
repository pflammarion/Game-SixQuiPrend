package org.isep.sixquiprend.view.GUI;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.*;

public class SceneManager {
    private final Stage stage;
    private final Map<String, Scene> scenes;
    private final List<String> viewQueue;
    private int currentViewIndex;

    public SceneManager(Stage stage) {
        this.stage = stage;
        this.scenes = new HashMap<>();
        this.viewQueue = new ArrayList<>();
        this.currentViewIndex = -1;
        stage.setTitle("Six qui prend");
    }

    public void addScene(String sceneName, Scene scene) {
        scenes.put(sceneName, scene);
    }

    public void switchToScene(String sceneName) {
        Scene scene = scenes.get(sceneName);

        if (scene == null) {
            throw new IllegalArgumentException("Scene not found: " + sceneName);
        }

        viewQueue.remove(sceneName);
        currentViewIndex = viewQueue.indexOf(sceneName);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/org/isep/sixquiprend/assets/css/style.css")).toExternalForm());
        stage.setScene(scene);
    }

    public void addViewToQueue(String sceneName) {
        viewQueue.add(sceneName);
    }

    public void switchToNextScene() {
        if (viewQueue.isEmpty()) {
            throw new IllegalStateException("View queue is empty");
        }

        currentViewIndex++;
        if (currentViewIndex >= viewQueue.size()) {
            currentViewIndex = 0;
        }
        String currentView = viewQueue.get(currentViewIndex);
        viewQueue.remove(currentView);
        switchToScene(currentView);
    }

    public String getCurrentSceneName() {
        return viewQueue.get(currentViewIndex);
    }

    public String getNextSceneName() {
        int nextIndex = currentViewIndex + 1;
        if (nextIndex >= viewQueue.size()) {
            nextIndex = 0;
        }
        return viewQueue.get(nextIndex);
    }
}
