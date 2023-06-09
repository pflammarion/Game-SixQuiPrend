package org.isep.sixquiprend.model;

import lombok.*;
import org.isep.sixquiprend.controller.CardController;
import org.isep.sixquiprend.model.player.Player;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    private List<Player> players = new ArrayList<>();
    private List<Card> cardsPlayed = new ArrayList<>();
    private ArrayList<List<Card>> board = new ArrayList<>();
    private int round = 1;
    private boolean gameEnded = false;
    private int currentPlayerIndex = 0;

}
