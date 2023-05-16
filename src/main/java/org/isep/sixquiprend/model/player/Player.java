package org.isep.sixquiprend.model.player;

import lombok.*;
import org.isep.sixquiprend.model.Card;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class Player {
    private List<Card> hand = new ArrayList<>();
    private int score = 0;
    private Card cardPlayed;
    // cardPlayed utilisé pour référer le joueur et la carte jouée de la main pour le board.
    // Exemple 1: si 6eme carte jouée sur une row, il faut savoir qui a joué la 6eme carte, pour pouvoir
    // donner toutes les cartes du row.

    // Exemple 2: si la carte jouée par un joueur ne peut pas être poser sur une des rows, le joueur doit récupérer
    // toutes les cartes d'une row pour pouvoir placer sa carte
}
