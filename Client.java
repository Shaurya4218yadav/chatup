import java.io.*;
import java.net.*;

public class Client {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private GUI gui;  // Reference to the GUI

    // Constructor to connect to the server and pass the GUI reference
    public Client(String serverAddress, int serverPort, GUI gui) {
        this.gui = gui;
        try {
            // Connect to the server
            socket = new Socket(serverAddress, serverPort);
            gui.addMessage("Connected to the server");

            // Set up input/output streams
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Start a thread to listen for incoming messages
            new Thread(new IncomingMessagesHandler()).start();
        } catch (IOException e) {
            e.printStackTrace();
            gui.addMessage("Error connecting to the server: " + e.getMessage());
        }
    }

    // Method to send messages to the server
    public void sendMessage(String message) {
        out.println(message);  // Send message to the server
    }

    // Inner class to handle incoming messages from the server
    class IncomingMessagesHandler implements Runnable {
        @Override
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    gui.addMessage("Server: " + message);  // Display received messages in the chat
                }
            } catch (IOException e) {
                gui.addMessage("Server disconnected.");
            }
        }
    }

    // Close the client connection
    public void closeConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}