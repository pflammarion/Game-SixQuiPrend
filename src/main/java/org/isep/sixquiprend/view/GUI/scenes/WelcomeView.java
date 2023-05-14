package org.isep.sixquiprend.view.GUI.scenes;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class WelcomeView {

    // Déclarer les éléments ici
    private final Button buttonQuit;
    private final Scene scene;
    public WelcomeView() {

        //ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/isep/harrypotter/assets/img/background/home-background.jpg"))));


        Label helloLabel = new Label("Hello");

        this.buttonQuit = new Button("Quit");

        VBox vbox = new VBox(helloLabel, buttonQuit);
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);


        //AnchorPane anchorPane = new AnchorPane(imageView, vbox);

        AnchorPane anchorPane = new AnchorPane(vbox);
        anchorPane.setPrefSize(1200, 600);
        AnchorPane.setBottomAnchor(vbox, 100.0);
        AnchorPane.setLeftAnchor(vbox, 300.0);
        AnchorPane.setRightAnchor(vbox, 300.0);

        this.scene = new Scene(anchorPane);

        //imageView.fitWidthProperty().bind(scene.widthProperty());
        //imageView.fitHeightProperty().bind(scene.heightProperty());
    }

    public Scene getScene() {
        return scene;
    }

    public Button getButtonQuit() {
        return buttonQuit;
    }
}
