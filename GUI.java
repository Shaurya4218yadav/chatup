import java.awt.*;
import javax.swing.*;

public class GUI extends JFrame {
    private JTextArea chatArea;        // Main chat area for displaying messages
    private JTextField chatField;      // Text field for inputting messages
    private JButton sendButton;        // Button to send messages
    private JList<String> userList;    // List to show connected users
    private Client client;             // Client instance for networking

    public GUI() {
        // Set up the basic GUI layout
        setTitle("ChatUp Beta version");
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(34, 40, 49));  // Dark background

        // Configure the main chat area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setBackground(new Color(57, 62, 70));  // Chat area background
        chatArea.setForeground(Color.WHITE);            // Text color
        chatArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));  // Padding for chat area

        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        chatScrollPane.setBorder(BorderFactory.createEmptyBorder());  // No border
        chatScrollPane.setBackground(new Color(57, 62, 70));
        chatScrollPane.setPreferredSize(new Dimension(700, 400));

        add(chatScrollPane, BorderLayout.CENTER);

        // Sidebar for connected users
        String[] users = {"User1", "User2", "User3"};  // Placeholder users, can be dynamically updated
        userList = new JList<>(users);
        userList.setBackground(new Color(57, 62, 70));  // Sidebar background
        userList.setForeground(Color.WHITE);            // Text color
        userList.setFont(new Font("SansSerif", Font.PLAIN, 14));
        userList.setFixedCellHeight(40);  // Increased height for modern look
        userList.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // Padding inside user list
        userList.setCellRenderer(new ModernUserCellRenderer());  // Custom renderer for modern user list

        JScrollPane userScrollPane = new JScrollPane(userList);
        userScrollPane.setPreferredSize(new Dimension(200, 0));  // Set the width of the sidebar
        userScrollPane.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));  // Separator line
        add(userScrollPane, BorderLayout.WEST);

        // Bottom panel for message input and send button
        chatField = new JTextField();
        chatField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        chatField.setBackground(new Color(57, 62, 70));  // Input field background
        chatField.setForeground(Color.WHITE);            // Input text color
        chatField.setCaretColor(Color.WHITE);            // Cursor color
        chatField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 110)), 
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));  // Subtle border with padding

        sendButton = new JButton("Send");
        sendButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        sendButton.setBackground(new Color(98, 114, 164));  // Send button color
        sendButton.setForeground(Color.WHITE);              // Button text color
        sendButton.setFocusPainted(false);
        sendButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));  // Padding around button
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));  // Hand cursor on hover
        sendButton.setOpaque(true);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // Padding around bottom panel
        bottomPanel.setBackground(new Color(34, 40, 49));  // Dark background for bottom panel
        bottomPanel.add(chatField, BorderLayout.CENTER);   // Add input field to the center
        bottomPanel.add(sendButton, BorderLayout.EAST);    // Add send button to the right
        add(bottomPanel, BorderLayout.SOUTH);

        // Action for sending a message
        sendButton.addActionListener(e -> sendMessage());

        // Action for pressing Enter in the chat field
        chatField.addActionListener(e -> sendMessage());

        // Connect to the chat server
        connectToServer();

        // Set window size, position, and visibility
        setSize(900, 600);  // Window size
        setLocationRelativeTo(null);  // Center the window on the screen
        setVisible(true);
    }

    // Custom renderer for the user list for a modern look
    class ModernUserCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setOpaque(true);
            label.setBackground(isSelected ? new Color(98, 114, 164) : new Color(57, 62, 70));  // Change background on selection
            label.setForeground(Color.WHITE);
            label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // Padding for each user
            return label;
        }
    }

    // Connect to the server and start client
    private void connectToServer() {
        String serverAddress = JOptionPane.showInputDialog(this, "Enter Server IP Address:", "Server Connection", JOptionPane.QUESTION_MESSAGE);
        int serverPort = 12345;  // The port should match with the server

        client = new Client(serverAddress, serverPort, this);
    }

    // Method to send a message
    private void sendMessage() {
        String message = chatField.getText().trim();
        if (!message.isEmpty()) {
            client.sendMessage("You: " + message);  // Send the message to the server
            chatField.setText("");  // Clear the input field
        }
    }
    // Method to display a message in the chat area
    public void addMessage(String message) {
        SwingUtilities.invokeLater(() -> chatArea.append(message + "\n"));
    }


    // Main method to start the GUI
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);  // Start the GUI on the event-dispatching thread
    }
}
