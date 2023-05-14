package org.isep.sixquiprend.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {
    @Test
    void getNumber() {
        Card card = new Card(1, 1, false);
        Assertions.assertEquals(1, card.getNumber());
        Assertions.assertNotEquals(10, card.getNumber());
    }

    @Test
    void getValue() {
        Card card = new Card(2, 1, true);
        Assertions.assertEquals(1, card.getValue());
        Assertions.assertNotEquals(10, card.getValue());
    }

    @Test
    void isUsed() {
        Card card = new Card(6, 1, true);
        Assertions.assertTrue(card.isUsed());
    }

    @Test
    void setNumber() {
        Card card = new Card(7, 1, false);
        Assertions.assertEquals(7, card.getNumber());
        Assertions.assertNotEquals(10, card.getNumber());
        card.setNumber(10);
        Assertions.assertEquals(10, card.getNumber());
        Assertions.assertNotEquals(7, card.getNumber());
    }

    @Test
    void setValue() {
        Card card = new Card(8, 2, true);
        Assertions.assertEquals(2, card.getValue());
        Assertions.assertNotEquals(11, card.getValue());
        card.setValue(11);
        Assertions.assertEquals(11, card.getValue());
        Assertions.assertNotEquals(2, card.getValue());
    }

    @Test
    void setUsed() {
        Card card = new Card(6, 12, true);
        Assertions.assertTrue(card.isUsed());
        card.setUsed(false);
        Assertions.assertFalse(card.isUsed());
    }
}