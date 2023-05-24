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
                // Read the instruction from the client
                Object instruction = inputStream.readObject();

                // Process the instruction and perform the corresponding action
                Object response = processInstruction(instruction);

                // Send the response back to the client
                outputStream.writeObject(response);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // Close the streams and socket when done
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
        // Implement the logic to process the received instruction and perform the corresponding action
        // You can use conditional statements, switch statements, or any other approach based on your requirements

        // Example:
        if (instruction instanceof String) {
            String command = (String) instruction;
            if (command.equals("GET_TIME")) {
                // Perform some action to get the current time
                return System.currentTimeMillis();
            } else if (command.equals("PERFORM_ACTION")) {
                // Perform some custom action
                return "Action performed successfully!";
            } else {
                return "Invalid command";
            }
        }

        return "Invalid instruction";
    }
}

