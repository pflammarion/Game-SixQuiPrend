package org.isep.sixquiprend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class Player {
    private String name;
    private List<Card> hand;
    private int score;

    protected void playCard(){
        // Play a card based on a player
    }
}
