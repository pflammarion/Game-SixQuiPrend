package org.isep.sixquiprend.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
        String command = (String) instruction;
        switch (command) {
            case "GET_TIME" :
                response = System.currentTimeMillis();
                break;
            case "GAME_START" :
                server.startGame();
                break;
            case "GET_PLAYERLIST" :
                response = server.getPlayerList();
                break;
            case "SET_PLAYERNAME" :
                String player = (String) inputStream.readObject();
                if (!player.equals("")){
                    this.clientName = player;
                }
                server.broadcastMessage(server.getPlayerList());
                sendMessage("_PLAYERNAME_", this.clientName);
                break;
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


