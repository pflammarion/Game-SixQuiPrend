package org.isep.sixquiprend.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientHandler implements Runnable, Serializable {
    private final Socket clientSocket;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final Server server;
    private String clientName;
    private boolean isHoster = false;

    public ClientHandler(Socket clientSocket, Server server) throws IOException {
        this.clientSocket = clientSocket;
        this.server = server;
        this.outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        this.inputStream = new ObjectInputStream(clientSocket.getInputStream());
        this.clientName = "Player " + server.getClientCount();
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object instruction = inputStream.readObject();

                System.out.println(instruction);

                Object response = processInstruction(instruction);
                if (null != response) {
                    outputStream.writeObject(response);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Client disconnected: " + clientSocket);
            server.removeClient(this);
        }
    }

    private Object processInstruction(Object instruction) throws IOException, ClassNotFoundException {
        Object response = null;
        if (instruction instanceof String) {
            String command = (String) instruction;
            switch (command) {
                case "GET_TIME" :
                    response = System.currentTimeMillis();
                    break;
                case "START_GAME" :
                    server.startGame();
                    break;
                case "GET_PLAYERLIST" :
                    response = server.getPlayerList();
                    break;
                case "GET_HOST" :
                    response = server.getHost();
                    break;
                case "SET_PLAYERNAME" :
                    String player = (String) inputStream.readObject();
                    if (!player.equals("")){
                        this.clientName = player;
                    }
                    server.broadcastMessage(server.getPlayerList());
                    server.broadcastMessage(server.getHost());
                    sendMessage("_PLAYERNAME_", this.clientName);
                    break;
            }
        }
        if (instruction instanceof Map<?, ?>) {
            Map<String, List<?>> command = (Map<String, List<?>>) instruction;

            if (command.containsKey("_GAMEINFO_")) {
                List<?> gameInfoList = command.get("_GAMEINFO_");

                if (gameInfoList != null && gameInfoList.size() == 1 && gameInfoList.get(0).equals("_GAMEINFO_")) {
                    System.out.println("gameinfo command");

                    // Process the game information here

                    List<List<Integer>> boardOnline = (List<List<Integer>>) command.get("_BOARD_");
                    List<Integer> roundList = (List<Integer>) command.get("_ROUND_");
                    List<List<?>> playerList = (List<List<?>>) command.get("_PLAYERS_");

                    // Do further processing with the received data

                    // Example: Print the board
                    System.out.println("Board:");
                    for (List<Integer> row : boardOnline) {
                        System.out.println(row);
                    }

                    // Example: Print the round
                    if (roundList != null && !roundList.isEmpty()) {
                        int round = roundList.get(0);
                        System.out.println("Round: " + round);
                    }

                    // Example: Print player information
                    System.out.println("Players:");
                    for (List<?> playerInfo : playerList) {
                        String playerName = (String) playerInfo.get(0);
                        List<Integer> playerHand = (List<Integer>) playerInfo.get(1);
                        int playerScore = (int) playerInfo.get(2);
                        int lastCardPlayed = (int) playerInfo.get(3);

                        System.out.println("Player: " + playerName);
                        System.out.println("Hand: " + playerHand);
                        System.out.println("Score: " + playerScore);
                        System.out.println("Last Card Played: " + lastCardPlayed);
                    }
                }
            }
        }


        return response;
    }

    public void sendMessage(String title, Object content) {
        List<Object> message = new ArrayList<>();
        message.add(title);
        message.add(content);
        try {
            outputStream.writeObject(message);
            System.out.println("Message sent : " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getClientName() {
        return clientName;
    }
}


