package org.isep.sixquiprend.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class Client {
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public void connectToServer() {
        try {
            socket = new Socket("flaminfo.fr", 4444);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            System.out.println("Connected to the server.");

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

    public Object waitForResponse(){
        try {
            return inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("_ERROR_ in communication: " + e.getMessage());
        }
        return null;
    }

    private void closeConnection() {
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
}

