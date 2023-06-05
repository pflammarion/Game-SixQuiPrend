package org.isep.sixquiprend.controller;
import org.isep.sixquiprend.model.Card;
import org.isep.sixquiprend.model.Deck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CardControllerTest {
    private CardController cardController;
    private Deck deck;
    @BeforeEach
    void setUp() {
        cardController = new CardController();
        cardController.setDeck(new Deck(cardController.fillDeck()));
        deck = new Deck();
    }

    @Test
    void testFillDeck() {
        List<Card> cards = cardController.fillDeck();

        assertEquals(104, cards.size());

        Card card = cards.get(0);
        assertEquals(1, card.getNumber());
        assertEquals(1, card.getBullHeads());
    }

    @Test
    void testExtremeCaseNoPlayableRows() {
        List<Card> aiPlayerHand = new ArrayList<>(List.of(
                new Card(1, 1),
                new Card(2, 2),
                new Card(3, 3)
        ));

        int selectedCardIndex = cardController.extremeCaseNoPlayableRows(aiPlayerHand, -1);
        assertEquals(0, selectedCardIndex);
    }

    @Test
    void testFindCardByNumberInList() {
        List<Integer> cardListToFind = List.of(1, 3, 5);
        List<Card> cardList = cardController.findCardByNumberInList(cardListToFind);

        assertEquals(3, cardList.size());

        Card card = cardList.get(0);
        assertEquals(1, card.getNumber());
        assertEquals(1, card.getBullHeads());
    }

    @Test
    void testFindCardByNumber() {
        Card card = cardController.findCardByNumber(1);

        assertNotNull(card);
        assertEquals(1, card.getNumber());
        assertEquals(1, card.getBullHeads());
    }

    @Test
    void testSortCardList() {
        List<Card> cardList = new ArrayList<>(List.of(
                new Card(3, 3),
                new Card(1, 1),
                new Card(2, 2)
        ));

        List<Card> sortedList = cardController.sortCardList(cardList);

        assertEquals(3, sortedList.size());
        assertEquals(1, sortedList.get(0).getNumber());
        assertEquals(2, sortedList.get(1).getNumber());
        assertEquals(3, sortedList.get(2).getNumber());
    }

    @Test
    void testDrawHand() {
        List<Card> hand = cardController.drawHand();

        assertEquals(10, hand.size());
    }

    @Test
    void testShuffle() {
        Deck deck = cardController.getDeck();

        cardController.shuffle();

        assertNotEquals(new Card(1, 1), deck.getCards().get(0));
        assertNotEquals(new Card(2, 1), deck.getCards().get(1));
        assertNotEquals(new Card(3, 1), deck.getCards().get(2));
    }

    @Test
    void testNewDeck() {
        cardController.newDeck();
        assertNotSame(deck, cardController.getDeck());
    }

    @Test
    void testDrawHand_Sorted() {
        cardController.setNumCardsPerPlayer(10);

        List<Card> hand = cardController.drawHand();

        assertEquals(10, hand.size());

        for (int i = 1; i < hand.size(); i++) {
            assertTrue(hand.get(i).getNumber() > hand.get(i - 1).getNumber());
        }
    }

    @Test
    void testDraw() {
        List<Card> cards = cardController.fillDeck();
        cardController.setDeck(new Deck(cards));

        Card card = cardController.draw();

        assertNotNull(card);
        assertEquals(103, cardController.getDeck().getCards().size());
    }

    @Test
    void testDraw_EmptyDeck() {
        cardController.setDeck(new Deck(List.of()));

        Card card = cardController.draw();

        assertNull(card);
    }

    @Test
    void testAICardCalculation() {
        List<List<Card>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of(new Card(1, 1), new Card(3, 1))),
                new ArrayList<>(List.of(new Card(2, 1)))
        ));
        List<Card> hand = new ArrayList<>(List.of(new Card(4, 1), new Card(5, 2)));

        int selectedCardIndex = cardController.AICardCalculation(board, hand);
        assertEquals(0, selectedCardIndex);
    }

    @Test
    void testAICardCalculationHard() {
        List<List<Card>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of(new Card(1, 1), new Card(3, 1))),
                new ArrayList<>(List.of(new Card(2, 1)))
        ));
        List<Card> hand = new ArrayList<>(List.of(new Card(4, 1), new Card(5, 2)));

        int selectedCardIndex = cardController.AICardCalculationHard(board, hand);
        assertEquals(0, selectedCardIndex);
    }

    @Test
    void testFindSelectedRowIndex() {
        Card card = new Card(5, 2);
        List<List<Card>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of(new Card(1, 1), new Card(3, 1))),
                new ArrayList<>(List.of(new Card(2, 1))),
                new ArrayList<>(List.of(new Card(4, 1)))
        ));

        int selectedRowIndex = cardController.findSelectedRowIndex(card, board);
        assertEquals(2, selectedRowIndex);
    }

    @Test
    void testFindSelectedRowIndexForNonPlayableCard() {
        List<List<Card>> board = new ArrayList<>(List.of(
                new ArrayList<>(List.of(new Card(1, 1), new Card(3, 3))),
                new ArrayList<>(List.of(new Card(2, 2))),
                new ArrayList<>(List.of(new Card(4, 4)))
        ));

        int selectedRowIndex = cardController.findSelectedRowIndexForNonPlayableCard(board);
        assertEquals(1, selectedRowIndex);
    }

    @Test
    void testCalculateScore() {
        List<Card> row = new ArrayList<>(List.of(
                new Card(1, 1),
                new Card(2, 2),
                new Card(3, 3)
        ));

        int score = cardController.calculateScore(row);
        assertEquals(6, score);
    }

}