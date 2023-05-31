package org.isep.sixquiprend.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


class DeckTest {
    private Deck deck;

    @BeforeEach
    public void setUp() {
        List<Card> cards = new ArrayList<>(Arrays.asList(
                new Card(1, 1),
                new Card(2, 1),
                new Card(3, 1),
                new Card(4, 1)
        ));
        deck = new Deck(cards);
    }

    @Test
    public void testGetCards() {
        List<Card> expectedCards = new ArrayList<>(Arrays.asList(
                new Card(1, 1),
                new Card(2, 1),
                new Card(3, 1),
                new Card(4, 1)
        ));
        Assertions.assertEquals(expectedCards, deck.getCards());
    }

    @Test
    public void testSetCards() {
        List<Card> newCards = new ArrayList<>(Arrays.asList(
                new Card(5, 1),
                new Card(6, 1),
                new Card(7, 1)
        ));

        deck.setCards(newCards);

        Assertions.assertEquals(newCards, deck.getCards());
    }

    @Test
    public void testShuffle() {
        deck.shuffle();

        Assertions.assertNotEquals(
                Arrays.asList(
                        new Card(1, 1),
                        new Card(2, 1),
                        new Card(3, 1),
                        new Card(4, 1)
                ),
                deck.getCards()
        );
    }

    @Test
    public void testDraw() {
        Card drawnCard = deck.draw();

        Card expectedCard = new Card(1, 1);
        Assertions.assertEquals(expectedCard, drawnCard);

        List<Card> remainingCards = new ArrayList<>(Arrays.asList(
                new Card(2, 1),
                new Card(3, 1),
                new Card(4, 1)
        ));
        Assertions.assertEquals(remainingCards, deck.getCards());
    }

    @Test
    public void testDrawEmptyDeck() {
        deck.getCards().clear();
        Card drawnCard = deck.draw();
        Assertions.assertNull(drawnCard);
    }

}