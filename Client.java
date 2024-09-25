import java.io.*;
import java.net.*;
import javax.swing.*;

public class Client {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private GUI gui;

    public Client(String serverAddress, int serverPort, GUI gui) {
        this.gui = gui;
        try {
            socket = new Socket(serverAddress, serverPort);  // Connect to the server
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            new Thread(new IncomingMessageHandler()).start();  // Start listening for incoming messages
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Unable to connect to server", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    // Send a message to the server
    public void sendMessage(String message) {
        out.println(message);  // Message is sent to the server
    }

    // Inner class to handle incoming messages from the server
    class IncomingMessageHandler implements Runnable {
        @Override
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    gui.addMessage(message);  // Display the incoming message in the chat window
                }
            } catch (IOException e) {
                System.err.println("Error reading from server: " + e.getMessage());
            }
        }
    }
}

