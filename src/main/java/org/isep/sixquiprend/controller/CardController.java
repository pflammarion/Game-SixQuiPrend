package org.isep.sixquiprend.controller;

import org.isep.sixquiprend.model.Card;

import java.util.ArrayList;
import java.util.List;

public class CardController {

    public List<Card> fillDeck() {
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
        return cards;
    }

    public int extremeCaseNoPlayableRows(List<Card> aiPlayerHand, int selectedCardIndex) {
        int minValue = Integer.MAX_VALUE;
        int currentIndex = 0;
        for (Card card : aiPlayerHand) {
            if (card.getNumber() < minValue) {
                minValue = card.getNumber();
                selectedCardIndex = currentIndex;
            }
            currentIndex++;
        }
        return selectedCardIndex;
    }

    public List<Card> findCardByNumberInList(List<Integer> cardListToFind) {
        List<Card> cardList = new ArrayList<>();
        List<Card> deckCards = this.fillDeck();
        for (int cardNumber : cardListToFind) {
            for (Card card : deckCards){
                if (card.getNumber() == cardNumber) {
                    cardList.add(card);
                    break;
                }
            }
        }
        return cardList;
    }

    public Card findCardByNumber(int number){
        List<Card> deckCards = this.fillDeck();
        for (Card card : deckCards){
            if (card.getNumber() == number) {
                return card;
            }
        }
        return null;
    }
}
