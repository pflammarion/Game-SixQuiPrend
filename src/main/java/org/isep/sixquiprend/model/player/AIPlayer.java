package org.isep.sixquiprend.model.player;

import lombok.Getter;
import lombok.Setter;
import org.isep.sixquiprend.model.Card;

import java.util.List;
@Getter
@Setter
public class AIPlayer extends Player {
    private Difficulty diff;
    public AIPlayer(String name, Difficulty diff) {
        super(name);
        this.diff = diff;
    }
}
