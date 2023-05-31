package org.isep.sixquiprend.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class CardTest {
    @Test
    void getNumber() {
        Card card = new Card(1, 1);
        Assertions.assertEquals(1, card.getNumber());
        Assertions.assertNotEquals(10, card.getNumber());
    }

    @Test
    void getValue() {
        Card card = new Card(2, 1);
        Assertions.assertEquals(1, card.getBullHeads());
        Assertions.assertNotEquals(10, card.getBullHeads());
    }


    @Test
    void setNumber() {
        Card card = new Card(7, 1);
        Assertions.assertEquals(7, card.getNumber());
        Assertions.assertNotEquals(10, card.getNumber());
        card.setNumber(10);
        Assertions.assertEquals(10, card.getNumber());
        Assertions.assertNotEquals(7, card.getNumber());
    }

    @Test
    void setValue() {
        Card card = new Card(8, 2);
        Assertions.assertEquals(2, card.getBullHeads());
        Assertions.assertNotEquals(11, card.getBullHeads());
        card.setBullHeads(11);
        Assertions.assertEquals(11, card.getBullHeads());
        Assertions.assertNotEquals(2, card.getBullHeads());
    }
}