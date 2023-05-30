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
    private HBox handHBox;
    private Card selectedCard;


    public GameView() {
        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/isep/sixquiprend/assets/img/background_accueil.jpg"))));
        playButton = new Button("Play");
        playerNames = new Text();
        roundLabel = new Label();
        boardPane = new VBox();
        selectedPlayer = new Label();

        selectedPlayer.getStyleClass().add("selected_player");

        playerNames.getStyleClass().add("player_name");
        playerNames.setTextAlignment(TextAlignment.RIGHT);

        boardPane.setAlignment(Pos.CENTER_LEFT);
        boardPane.setMaxWidth(500.0);
        boardPane.setSpacing(10);


        HBox gameInfosHBox = new HBox(playerNames, selectedPlayer);
        gameInfosHBox.setMaxWidth(1500);
        gameInfosHBox.setSpacing(1000);

        this.handHBox = new HBox();
        handHBox.setAlignment(Pos.CENTER);
        handHBox.setSpacing(10);
        handHBox.setMinWidth(880.0);
        handHBox.setMaxWidth(880.0);


        VBox mainvbox = new VBox(roundLabel, gameInfosHBox, boardPane, handHBox, playButton);
        mainvbox.setMinWidth(1400);
        mainvbox.setMinHeight(800);
        mainvbox.setSpacing(20);
        mainvbox.setAlignment(Pos.CENTER);


        AnchorPane anchorPane = new AnchorPane(imageView, mainvbox);
        anchorPane.setPrefSize(1400, 800);
        AnchorPane.setTopAnchor(mainvbox, 50.0);
        AnchorPane.setLeftAnchor(mainvbox, 150.0);

        this.scene = new Scene(anchorPane);
    }

    public Scene getScene() {
        return scene;
    }

    public Button getPlayButton() {
        return playButton;
    }

    public void updatePlayers(List<Player> players) {
        StringBuilder playerNamesText = new StringBuilder();
        for (Player player : players) {
            playerNamesText.append(player.getName()).append(" | score : ").append(player.getScore()).append("\n");
        }
        this.playerNames.setText(playerNamesText.toString());
    }

    public void setPlayerText(String text) {
        this.playerNames.setText(text);
    }

    public void updateBoard(List<List<Card>> board) {
        boardPane.getChildren().clear();
        for (List<Card> row : board) {
            HBox rowBox = new HBox();
            rowBox.setSpacing(10);
            rowBox.setAlignment(Pos.BOTTOM_LEFT);

            for (Card card : row) {
                ImageView cardImagePlayed = new ImageView();
                cardImagePlayed.getStyleClass().add("card");
                String imagePath = ("/org/isep/sixquiprend/assets/img/cards/"+ card.getNumber() +".png");
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
                cardImagePlayed.setImage(image);
                cardImagePlayed.setFitHeight(80);
                cardImagePlayed.setFitWidth(55);

                rowBox.getChildren().add(cardImagePlayed);
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
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
            cardImage.setImage(image);
            cardImage.setFitHeight(120);
            handHBox.getChildren().add(cardImage);

            cardImage.setOnMouseClicked(event -> {
                for (Node child : handHBox.getChildren()) {
                    child.getStyleClass().remove("card_selected");
                }
                this.selectedCard = card;
                cardImage.getStyleClass().add("card_selected");
            });
        }

        this.playerNames.setStyle("-fx-font-weight: bold;");
        this.selectedPlayer.setText(playerName);

    }

    // TODO faire l'espace pour la selected card en fonction de this.selectedCard

    public Card getSelectedCard() {
        return this.selectedCard;
    }

    public void updateCards(List<Card> cardList){
        ObservableList<Card> observableList = FXCollections.observableArrayList(cardList);
        this.hand.setItems(observableList);
    }
}
