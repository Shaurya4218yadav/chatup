import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    // List to store connected clients
    private static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        int port = 12345; // Port number

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is running on port " + port + "...");

            while (true) {
                // Accept new client connections
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);

                // Create a handler for the new client
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);

                // Start the client thread to handle communication
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to broadcast messages to all clients
    public static void broadcastMessage(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    // Inner class to handle client connections
    static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;

            try {
                // Set up input/output streams
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Received: " + message);
                    // Broadcast the message to other clients
                    Server.broadcastMessage(message, this);
                }
            } catch (IOException e) {
                System.out.println("Client disconnected: " + clientSocket);
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Method to send a message to the client
        public void sendMessage(String message) {
            out.println(message);
        }
    }
}
