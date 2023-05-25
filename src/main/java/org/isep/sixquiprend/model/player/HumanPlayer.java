package org.isep.sixquiprend.model.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.isep.sixquiprend.model.Card;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class HumanPlayer extends Player implements Serializable {
    public HumanPlayer(String name) {
        super(name);
    }
}
