package org.isep.sixquiprend.view.GUI.scenes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import org.isep.sixquiprend.model.Card;
import org.isep.sixquiprend.model.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javafx.scene.text.Text;


public class GameView {

    private final Button playButton;
    private final Scene scene;
    private final Text playerNames;
    private final Label roundLabel;
    private final VBox boardPane;
    private Label selectedPlayer;
    private ListView<Card> hand;


    public GameView() {
        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/isep/sixquiprend/assets/img/background_accueil.jpg"))));
        playButton = new Button("Play");
        playerNames = new Text();
        roundLabel = new Label();
        boardPane = new VBox();
        selectedPlayer = new Label();

        this.hand = new ListView<>();
        hand.setMaxSize(200, 200);

        selectedPlayer.getStyleClass().add("selected_player");

        playerNames.getStyleClass().add("player_names");
        playerNames.setTextAlignment(TextAlignment.RIGHT);
        boardPane.setAlignment(Pos.TOP_LEFT);



        HBox gameInfosHBox = new HBox();
        HBox.setHgrow(selectedPlayer, Priority.ALWAYS);
        HBox.setHgrow(playerNames, Priority.ALWAYS);
        selectedPlayer.setMaxWidth(Double.MAX_VALUE);
        playerNames.maxWidth(Double.MAX_VALUE);
        gameInfosHBox.getChildren().addAll(selectedPlayer,playerNames);
        gameInfosHBox.setAlignment(Pos.TOP_CENTER);
        gameInfosHBox.setPrefWidth(Double.MAX_VALUE);

        VBox gameHeader = new VBox(roundLabel, gameInfosHBox);
        gameHeader.setAlignment(Pos.TOP_CENTER);

        HBox gameHeaderHBox = new HBox(gameHeader);
        gameHeaderHBox.setAlignment(Pos.TOP_CENTER);

        VBox gameInfosVBox = new VBox(gameHeader,boardPane);

        gameInfosVBox.getStyleClass().add("game-infos-vbox");

        VBox vbox = new VBox(gameInfosVBox);

        AnchorPane anchorPane = new AnchorPane(imageView, gameInfosVBox);
        anchorPane.setPrefSize(1200, 600);
        AnchorPane.setTopAnchor(gameInfosVBox, 100.0);
        AnchorPane.setBottomAnchor(gameInfosVBox, 100.0);
        AnchorPane.setLeftAnchor(gameInfosVBox, 300.0);
        AnchorPane.setRightAnchor(gameInfosVBox, 300.0);

        this.scene = new Scene(anchorPane);
    }

    public Scene getScene() {
        return scene;
    }

    public Button getPlayButton() {
        return playButton;
    }

    public void updatePlayers(List<Player> players) {
        StringBuilder playerNames = new StringBuilder();
        for (Player player : players) {
            playerNames.append(player.getName()).append(" | score : ").append(player.getScore()).append("\n");
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

    public void setPlayerTurn(Player currentPlayer) {
        String playerName = currentPlayer.getName();
        List<Card> hand = currentPlayer.getHand();

        ObservableList<Card> observableList = FXCollections.observableArrayList(hand);
        this.hand.setItems(observableList);

        this.playerNames.setStyle("-fx-font-weight: bold;");
        this.selectedPlayer.setText(playerName);

    }

    public Card getSelectedCard() {
        MultipleSelectionModel<Card> selectionModel = this.hand.getSelectionModel();

        return selectionModel.getSelectedItem();
    }
}
