package org.isep.sixquiprend.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientHandler implements Runnable {
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
        if (instruction instanceof List<?>) {
            List<Object> command = (List<Object>) instruction;
            if (!command.isEmpty()) {
                if (command.get(0).equals("_GAMEINFO_")) {
                    processGameInfo(command);
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

    private void processGameInfo(List<Object> gameInfo) {

        int index = 1;

        if (gameInfo.get(index).equals("_BOARD_")) {
            index++;

            List<List<Integer>> boardOnline = new ArrayList<>();
            while (index < gameInfo.size() && !(gameInfo.get(index) instanceof String)) {
                List<Integer> row = (List<Integer>) gameInfo.get(index);
                boardOnline.add(row);
                index++;
            }

            // Do something with the board data (boardOnline)
            System.out.println(boardOnline);

        }

        if (gameInfo.get(index).equals("_ROUND_")) {
            index++;

            int round = (int) gameInfo.get(index);
            index++;

            // Do something with the round info (round)
        }

        if (gameInfo.get(index).equals("_PLAYERS_")) {
            index++;

            List<List<?>> playerList = new ArrayList<>();
            while (index < gameInfo.size()) {
                List<Object> player = new ArrayList<>();
                player.add(gameInfo.get(index));
                playerList.add(player);
                index++;
            }
            System.out.println(playerList);

            // Do something with the player info (playerList)
        }
    }
}


