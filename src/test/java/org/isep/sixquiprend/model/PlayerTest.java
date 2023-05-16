package org.isep.sixquiprend.model;

import org.isep.sixquiprend.model.player.HumanPlayer;
import org.isep.sixquiprend.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    HumanPlayer player;
    @BeforeEach
    void init(){
        List<Card> hand = new ArrayList<>();
        hand.add(new Card(11, 3));
        player = new HumanPlayer("Paul");
    }

    @Test
    void playCard() {
        //do the test after the method
    }

    @Test
    void getName() {
        assertEquals("Paul", player.getName());
        assertNotEquals("Lilla", player.getName());
    }

    @Test
    void getHand() {
        List<Card> list = new ArrayList<>();
        list.add(new Card(12, 3));
        assertNotEquals(list, player.getHand());
    }

    @Test
    void getScore() {
        assertEquals(0, player.getScore());
        assertNotEquals(1, player.getScore());
    }

    @Test
    void setName() {
        player.setName("Vincent");
        assertNotEquals("Paul", player.getName());
        assertEquals("Vincent", player.getName());
    }

    @Test
    void setHand() {
        List<Card> hand = player.getHand();
        hand.add(new Card(12, 3));
        player.setHand(hand);
        assertEquals(player.getHand(), hand);
    }

    @Test
    void setScore() {
        player.setScore(1);
        assertEquals(1, player.getScore());
        assertNotEquals(0, player.getScore());
    }
}