package org.isep.sixquiprend.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final int PORT = 4444;
    private static final int MAX_CONNECTIONS = 10;

    private List<ClientHandler> clientHandlers;

    public Server() {
        clientHandlers = new ArrayList<>();
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server is listening for connections on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New connection established: " + clientSocket);

                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clientHandlers.add(clientHandler);

                Thread thread = new Thread(clientHandler);
                thread.start();

                if (clientHandlers.size() > MAX_CONNECTIONS) {
                    startGame();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void broadcastMessage(Object message) {
        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.sendMessage(message);
        }
    }

    public synchronized void removeClient(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
    }

    public void startGame() {
        System.out.println("Starting the game...");

        broadcastMessage("GAME_START");
        broadcastMessage(clientHandlers.toString());
    }

    public synchronized int getClientCount() {
        return clientHandlers.size();
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}