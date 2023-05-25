package org.isep.sixquiprend.view.GUI.scenes;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.isep.sixquiprend.model.player.Player;

import java.util.List;

public class LobbyView {

    private final Button quitButton;
    private final Button playButton;
    private final Text playerList;
    private final Scene scene;
    public LobbyView() {

        this.quitButton = new Button("Quitter le jeu en ligne");

        this.playerList = new Text();
        this.playButton = new Button("Lancer la partie");


        VBox vbox = new VBox(playerList, playButton, quitButton);
        vbox.setSpacing(50);
        vbox.setAlignment(Pos.CENTER);

        AnchorPane anchorPane = new AnchorPane(vbox);
        anchorPane.setPrefSize(1200, 600);
        AnchorPane.setTopAnchor(vbox, 100.0);
        AnchorPane.setBottomAnchor(vbox, 100.0);
        AnchorPane.setLeftAnchor(vbox, 300.0);
        AnchorPane.setRightAnchor(vbox, 300.0);

        this.scene = new Scene(anchorPane);

    }

    public Scene getScene() {
        return scene;
    }

    public Button getQuitButton() {
        return quitButton;
    }

    public Button getPlayButton() {
        return playButton;
    }

    public void setPlayers(List<Player> players) {
        StringBuilder playerNames = new StringBuilder();
        for (Player player : players) {
            playerNames.append(player.getName()).append("\n");
        }
        this.playerList.setText(playerNames.toString());
    }
}
