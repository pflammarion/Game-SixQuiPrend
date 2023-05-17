package org.isep.sixquiprend.view.GUI.scenes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.isep.sixquiprend.model.Card;
import org.isep.sixquiprend.model.player.Player;

import java.util.ArrayList;
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
    private Label selectedPlayer;
    private ListView<Card> hand;


    public GameView() {
        skipButton = new Button("Skip");
        playButton = new Button("Play");
        playerNames = new Text();
        roundLabel = new Label();
        totalBullHeads = new Label();
        boardPane = new VBox();
        selectedPlayer = new Label();
        selectedPlayer.setStyle("-fx-background-color: lightblue; -fx-padding: 5px;");

        this.hand = new ListView<>();
        hand.setMaxSize(200, 200);

        VBox vbox = new VBox(selectedPlayer, roundLabel, playerNames, hand, playButton, skipButton, boardPane);
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
        List<Card> hand = currentPlayer.getHand();

        ObservableList<Card> observableList = FXCollections.observableArrayList(hand);
        this.hand.setItems(observableList);

        this.playerNames.setStyle("-fx-font-weight: bold;");
        this.selectedPlayer.setText(playerName);

    }

    public List<Object> getSelectedCard() {
        List<Object> playedCard = new ArrayList<>();
        MultipleSelectionModel<Card> selectionModel = this.hand.getSelectionModel();
        Card selectedCard = selectionModel.getSelectedItem();

        if (selectedCard != null) {
            playedCard.add(selectedCard);
            // TODO choisir sa colonne
            playedCard.add(1);
        }

        return playedCard;
    }
}
