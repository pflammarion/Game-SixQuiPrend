package org.isep.sixquiprend.controller;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.isep.sixquiprend.model.Card;
import org.isep.sixquiprend.model.Deck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Data
@NoArgsConstructor
public class CardController {
    private Deck deck;
    private int numCardsPerPlayer = 10;

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

    public List<Card> drawHand(){
        List<Card> cards = new ArrayList<>();
        for (int j = 0; j < numCardsPerPlayer; j++) {
            Card card = this.draw();
            cards.add(card);
        }

        cards.sort(Comparator.comparingInt(Card::getNumber));
        return cards;
    }

    public void shuffle(){
        Collections.shuffle(deck.getCards());
    }

    public void newDeck(){
        this.deck = new Deck(this.fillDeck());
    }

    public Card draw() {
        if (deck.getCards().isEmpty()) {
            return null;
        }
        return deck.getCards().remove(0);
    }

    public int AICardCalculation(List<List<Card>> board, List<Card> hand){
        int smallestDiff = Integer.MAX_VALUE;
        int lowestRowValue = Integer.MAX_VALUE;
        int selectedCardIndex = -1;
        List<Integer> tempStore = new ArrayList<>();

        for (List<Card> row : board) {
            int lastCardNumber = row.get(row.size() - 1).getNumber();
            if (lastCardNumber < lowestRowValue) {
                lowestRowValue = lastCardNumber;
            }
        }

        for (Card card : hand) {
            int cardNumber = card.getNumber();
            int diff = cardNumber - lowestRowValue;
            tempStore.add(diff);
        }

        for (int i = 0; i < tempStore.size(); i++) {
            int currentDiff = tempStore.get(i);
            if (currentDiff < smallestDiff && currentDiff > 0) {
                smallestDiff = currentDiff;
                selectedCardIndex = i;
            }
        }
        if (selectedCardIndex == -1) {
            selectedCardIndex =  extremeCaseNoPlayableRows(hand, selectedCardIndex);
        }
        return selectedCardIndex;
    }

    public int AICardCalculationHard(List<List<Card>> board, List<Card> hand) {
        int smallestDiff;
        List<Integer> lowestEachRow = new ArrayList<>();
        List<List<Integer>> diffLatestRowValue = new ArrayList<>();
        int selectedCardIndex = -1;
        List<Integer> latestRowValue = new ArrayList<>();
        int bestRow = -1;

        for (List<Card> row : board) {
            int lastCardNumber = row.get(row.size() - 1).getNumber();
            latestRowValue.add(lastCardNumber);
        }

        for (Integer integer : latestRowValue) {
            List<Integer> tempStore = new ArrayList<>();
            int rowValue = integer;
            for (Card card : hand) {
                int cardNumber = card.getNumber();
                int diff = cardNumber - rowValue;
                tempStore.add(diff);
            }
            diffLatestRowValue.add(tempStore);
        }

        for (List<Integer> rowDiff : diffLatestRowValue) {
            smallestDiff = Integer.MAX_VALUE;  // Reset smallestDiff for each row
            for (int currentDiff : rowDiff) {
                if (currentDiff < smallestDiff && currentDiff > 0) {
                    smallestDiff = currentDiff;
                }
            }
            lowestEachRow.add(smallestDiff);
        }

        smallestDiff = Integer.MAX_VALUE;  // Reset smallestDiff

        for (int i = 0; i < lowestEachRow.size(); i++) {
            int currentDiff = lowestEachRow.get(i);
            if (currentDiff < smallestDiff && currentDiff > 0) {
                smallestDiff = currentDiff;
                bestRow = i;
            }
        }

        if (bestRow != -1){
            List<Integer> chosenRowDiff = diffLatestRowValue.get(bestRow);
            selectedCardIndex = chosenRowDiff.indexOf(smallestDiff);
        }
        else {
            selectedCardIndex =  extremeCaseNoPlayableRows(hand, selectedCardIndex);
        }
        return selectedCardIndex;
    }

}
