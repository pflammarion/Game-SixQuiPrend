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

                broadcastMessage(getPlayerList());

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
            clientHandler.sendMessage("_ALL_", message);
        }
    }

    public synchronized void removeClient(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
        broadcastMessage(getPlayerList());
    }

    public void startGame() {
        System.out.println("Starting the game...");
    }

    public Object getPlayerList() {
        List<String> clientNameList = new ArrayList<>();
        clientNameList.add("_PLAYERLIST_");
        for (ClientHandler client: clientHandlers) {
            clientNameList.add(client.getClientName());
        }

        return clientNameList;
    }

    public synchronized int getClientCount() {
        return clientHandlers.size();
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}