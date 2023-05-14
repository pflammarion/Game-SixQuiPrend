package org.isep.sixquiprend.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import org.isep.sixquiprend.model.Card;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void getBoard() {
        List<Card> row1 = new ArrayList<>();
        row1.add(new Card(1, 1, false));
        row1.add(new Card(2, 1, true));

        List<Card> row2 = new ArrayList<>();
        row2.add(new Card(3, 1, false));
        row2.add(new Card(4, 1, true));

        List<Card> row3 = new ArrayList<>();
        row3.add(new Card(5, 5, false));
        row3.add(new Card(6, 1, true));

        List<Card> row4 = new ArrayList<>();
        row4.add(new Card(7, 1, false));
        row4.add(new Card(8, 1, true));

        ArrayList<List<Card>> board = new ArrayList<>();
        board.add(row1);
        board.add(row2);
        board.add(row3);
        board.add(row4);
        Game game = new Game(board, new ArrayList<>());
        Assertions.assertEquals(board, game.getBoard());
    }

    @Test
    void getPlayedCard() {
        List<Card> playedCard = new ArrayList<>();
        playedCard.add(new Card(2, 1, true));
        playedCard.add(new Card(6, 1, true));
        Game game = new Game(new ArrayList<>(), playedCard);
        Assertions.assertEquals(playedCard, game.getPlayedCard());
        for (int i = 0; i > playedCard.size(); i++)
        {
            Assertions.assertTrue(game.getPlayedCard().get(i).isUsed());
        }
    }

    @Test
    void setBoard() {
        List<Card> row1 = new ArrayList<>();
        row1.add(new Card(1, 1, false));
        row1.add(new Card(2, 1, true));

        List<Card> row2 = new ArrayList<>();
        row2.add(new Card(3, 1, false));
        row2.add(new Card(4, 1, true));

        List<Card> row3 = new ArrayList<>();
        row3.add(new Card(5, 5, false));
        row3.add(new Card(6, 1, true));

        List<Card> row4 = new ArrayList<>();
        row4.add(new Card(7, 1, false));
        row4.add(new Card(8, 1, true));

        ArrayList<List<Card>> board = new ArrayList<>();
        board.add(row1);
        board.add(row2);
        board.add(row3);
        board.add(row4);

        Game game = new Game(new ArrayList<>(), new ArrayList<>());
        game.setBoard(board);
        Assertions.assertEquals(board, game.getBoard());
    }

    @Test
    void setPlayedCard() {
        List<Card> playedCard = new ArrayList<>();
        playedCard.add(new Card(2, 1, true));
        playedCard.add(new Card(6, 1, true));
        Game game = new Game(new ArrayList<>(), new ArrayList<>());
        game.setPlayedCard(playedCard);
        Assertions.assertEquals(playedCard, game.getPlayedCard());
        for (int i = 0; i > playedCard.size(); i++)
        {
            Assertions.assertTrue(game.getPlayedCard().get(i).isUsed());
        }
    }
}