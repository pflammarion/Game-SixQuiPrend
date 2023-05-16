package org.isep.sixquiprend.model.player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HumanPlayer extends Player {
    private String name;

    public HumanPlayer(String name) {
        this.name = name;
    }
}
