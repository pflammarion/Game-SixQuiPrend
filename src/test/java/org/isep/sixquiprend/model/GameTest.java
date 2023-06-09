package org.isep.sixquiprend.model;

import org.isep.sixquiprend.model.player.HumanPlayer;
import org.isep.sixquiprend.model.player.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameTest {
    private Game game;

    @BeforeEach
    public void setUp() {
        game = new Game();
    }

    @Test
    public void testGetPlayers() {
        List<Player> players = new ArrayList<>(Arrays.asList(new HumanPlayer("Paul"),  new HumanPlayer("Lilla")));
        game.setPlayers(players);
        Assertions.assertEquals(players, game.getPlayers());
    }

    @Test
    public void testSetPlayers() {
        List<Player> players = new ArrayList<>(Arrays.asList(new HumanPlayer("Paul"),  new HumanPlayer("Lilla")));
        game.setPlayers(players);
        Assertions.assertEquals(players, game.getPlayers());
    }

    @Test
    public void testGetCardsPlayed() {
        List<Card> playedCards = new ArrayList<>(Arrays.asList(new Card(2, 1), new Card(6, 1)));
        game.setCardsPlayed(playedCards);
        Assertions.assertEquals(playedCards, game.getCardsPlayed());
    }

    @Test
    public void testSetCardsPlayed() {
        List<Card> playedCards = new ArrayList<>(Arrays.asList(new Card(2, 1), new Card(6, 1)));
        game.setCardsPlayed(playedCards);
        Assertions.assertEquals(playedCards, game.getCardsPlayed());
    }

    @Test
    public void testGetBoard() {
        List<Card> row1 = new ArrayList<>(Arrays.asList(new Card(1, 1), new Card(2, 1)));
        List<Card> row2 = new ArrayList<>(Arrays.asList(new Card(3, 1), new Card(4, 1)));
        List<Card> row3 = new ArrayList<>(Arrays.asList(new Card(5, 5), new Card(6, 1)));
        List<Card> row4 = new ArrayList<>(Arrays.asList(new Card(7, 1), new Card(8, 1)));
        ArrayList<List<Card>> board = new ArrayList<>(Arrays.asList(row1, row2, row3, row4));
        game.setBoard(board);
        Assertions.assertEquals(board, game.getBoard());
    }

    @Test
    public void testSetBoard() {
        List<Card> row1 = new ArrayList<>(Arrays.asList(new Card(1, 1), new Card(2, 1)));
        List<Card> row2 = new ArrayList<>(Arrays.asList(new Card(3, 1), new Card(4, 1)));
        List<Card> row3 = new ArrayList<>(Arrays.asList(new Card(5, 5), new Card(6, 1)));
        List<Card> row4 = new ArrayList<>(Arrays.asList(new Card(7, 1), new Card(8, 1)));

        ArrayList<List<Card>> board = new ArrayList<>(Arrays.asList(row1, row2, row3, row4));
        game.setBoard(board);
        Assertions.assertEquals(board, game.getBoard());
    }

    @Test
    public void testGetRound() {
        int round = 5;
        game.setRound(round);
        Assertions.assertEquals(round, game.getRound());
    }

    @Test
    public void testSetRound() {
        int round = 5;
        game.setRound(round);

        Assertions.assertEquals(round, game.getRound());
    }

    @Test
    public void testIsGameEnded() {
        game.setGameEnded(true);
        Assertions.assertTrue(game.isGameEnded());
    }

    @Test
    public void testSetGameEnded() {
        game.setGameEnded(true);
        Assertions.assertTrue(game.isGameEnded());
    }

    @Test
    public void testGetCurrentPlayerIndex() {
        int currentPlayerIndex = 2;
        game.setCurrentPlayerIndex(currentPlayerIndex);

        Assertions.assertEquals(currentPlayerIndex, game.getCurrentPlayerIndex());
    }

    @Test
    public void testSetCurrentPlayerIndex() {
        int currentPlayerIndex = 2;
        game.setCurrentPlayerIndex(currentPlayerIndex);

        Assertions.assertEquals(currentPlayerIndex, game.getCurrentPlayerIndex());
    }

}