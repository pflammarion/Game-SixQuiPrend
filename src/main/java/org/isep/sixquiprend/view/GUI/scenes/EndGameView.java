package org.isep.sixquiprend.view.GUI.scenes;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.isep.sixquiprend.model.player.Player;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class EndGameView {

    private final Button quitButton;
    private final Button restartButton;
    private final Label endLabel;
    private final Label winnerAnnounce;
    private final Label winner;
    private final Label scores;
    private final Scene scene;
    public EndGameView() {

        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/isep/sixquiprend/assets/img/background_accueil.jpg"))));
        this.quitButton = new Button("Quitter");
        this.restartButton = new Button("Rejouer !");

        this.scores = new Label();
        this.winner = new Label();
        this.endLabel = new Label("Fin du jeu");
        this.winnerAnnounce = new Label("Le gagnant est:");

        endLabel.getStyleClass().add("end_label");
        quitButton.getStyleClass().add("quit_button");
        restartButton.getStyleClass().add("restart_button");

        HBox endLabelHbox = new HBox(endLabel);
        endLabelHbox.setAlignment(Pos.TOP_CENTER);

        VBox winnerVBox = new VBox(winnerAnnounce, winner);
        winner.getStyleClass().add("winner_label");
        winnerVBox.setAlignment(Pos.CENTER);
        winnerVBox.setSpacing(10);


        HBox buttonsHBox = new HBox(quitButton, restartButton);
        buttonsHBox.setAlignment(Pos.CENTER);
        buttonsHBox.setSpacing(20);


        VBox sBox= new VBox(endLabelHbox, winnerVBox, scores, buttonsHBox);
        sBox.setSpacing(20);
        sBox.setAlignment(Pos.CENTER);

        AnchorPane anchorPane = new AnchorPane(imageView, sBox);
        anchorPane.setPrefSize(1200, 600);
        AnchorPane.setTopAnchor(sBox, 100.0);
        AnchorPane.setBottomAnchor(sBox, 100.0);
        AnchorPane.setLeftAnchor(sBox, 300.0);
        AnchorPane.setRightAnchor(sBox, 300.0);

        this.scene = new Scene(anchorPane);

        imageView.fitWidthProperty().bind(scene.widthProperty());
        imageView.fitHeightProperty().bind(scene.heightProperty());

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
        players.sort(Comparator.comparingInt(Player::getScore));
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
