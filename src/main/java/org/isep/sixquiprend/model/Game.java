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

    public void moveToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }
    public Player getCurrentPlayer(){
        return players.get(currentPlayerIndex);
    }
    public void incrementRound() {
        this.round += 1;
    }
    public void reset() {
        players.clear();
        cardsPlayed.clear();
        board.clear();
        round = 1;
        totalBullHeads = 0;
        gameEnded = false;
    }
}
