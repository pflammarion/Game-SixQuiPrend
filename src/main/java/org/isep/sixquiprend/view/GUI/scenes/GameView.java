package org.isep.sixquiprend.view.GUI.scenes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
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
    private HBox handHBox;


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

        playerNames.getStyleClass().add("player_name");
        playerNames.setTextAlignment(TextAlignment.RIGHT);


        VBox roundLabelVBox = new VBox(roundLabel);
        roundLabelVBox.setAlignment(Pos.CENTER);

        boardPane.setAlignment(Pos.BASELINE_LEFT);

        VBox playerPaneVBox = new VBox(selectedPlayer, boardPane);
        playerPaneVBox.setSpacing(40);
        playerPaneVBox.setAlignment(Pos.BASELINE_LEFT);

        HBox gameInfosHBox = new HBox();
        HBox.setHgrow(playerPaneVBox, Priority.ALWAYS);
        playerPaneVBox.setMaxWidth(Double.MAX_VALUE);
        playerNames.maxWidth(Double.MAX_VALUE);
        gameInfosHBox.getChildren().addAll(playerPaneVBox,playerNames);
        gameInfosHBox.setAlignment(Pos.TOP_LEFT);
        gameInfosHBox.setSpacing(40);

        this.handHBox = new HBox();


        
        VBox newvbox = new VBox(gameInfosHBox, handHBox, playButton);
        newvbox.setSpacing(40);
        newvbox.setAlignment(Pos.CENTER);



        AnchorPane anchorPane = new AnchorPane(imageView, newvbox);
        anchorPane.setPrefSize(1200, 600);
        AnchorPane.setTopAnchor(newvbox, 100.0);
        AnchorPane.setBottomAnchor(newvbox, 100.0);
        AnchorPane.setLeftAnchor(newvbox, 300.0);
        AnchorPane.setRightAnchor(newvbox, 300.0);

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
            rowBox.setAlignment(Pos.BOTTOM_LEFT);

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
        List<Card> playerHand = currentPlayer.getHand();
        handHBox.getChildren().clear();

        for (Card card : playerHand) {
            ImageView cardImage = new ImageView();
            cardImage.getStyleClass().add("card");
            String imagePath = ("/org/isep/sixquiprend/assets/img/cards/"+ card.getNumber() +".png");
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            cardImage.setImage(image);
            cardImage.setFitHeight(120);
            handHBox.getChildren().add(cardImage);
        }

        this.playerNames.setStyle("-fx-font-weight: bold;");
        this.selectedPlayer.setText(playerName);

    }

    public Card getSelectedCard() {
        MultipleSelectionModel<Card> selectionModel = this.hand.getSelectionModel();

        return selectionModel.getSelectedItem();
    }
}
