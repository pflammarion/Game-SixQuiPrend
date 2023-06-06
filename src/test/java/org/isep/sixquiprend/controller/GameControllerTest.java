package org.isep.sixquiprend.controller;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.isep.sixquiprend.controller.GameController;
import org.isep.sixquiprend.model.Card;
import org.isep.sixquiprend.model.Game;
import org.isep.sixquiprend.model.player.HumanPlayer;
import org.isep.sixquiprend.model.player.Player;
import org.isep.sixquiprend.view.GUI.SceneManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {
    private GameController gameController;
    private SceneManager sceneManager;

    @BeforeAll
    public static void setup(Stage stage) {
        SceneManager sceneManager = new SceneManager(stage);
        GameController gameController = new GameController(sceneManager);
    }

    @Test
    public void testStartGameWithEnoughPlayers() {
        Game game = gameController.getGame();

        List<Player> players = new ArrayList<>();
        players.add(new HumanPlayer("Player 1"));
        players.add(new HumanPlayer("Player 2"));
        game.setPlayers(players);

        gameController.startGame();

        assertNotNull(game.getBoard());
        assertEquals(1, game.getRound());
        assertFalse(game.isGameEnded());
    }

    @Test
    public void testStartGameWithInsufficientPlayers() {
        Game game = gameController.getGame();

        List<Player> players = new ArrayList<>();
        players.add(new HumanPlayer("Player 1"));
        game.setPlayers(players);

        gameController.startGame();

        assertNull(game.getBoard());
        assertEquals(1, game.getRound());
        assertFalse(game.isGameEnded());
    }

    @Test
    public void testPlayCard() {
        Game game = gameController.getGame();

        List<Player> players = new ArrayList<>();
        HumanPlayer currentPlayer = new HumanPlayer("Player 1");
        players.add(currentPlayer);
        players.add(new HumanPlayer("Player 2"));
        game.setPlayers(players);

        Card card = new Card(5, 2);

        gameController.playCard();

        // Assert that the card is played and removed from the player's hand
        assertFalse(currentPlayer.getHand().contains(card));
        assertEquals(card, currentPlayer.getLastCardPlayed());
    }
}