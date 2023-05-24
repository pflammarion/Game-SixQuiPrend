package org.isep.sixquiprend.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final Server server;

    public ClientHandler(Socket clientSocket, Server server) throws IOException {
        this.clientSocket = clientSocket;
        this.server = server;
        this.outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        this.inputStream = new ObjectInputStream(clientSocket.getInputStream());
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object instruction = inputStream.readObject();

                Object response = processInstruction(instruction);

                outputStream.writeObject(response);

                System.out.println(instruction);

                if (instruction instanceof String && ((String) instruction).equals("GAME_START")) {
                    if (server.getClientCount() > 10) {
                        server.broadcastMessage("GAME_START");
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outputStream.close();
                clientSocket.close();
                server.removeClient(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Object processInstruction(Object instruction) {
        if (instruction instanceof String) {
            String command = (String) instruction;
            if (command.equals("GET_TIME")) {
                return System.currentTimeMillis();
            } else if (command.equals("PERFORM_ACTION")) {
                return "Action performed successfully!";
            } else {
                return "Invalid command";
            }
        }

        return "Invalid instruction";
    }

    public void sendMessage(Object message) {
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


