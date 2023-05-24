package org.isep.sixquiprend.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        int port = 4444; // Le port sur lequel le serveur écoutera les connexions

        try {
            // Créer une instance de ServerSocket pour écouter les connexions entrantes
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Le serveur est en attente de connexions sur le port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nouvelle connexion entrante : " + clientSocket);

                // Créer un thread pour gérer la communication avec le client
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.run();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}