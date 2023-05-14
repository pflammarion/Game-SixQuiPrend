package org.isep.sixquiprend.controller;

import javafx.application.Platform;
import org.isep.sixquiprend.view.GUI.SceneManager;
import org.isep.sixquiprend.view.GUI.scenes.WelcomeView;

public class GameController {
    private SceneManager sceneManager;
    private WelcomeView welcomeView;
    public GameController(SceneManager sceneManager){
        this.sceneManager = sceneManager;
        this.welcomeView = new WelcomeView();
        eventListener();
    }
    private void eventListener(){
        // to add a scene on construct do :
        // sceneManager.addScene("sceneName", myScene.getScene());
        sceneManager.addScene("welcome", welcomeView.getScene());

        welcomeView.getButtonQuit().setOnAction(event -> {
            Platform.exit();
            System.exit(0);
        });

    }
}
