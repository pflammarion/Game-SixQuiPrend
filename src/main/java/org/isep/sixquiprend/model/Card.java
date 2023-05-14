package org.isep.sixquiprend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Card {
    private int number;
    private int value;
    private String name;
    private boolean used;
}
