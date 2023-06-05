package org.isep.sixquiprend.controller;
import org.isep.sixquiprend.model.player.HumanPlayer;
import org.isep.sixquiprend.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerControllerTest {
    private PlayerController playerController;
    private List<Player> playerList;

    @BeforeEach
    void setUp() {
        playerController = new PlayerController();
        playerList = new ArrayList<>();
        playerList.add(new HumanPlayer("Paul"));
        playerList.add(new HumanPlayer("Lilla"));
        playerList.add(new HumanPlayer("Vincent"));
    }

    @Test
    void testIsNameUsed() {
        assertTrue(playerController.isNameUsed("Paul", playerList));
        assertFalse(playerController.isNameUsed("Dave", playerList));
    }

    @Test
    void testCreatePlayerListFromString() {
        List<String> names = new ArrayList<>(List.of("Paul", "Lilla", "Vincent"));
        List<Player> players = playerController.createPlayerListFromString(names);
        assertEquals(names.size(), players.size());
        for (int i = 0; i < names.size(); i++) {
            assertEquals(names.get(i), players.get(i).getName());
        }
    }

    @Test
    void testFindPlayerByName() {
        Player player = playerController.findPlayerByName("Lilla", playerList);
        assertNotNull(player);
        assertEquals("Lilla", player.getName());

        Player nonExistentPlayer = playerController.findPlayerByName("Dave", playerList);
        assertNull(nonExistentPlayer);
    }
}

