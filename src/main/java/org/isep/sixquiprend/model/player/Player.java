package org.isep.sixquiprend.model.player;

import lombok.*;
import org.isep.sixquiprend.model.Card;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class Player {
    private List<Card> hand = new ArrayList<>();
    private int score = 0;
}
