package org.isep.sixquiprend.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

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
        this.clientName = "Player" + server.getClientCount() + 1;
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
        }
    }

    private Object processInstruction(Object instruction) {
        Object response = null;
        String command = (String) instruction;
        switch (command) {
            case "GET_TIME" :
                response = System.currentTimeMillis();
                break;
            case "GAME_START" :
                server.startGame();
                break;
        }

        return response;
    }

    public void sendMessage(Object message) {
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getClientName() {
        return clientName;
    }
}


