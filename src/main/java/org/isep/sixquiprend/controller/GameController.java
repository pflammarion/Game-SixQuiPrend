package org.isep.sixquiprend.controller;

import javafx.application.Platform;
import org.isep.sixquiprend.model.Card;
import org.isep.sixquiprend.model.Deck;
import org.isep.sixquiprend.model.Game;
import org.isep.sixquiprend.model.player.HumanPlayer;
import org.isep.sixquiprend.model.player.Player;
import org.isep.sixquiprend.view.GUI.SceneManager;
import org.isep.sixquiprend.view.GUI.scenes.WelcomeView;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    private SceneManager sceneManager;
    private WelcomeView welcomeView;
    private Game game;
    private Deck deck;
    public GameController(SceneManager sceneManager){
        this.sceneManager = sceneManager;
        this.welcomeView = new WelcomeView();
        this.game = new Game();
        eventListener();
        fillDeck();
        dealCards();
    }
    private void eventListener(){
        // to add a scene on construct do :
        // sceneManager.addScene("sceneName", myScene.getScene());
        sceneManager.addScene("welcome", welcomeView.getScene());

        welcomeView.getButtonQuit().setOnAction(event -> {
            Platform.exit();
            System.exit(0);
        });

    }

    private void fillDeck() {
        List<Card> cards = new ArrayList<>();
        for (int i = 1; i <= 104; i++) {
            int bullHeads;
            if (i % 11 == 0) {
                bullHeads = 5;
            } else if (i % 10 == 0) {
                bullHeads = 3;
            } else if (i % 5 == 0) {
                bullHeads = 2;
            } else {
                bullHeads = 1;
            }
            Card card = new Card(i, bullHeads);
            cards.add(card);
        }
        this.deck = new Deck(cards);
    }

    public void dealCards() {
        List<Player> players = this.game.getPlayers();
        int numCardsPerPlayer = 10;
        for (Player player : players) {
            List<Card> cards = new ArrayList<>();
            for (int j = 0; j < numCardsPerPlayer; j++) {
                Card card = deck.draw();
                cards.add(card);
            }
            player.setHand(cards);
        }
    }
}
