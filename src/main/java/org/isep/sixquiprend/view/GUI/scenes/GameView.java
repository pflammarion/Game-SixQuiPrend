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

import java.nio.file.LinkOption;
import java.util.*;

import javafx.scene.text.Text;


public class GameView {
    private final Button playButton;
    private final Scene scene;
    private final HBox playerNamesContainer;
    private final Label roundLabel;
    private final VBox boardPane;
    private Label selectedPlayer;
    private HBox handHBox;
    private Card selectedCard;
    private VBox cardPlayed;
    private VBox playedCards;


    public GameView() {
        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/isep/sixquiprend/assets/img/background_accueil.jpg"))));
        playButton = new Button("Jouer cette carte");
        roundLabel = new Label();
        boardPane = new VBox();
        selectedPlayer = new Label();

        selectedPlayer.getStyleClass().add("selected_player");

        playerNamesContainer = new HBox();
        playerNamesContainer.setSpacing(10);
        playerNamesContainer.getStyleClass().add("player_list");

        boardPane.setAlignment(Pos.CENTER_LEFT);
        boardPane.setMinWidth(750.0);
        boardPane.setSpacing(10);

        playedCards = new VBox();
        playedCards.setAlignment(Pos.CENTER_LEFT);
        playedCards.setSpacing(10);

        HBox middlePane = new HBox(boardPane, playedCards);
        middlePane.setSpacing(100);

        HBox gameInfosHBox = new HBox(playerNamesContainer, selectedPlayer);
        gameInfosHBox.setMaxWidth(1500);
        gameInfosHBox.setSpacing(1000);

        this.handHBox = new HBox();
        handHBox.setAlignment(Pos.BOTTOM_CENTER);
        handHBox.setSpacing(10);
        handHBox.setMinWidth(880.0);
        handHBox.setMaxWidth(880.0);

        cardPlayed = new VBox();
        cardPlayed.setAlignment(Pos.CENTER);
        cardPlayed.setMinHeight(170);
        cardPlayed.setMinWidth(200);

        HBox playerArea = new HBox(handHBox, cardPlayed);
        playerArea.setSpacing(50);
        playerArea.setAlignment(Pos.BOTTOM_CENTER);

        VBox mainvbox = new VBox(roundLabel, gameInfosHBox, middlePane, playerArea, playButton);
        mainvbox.setMinWidth(1400);
        mainvbox.setMinHeight(800);
        mainvbox.setSpacing(20);
        mainvbox.setAlignment(Pos.CENTER);

        AnchorPane anchorPane = new AnchorPane(imageView, mainvbox);
        anchorPane.setPrefSize(1400, 800);
        AnchorPane.setTopAnchor(mainvbox, 50.0);
        AnchorPane.setLeftAnchor(mainvbox, 150.0);

        this.scene = new Scene(anchorPane);
        imageView.fitWidthProperty().bind(scene.widthProperty());
        imageView.fitHeightProperty().bind(scene.heightProperty());
    }

    public Scene getScene() {
        return scene;
    }

    public Button getPlayButton() {
        return playButton;
    }

    public void updatePlayers(List<Player> players) {
        playerNamesContainer.getChildren().clear();
        StringBuilder playerNamesText = new StringBuilder();
        int playerCount= 0;

        for (Player player : players) {
            playerNamesText.append(player.getName()).append(" | score : ").append(player.getScore()).append("\n");

            playerCount++;
            if (playerCount % 4 == 0) {
                Text playerName = new Text(playerNamesText.toString());
                this.playerNamesContainer.getChildren().add(playerName);
                playerNamesText = new StringBuilder();
            }
        }
        Text playerName = new Text(playerNamesText.toString());
        this.playerNamesContainer.getChildren().add(playerName);
    }

    public void setPlayerText(String text) {
        this.playerNamesContainer.getChildren().clear();
        Text playerName = new Text(text);
        this.playerNamesContainer.getChildren().add(playerName);
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
        roundLabel.setText("Tour : " + round);
    }

    public void setPlayerTurn(Player currentPlayer) {

        String playerName = currentPlayer.getName();
        List<Card> playerHand = currentPlayer.getHand();
        updateCards(playerHand);

        this.playerNamesContainer.setStyle("-fx-font-weight: bold;");
        this.selectedPlayer.setText(playerName);

    }

    public Card getSelectedCard() {
        Card card = this.selectedCard;
        this.selectedCard = null;
        cardPlayed.getChildren().clear();
        return card;
    }

    public void updateCards(List<Card> cardList){
        handHBox.getChildren().clear();
        for (Card card : cardList) {
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
                this.updateUIChoosenCard(card);
                cardImage.getStyleClass().add("card_selected");
            });
        }
    }

    private void updateUIChoosenCard(Card card) {

        cardPlayed.getChildren().clear();

        Label selectedCardLabel = new Label("Carte jouée : \n");

        ImageView cardImage = new ImageView();
        cardImage.getStyleClass().add("card");
        String imagePath = ("/org/isep/sixquiprend/assets/img/cards/"+ card.getNumber() +".png");
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        cardImage.setImage(image);
        cardImage.setFitHeight(120);
        this.cardPlayed.getChildren().add(selectedCardLabel);
        this.cardPlayed.getChildren().add(cardImage);
    }

    public void updatePreviousRound(List<Player> players) {
        playedCards.getChildren().clear();
        HBox playerContainer  = new HBox();


        Label playedCardsLabel = new Label("Cartes jouées : \n");

        List<Player> sortedPlayers = new ArrayList<>(players);
        sortedPlayers.sort(Comparator.comparingInt(player -> player.getLastCardPlayed().getNumber()));

        this.playedCards.getChildren().add(playedCardsLabel);
        int count = 0;

        for (Player player : sortedPlayers){

            ImageView cardImage = new ImageView();
            cardImage.getStyleClass().add("card");
            String imagePath = ("/org/isep/sixquiprend/assets/img/cards/"+ player.getLastCardPlayed().getNumber() +".png");
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
            cardImage.setImage(image);
            cardImage.setFitWidth(40);
            cardImage.setFitHeight(60);
            Text playerName = new Text(player.getName());
            VBox cardPlayer = new VBox(playerName, cardImage);
            cardPlayer.setAlignment(Pos.CENTER);
            cardPlayer.setSpacing(5);
            cardPlayer.setMinWidth(100);
            playerContainer.getChildren().add(cardPlayer);
            count++;
            if (count % 4 == 0){
                this.playedCards.getChildren().add(playerContainer);
                playerContainer  = new HBox();
                count = 0;
            }
        }
        this.playedCards.getChildren().add(playerContainer);

    }
}
