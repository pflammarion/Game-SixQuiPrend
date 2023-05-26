package org.isep.sixquiprend.view.GUI.scenes;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.isep.sixquiprend.model.player.Player;

import java.util.List;

public class EndGameView {

    private final Button quitButton;
    private final Button restartButton;
    private final Label winner;
    private final Label scores;
    private final Scene scene;
    public EndGameView() {

        this.quitButton = new Button("Quit");
        this.restartButton = new Button("Restart");

        this.scores = new Label();
        this.winner = new Label();


        VBox vbox = new VBox(winner, scores, quitButton, restartButton);
        vbox.setSpacing(10);
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

    public Button getRestartButton() {
        return restartButton;
    }

    public void setScores(List<Player> players) {
        StringBuilder playerNames = new StringBuilder();
        for (Player player : players) {
            playerNames.append(player.getName()).append(" | score : ").append(player.getScore()).append("\n");
        }
        this.scores.setText(playerNames.toString());
    }

    public void setPlayerText(String text) {
        this.scores.setText(text);
    }

    public void setWinner(Player winner) {
        this.winner.setText(winner.getName());
    }

    public void setWinnerText(String winner) {
        this.winner.setText(winner);
    }
}
