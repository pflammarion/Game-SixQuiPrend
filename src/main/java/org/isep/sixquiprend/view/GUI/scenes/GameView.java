package org.isep.sixquiprend.view.GUI.scenes;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.isep.sixquiprend.model.Card;
import org.isep.sixquiprend.model.player.Player;

import java.util.List;

public class GameView {

    private final Button playButton;
    private final Button skipButton;
    private final Scene scene;
    public GameView() {

        skipButton = new Button("skip");
        playButton= new Button("play");

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
    public Button getPlayButton() {
        return playButton;
    }

    public Button getSkipButton() {
        return skipButton;
    }
    public void updatePlayers(List<Player> players) {
        // Update the UI to display the players
        // You can use labels, tables, or any other appropriate UI elements
    }

    public void updateBoard(List<List<Card>> board) {
        // Update the UI to display the board
        // You can use labels, tables, or any other appropriate UI elements
    }

    public void updateRound(int round) {
        // Update the UI to display the current round
        // You can use labels or any other appropriate UI elements
    }

    public void updateTotalBullHeads(int totalBullHeads) {
        // Update the UI to display the total bull heads
        // You can use labels or any other appropriate UI elements
    }

    public void setPlayerTurn(Player currentPlayer) {
        // Update the UI to indicate the current player's turn
        // You can use labels, highlighting, or any other appropriate UI elements
    }

    public void clearSelectedCard() {

    }
    public Card getSelectedCard() {
        return new Card(11, 1);
    }
}