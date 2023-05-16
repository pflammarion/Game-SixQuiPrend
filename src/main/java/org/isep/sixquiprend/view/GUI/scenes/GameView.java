package org.isep.sixquiprend.view.GUI.scenes;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.isep.sixquiprend.model.Card;
import org.isep.sixquiprend.model.player.Player;

import java.util.List;

import javafx.scene.text.Text;


public class GameView {

    private final Button playButton;
    private final Button skipButton;
    private final Scene scene;
    private final Text playerNames;
    private final Label roundLabel;
    private final Label totalBullHeads;
    private final VBox boardPane;
    private Card selectedCard;


    public GameView() {
        skipButton = new Button("Skip");
        playButton = new Button("Play");
        playerNames = new Text();
        roundLabel = new Label();
        totalBullHeads = new Label();
        boardPane = new VBox();

        VBox vbox = new VBox(roundLabel, playerNames, playButton, skipButton, boardPane);
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

    public Button getPlayButton() {
        return playButton;
    }

    public Button getSkipButton() {
        return skipButton;
    }

    public void updatePlayers(List<Player> players) {
        StringBuilder playerNames = new StringBuilder();
        for (Player player : players) {
            playerNames.append(player.getName()).append("\n");
        }
        this.playerNames.setText(playerNames.toString());
    }

    public void updateBoard(List<List<Card>> board) {
        boardPane.getChildren().clear();

        for (List<Card> row : board) {
            HBox rowBox = new HBox();
            rowBox.setSpacing(10);
            rowBox.setAlignment(Pos.CENTER);

            for (Card card : row) {
                Label cardLabel = new Label(card.getNumber() + " (" + card.getBullHeads() + ")");
                cardLabel.setStyle("-fx-background-color: lightblue; -fx-padding: 5px;");

                rowBox.getChildren().add(cardLabel);
            }

            boardPane.getChildren().add(rowBox);
        }
    }

    public void updateRound(int round) {
        roundLabel.setText("Round: " + round);
    }

    public void updateTotalBullHeads(int totalBullHeadsInt) {
        String bullHeadsText = "Total Bull Heads: " + totalBullHeadsInt;
        this.totalBullHeads.setText(bullHeadsText);
    }

    public void setPlayerTurn(Player currentPlayer) {
        String playerName = currentPlayer.getName();

        this.playerNames.setStyle("-fx-font-weight: bold;");

        for (Node node : playerNames.getParent().getChildrenUnmodifiable()) {
            if (node instanceof Text) {
                Text text = (Text) node;
                if (text.getText().equals(playerName)) {
                    text.setStyle("-fx-font-weight: bold;");
                } else {
                    text.setStyle("-fx-font-weight: normal;");
                }
            }
        }
    }

    public void clearSelectedCard() {
        selectedCard = null;
    }

    public Card getSelectedCard() {
        return selectedCard;
    }
}
