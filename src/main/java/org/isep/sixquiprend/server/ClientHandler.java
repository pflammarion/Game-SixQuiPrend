package org.isep.sixquiprend.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;

    public ClientHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
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
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outputStream.close();
                clientSocket.close();
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
            } else if (command.equals("GET_PLAYERS")) {

                return "playerlist";
            } else {
                return "Invalid command";
            }
        }

        return "Invalid instruction";
    }
}

