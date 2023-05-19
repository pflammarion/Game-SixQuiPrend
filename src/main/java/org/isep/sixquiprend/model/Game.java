package org.isep.sixquiprend.model;

import lombok.*;
import org.isep.sixquiprend.model.player.Player;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    private List<Player> players = new ArrayList<>();
    private Deck deck;
    private List<Card> cardsPlayed = new ArrayList<>();
    private ArrayList<List<Card>> board = new ArrayList<>();
    private int round = 1;
    private int totalBullHeads = 0;
    private boolean gameEnded = false;
    private int currentPlayerIndex = 0;

    public void boardSetUp(Deck deck) {
        for (int i = 0; i < 4; i++) {
            this.board.add(new ArrayList<>());
            Card card = deck.draw();
            this.board.get(i).add(card);
        }
    }
}
