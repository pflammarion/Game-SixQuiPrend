package org.isep.sixquiprend.model;

import org.isep.sixquiprend.model.player.AIPlayer;
import org.isep.sixquiprend.model.player.Difficulty;
import org.isep.sixquiprend.model.player.HumanPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    HumanPlayer humanPlayer;
    AIPlayer aiPlayer;

    @BeforeEach
    void init(){
        List<Card> hand = new ArrayList<>();
        hand.add(new Card(11, 3));
        humanPlayer = new HumanPlayer("Paul");
        humanPlayer.setHand(hand);

        aiPlayer = new AIPlayer("AI Player", Difficulty.EASY);
        aiPlayer.setHand(hand);
    }

    @Test
    void getName() {
        assertEquals("Paul", humanPlayer.getName());
        assertNotEquals("Lilla", humanPlayer.getName());

        assertEquals("AI Player", aiPlayer.getName());
        assertNotEquals("Bob", aiPlayer.getName());
    }

    @Test
    void getHand() {
        List<Card> list = new ArrayList<>();
        list.add(new Card(12, 3));
        assertNotEquals(list, humanPlayer.getHand());
        assertNotEquals(list, aiPlayer.getHand());
    }

    @Test
    void getScore() {
        assertEquals(0, humanPlayer.getScore());
        assertNotEquals(1, humanPlayer.getScore());

        assertEquals(0, aiPlayer.getScore());
        assertNotEquals(1, aiPlayer.getScore());
    }

    @Test
    void setName() {
        humanPlayer.setName("Vincent");
        assertNotEquals("Paul", humanPlayer.getName());
        assertEquals("Vincent", humanPlayer.getName());

        aiPlayer.setName("AI Player 2");
        assertNotEquals("AI Player", aiPlayer.getName());
        assertEquals("AI Player 2", aiPlayer.getName());
    }

    @Test
    void setHand() {
        List<Card> hand = humanPlayer.getHand();
        hand.add(new Card(12, 3));
        humanPlayer.setHand(hand);
        assertEquals(hand, humanPlayer.getHand());

        hand = aiPlayer.getHand();
        hand.add(new Card(12, 3));
        aiPlayer.setHand(hand);
        assertEquals(hand, aiPlayer.getHand());
    }

    @Test
    void setScore() {
        humanPlayer.setScore(1);
        assertEquals(1, humanPlayer.getScore());
        assertNotEquals(0, humanPlayer.getScore());

        aiPlayer.setScore(1);
        assertEquals(1, aiPlayer.getScore());
        assertNotEquals(0, aiPlayer.getScore());
    }

    @Test
    void getLastCardPlayed() {
        assertNull(humanPlayer.getLastCardPlayed());

        Card card = new Card(10, 2);
        humanPlayer.setLastCardPlayed(card);
        assertEquals(card, humanPlayer.getLastCardPlayed());

        assertNull(aiPlayer.getLastCardPlayed());

        aiPlayer.setLastCardPlayed(card);
        assertEquals(card, aiPlayer.getLastCardPlayed());
    }

    @Test
    void setLastCardPlayed() {
        Card card = new Card(10, 2);
        humanPlayer.setLastCardPlayed(card);
        assertEquals(card, humanPlayer.getLastCardPlayed());

        card = new Card(8, 4);
        aiPlayer.setLastCardPlayed(card);
        assertEquals(card, aiPlayer.getLastCardPlayed());
    }

    @Test
    void setLastCardPlayed_Null() {
        humanPlayer.setLastCardPlayed(null);
        assertNull(humanPlayer.getLastCardPlayed());

        aiPlayer.setLastCardPlayed(null);
        assertNull(aiPlayer.getLastCardPlayed());
    }

    @Test
    void setDiff() {
        aiPlayer.setDiff(Difficulty.HARD);
        assertEquals(Difficulty.HARD, aiPlayer.getDiff());
        assertNotEquals(Difficulty.EASY, aiPlayer.getDiff());
    }

}
