import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI extends JFrame {
    private JTextArea chatArea;        // Main chat area for displaying messages
    private JTextField chatField;      // Text field for inputting messages
    private JButton sendButton;        // Button to send messages
    private JList<String> userList;    // List to show connected users
    private Client client;             // Client instance for networking

    public GUI() {
        // Set up the basic GUI layout
        setTitle("ChatUp");
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(40, 42, 54));

        // Configure the main chat area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setBackground(new Color(58, 60, 70));
        chatArea.setForeground(Color.WHITE);
        chatArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        chatScrollPane.setBorder(BorderFactory.createEmptyBorder());
        chatScrollPane.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        add(chatScrollPane, BorderLayout.CENTER);

        // Sidebar for connected users
        String[] users = {"User1", "User2", "User3"}; // Placeholder users, can be dynamically updated
        userList = new JList<>(users);
        userList.setBackground(new Color(58, 60, 70));
        userList.setForeground(Color.WHITE);
        userList.setFont(new Font("SansSerif", Font.PLAIN, 14));
        userList.setFixedCellHeight(30);
        userList.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane userScrollPane = new JScrollPane(userList);
        userScrollPane.setPreferredSize(new Dimension(150, 0));
        userScrollPane.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));
        add(userScrollPane, BorderLayout.WEST);

        // Bottom panel for message input and send button
        chatField = new JTextField();
        chatField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        chatField.setBackground(new Color(58, 60, 70));
        chatField.setForeground(Color.WHITE);
        chatField.setCaretColor(Color.WHITE);
        chatField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        sendButton = new JButton("Send");
        sendButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        sendButton.setBackground(new Color(98, 114, 164));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomPanel.add(chatField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        // Action for sending a message
        sendButton.addActionListener(e -> sendMessage());

        // Action for pressing Enter in the chat field
        chatField.addActionListener(e -> sendMessage());

        // Set window size, position, and visibility
        setSize(900, 600);
        setLocationRelativeTo(null); // Center the window
        setVisible(true);

        // Connect to the server (modify this to connect to your server)
        connectToServer();
    }

    // Method to connect the GUI to the server
    private void connectToServer() {
        String serverAddress = "localhost";  // Server address
        int serverPort = 12345;              // Server port

        client = new Client(serverAddress, serverPort, this);  // Pass GUI reference to the client
    }

    // Method to send a message to the server
    private void sendMessage() {
        String message = chatField.getText().trim();
        if (!message.isEmpty()) {
            client.sendMessage(message);  // Send the message to the server
            addMessage("You: " + message);  // Display the message in the chat area
            chatField.setText("");  // Clear the text field
        }
    }

    // Method to display a message in the chat area
    public void addMessage(String message) {
        chatArea.append(message + "\n");
    }

    // Method to close the client connection and exit the GUI
    @Override
    public void dispose() {
        client.closeConnection();  // Close the client connection
        super.dispose();
    }

    // Main method to start the GUI
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);  // Start the GUI on the event-dispatching thread
    }
}
