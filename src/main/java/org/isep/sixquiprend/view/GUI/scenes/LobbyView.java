package org.isep.sixquiprend.view.GUI.scenes;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.isep.sixquiprend.model.player.Player;

import java.util.List;
import java.util.Objects;

public class LobbyView {

    private final Button quitButton;
    private final Button playButton;
    private final Text playerList;
    private final Label waitingHost;
    private final Label players;
    private final Scene scene;
    public LobbyView() {


        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/isep/sixquiprend/assets/img/background_accueil.jpg"))));

        this.quitButton = new Button("Quitter le jeu en ligne");
        this.players = new Label ("Joueurs dans votre partie:");
        this.playerList = new Text();
        this.waitingHost = new Label("Attendez que l'hôte lance le jeu...");
        this.playButton = new Button("Lancer la partie");

        playButton.getStyleClass().add("big_button");
        quitButton.getStyleClass().add("quit_button");
        waitingHost.getStyleClass().add("waiting");
        playerList.getStyleClass().add("player_list");

        VBox waitingPlayers = new VBox(players, playerList, waitingHost);
        waitingPlayers.setSpacing(30);
        waitingPlayers.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(waitingPlayers, quitButton, playButton);
        vbox.setSpacing(5);
        vbox.setAlignment(Pos.CENTER);

        AnchorPane anchorPane = new AnchorPane(imageView, vbox);
        anchorPane.setPrefSize(1200, 600);
        AnchorPane.setTopAnchor(vbox, 100.0);
        AnchorPane.setBottomAnchor(vbox, 100.0);
        AnchorPane.setLeftAnchor(vbox, 300.0);
        AnchorPane.setRightAnchor(vbox, 300.0);


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

    public void setHost(boolean host){
        playButton.setVisible(host);
        waitingHost.setVisible(!host);
    }
}
