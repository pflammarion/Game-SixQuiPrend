package org.isep.sixquiprend.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static ObjectOutputStream outputStream;
    private static ObjectInputStream inputStream;
    public static void main(String[] args) {
        int port = 4444; // Le port sur lequel le serveur écoutera les connexions

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started. Waiting for client connections...");

            // Attendre une connexion cliente
            clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

            // Initialiser les flux de sortie et d'entrée pour envoyer et recevoir des objets
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            inputStream = new ObjectInputStream(clientSocket.getInputStream());

            // Ici, vous pouvez commencer à envoyer et recevoir des messages du client
            // Utilisez outputStream.writeObject() pour envoyer des messages
            // Utilisez inputStream.readObject() pour recevoir des messages

            // Exemple : Réception d'un message du client
            Object message = inputStream.readObject();
            System.out.println("Received message from client: " + message);

            // Exemple : Envoyer une réponse au client
            String response = "Hello, client!";
            outputStream.writeObject(response);

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Error in communication: " + e.getMessage());
        } finally {
            // Fermez les flux et les sockets lorsque vous avez terminé
            try {
                if (outputStream != null)
                    outputStream.close();
                if (inputStream != null)
                    inputStream.close();
                if (clientSocket != null)
                    clientSocket.close();
                if (serverSocket != null)
                    serverSocket.close();
            } catch (IOException e) {
                System.out.println("Error while closing connection: " + e.getMessage());
            }
        }
    }
}