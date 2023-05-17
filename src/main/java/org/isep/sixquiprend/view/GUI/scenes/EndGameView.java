package org.isep.sixquiprend.view.GUI.scenes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.isep.sixquiprend.model.player.Player;

import java.util.List;

public class EndGameView {

    private final Button quitButton;
    private final Button restartButton;
    private final Scene scene;
    public EndGameView() {

        quitButton = new Button("quit");
        restartButton = new Button("restart");

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);


        //AnchorPane anchorPane = new AnchorPane(imageView, vbox);

        AnchorPane anchorPane = new AnchorPane(vbox);
        anchorPane.setPrefSize(1200, 600);
        AnchorPane.setTopAnchor(vbox, 100.0);
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

    public Button getQuitButton() {
        return quitButton;
    }

    public Button getRestartButton() {
        return restartButton;
    }

    public void setTotalBullHeads(int number) {

    }
    public void setWinner(Player winner) {

    }
}
