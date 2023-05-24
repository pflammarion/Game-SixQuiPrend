package org.isep.sixquiprend.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        int port = 4444;

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Le serveur est en attente de connexions sur le port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nouvelle connexion entrante : " + clientSocket);

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.run();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}