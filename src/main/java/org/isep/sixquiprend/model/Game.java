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

    public void boardSetUp() {
        for (int i = 0; i < 4; i++) {
            this.board.add(new ArrayList<>());
        }
    }


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
        boardSetUp();
        round = 1;
        totalBullHeads = 0;
        gameEnded = false;
    }
    public int updateBoard(List<Object> played) {
        int score = 0;
        List<Card> selectedRow = this.board.get((Integer) played.get(1));

        if (selectedRow.size() < 6) {
            selectedRow.add((Card) played.get(0));
        } else {
            for (Card card : selectedRow) {
                score += card.getBullHeads();
            }
            selectedRow.clear();
        }
        board.set((Integer) played.get(1), selectedRow);

        return score;
    }
}
