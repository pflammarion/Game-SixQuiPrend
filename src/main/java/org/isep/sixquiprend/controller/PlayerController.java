package org.isep.sixquiprend.controller;

import org.isep.sixquiprend.model.player.HumanPlayer;
import org.isep.sixquiprend.model.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlayerController {
    public boolean isNameUsed(String name, List<Player> playerList) {
        for (Player player : playerList){
            if (Objects.equals(player.getName(), name)){
                return true;
            }
        }
        return false;
    }
    public List<Player> createPlayerListFromString(List<String> list){
        List<Player> players = new ArrayList<>();
        for (String s : list) {
            players.add(new HumanPlayer(s));
        }
        return players;
    }

    public Player findPlayerByName(String name, List<Player> playerList){
        for (Player player : playerList){
            if (player.getName().equals(name)){
                return player;
            }
        }
        return null;
    }
}
