package org.isep.sixquiprend.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final int PORT = 4444;
    private static final int MAX_CONNECTIONS = 10;
    private final List<ClientHandler> clientHandlers;
    private List<List<Object>> roundInfo = new ArrayList<>();

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
                broadcastMessage(getHost());

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
        broadcastMessage(getHost());
    }

    public void startGame() {
        System.out.println("Starting the game...");
        List<String> message = new ArrayList<>();
        message.add("GAME_STARTED");
        message.add("GAME_STARTED");
        broadcastMessage(message);
    }

    public List<String> getPlayerList() {
        List<String> clientNameList = new ArrayList<>();
        clientNameList.add("_PLAYERLIST_");
        for (ClientHandler client: clientHandlers) {
            clientNameList.add(client.getClientName());
        }

        return clientNameList;
    }
    public Object getHost() {
        List<String> host = new ArrayList<>();
        host.add("_HOST_");
        host.add(clientHandlers.get(0).getClientName());
        return host;
    }

    public void sendMessageToClientByName(String clientName, String title, Object message) {
        for (ClientHandler client : clientHandlers) {
            if (client.getClientName().equals(clientName)) {
                client.sendMessage(title, message);
            }
        }
    }

    public int nameVerif(){
        int size = getClientCount();
        String comp = "Player " + size;
        try {
            String nameLastestPlayer = clientHandlers.get(clientHandlers.size() - 1).getClientName();
            if (comp.equals(nameLastestPlayer)){
                return size + 1;
            } else {
                return size;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public synchronized int getClientCount() {
        return clientHandlers.size();
    }

    public void setRoundInfo(List<Object> info) {
        List<List<Object>> infoToAdd = new ArrayList<>();

        if (this.roundInfo.isEmpty()) {
            infoToAdd.add(info);
        } else {
            boolean isPresent = false;
            for (List<Object> name : this.roundInfo) {
                if (name.get(0).equals(info.get(0))) {
                    isPresent = true;
                    break;
                }
            }
            if (!isPresent) {
                infoToAdd.add(info);
            }
        }

        this.roundInfo.addAll(infoToAdd);

        if (this.roundInfo.size() == clientHandlers.size()) {
            sendMessageToClientByName(clientHandlers.get(0).getClientName(), "_ROUNDINFO_", this.roundInfo);
            this.roundInfo = new ArrayList<>();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}