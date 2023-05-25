package org.isep.sixquiprend.controller;

import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Objects;

public class Client {
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private GameController gameController;

    public Client(GameController gameController) {
        this.gameController = gameController;
    }

    public void connectToServer() {
        try {
            socket = new Socket("flaminfo.fr", 4444);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            System.out.println("Connected to the server.");

            Thread instructionThread = new Thread(this::handleInstructions);
            instructionThread.start();

        } catch (IOException e) {
            System.out.println("Failed to connect to the server: " + e.getMessage());
        }
    }

    public void sendMessageToServer(Object message) {
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            System.out.println("Error while sending message to the server: " + e.getMessage());
        }
    }

    public Object waitForResponse() {
        try {
            return inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("_ERROR_ in communication: " + e.getMessage());
        }
        return null;
    }

    public void closeConnection() {
        try {
            if (outputStream != null)
                outputStream.close();
            if (inputStream != null)
                inputStream.close();
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            System.out.println("Error while closing connection: " + e.getMessage());
        }
    }

    private void handleInstructions() {
        try {
            while (true) {
                Object instruction = inputStream.readObject();
                if (instruction instanceof List<?>) {
                    List<?> listInstruction = (List<?>) instruction;

                    String title = (String) listInstruction.get(0);

                    if (Objects.equals(title, "_ALL_")){
                        listInstruction = (List<?>) listInstruction.get(1);
                        title = (String) listInstruction.get(0);
                    }

                    listInstruction.remove(0);

                    switch (title) {
                        case "_PLAYERLIST_" -> {
                            List<String> playerList = (List<String>) listInstruction;
                            gameController.updateOnlinePlayerList(playerList);
                        }
                        case "_PLAYERNAME_" -> gameController.setPlayerName((String) listInstruction.get(0));
                        case "_HOST_" -> gameController.setGameHost((String) listInstruction.get(0));
                        case "GAME_STARTED" -> {
                            Platform.runLater(() -> {
                                gameController.onlineChangeView("game");
                            });
                        }
                        case "_PLAYERCARD_" -> {
                            List<Integer> playerList = (List<Integer>) listInstruction.get(0);
                            gameController.onlineUpdatePlayerCard(playerList);
                        }
                        case "_BOARD_" -> {
                            List<List<Integer>> boardInfo = (List<List<Integer>>) listInstruction.get(0);
                            gameController.onlineUpdateBoard(boardInfo);
                        }
                        case "_ROUND_" -> gameController.onlineUpdateRound((int) listInstruction.get(0));
                        case "_PLAYERINFO_" -> {
                            List<List<?>> playerInfo = (List<List<?>>) listInstruction.get(0);
                            gameController.onlineHandlePlayerInfo(playerInfo);
                        }
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error in instruction handling: " + e.getMessage());
        }
    }
}
