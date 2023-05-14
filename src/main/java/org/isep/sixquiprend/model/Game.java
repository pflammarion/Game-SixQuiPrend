package org.isep.sixquiprend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
public class Game {
    private ArrayList<List<Card>> board;
    private List<Card> playedCard;
}
