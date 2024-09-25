import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static Set<ClientHandler> clientHandlers = new HashSet<>();  // Store all client connections

    public static void main(String[] args) {
        int port = 12345;  // Server will listen on this port

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Chat server started on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();  // Accept new client connections
                System.out.println("New client connected");

                ClientHandler clientHandler = new ClientHandler(socket);  // Handle the new client
                clientHandlers.add(clientHandler);  // Add the new client to the set
                new Thread(clientHandler).start();  // Run client handler in a new thread
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Broadcast a message to all clients
    public static synchronized void broadcast(String message, ClientHandler sender) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler != sender) {  // Don't send the message back to the sender
                clientHandler.sendMessage(message);
            }
        }
    }

    // Remove a client when they disconnect
    public static synchronized void removeClient(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
    }
}

// ClientHandler class to handle communication with individual clients
class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String message;
        try {
            // Listen for incoming messages from the client
            while ((message = in.readLine()) != null) {
                System.out.println("Received: " + message);
                ChatServer.broadcast(message, this);  // Broadcast the message to other clients
            }
        } catch (IOException e) {
            System.out.println("Client disconnected");
        } finally {
            ChatServer.removeClient(this);  // Remove the client when they disconnect
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Send a message to the client
    public void sendMessage(String message) {
        out.println(message);
    }
}
