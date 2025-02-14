package com.ronial.app.views;

import com.ronial.app.context.ContextProvider;
import com.ronial.app.socket.SocketService;
import com.ronial.app.utils.NotificationUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ChatView extends JFrame {
    private JPanel chatPanel;
    private JScrollPane scrollPane;
    private JTextField messageField;
    private JButton sendButton, imageButton;
    private Box chatBox;
    private SocketService socketService;

    public ChatView(String nickname) {
        socketService = ContextProvider.get(SocketService.class);
        setTitle("Chat Application - " + nickname);
        setSize(700, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Khu vực chat
        chatPanel = new JPanel(new BorderLayout());
        chatPanel.setBackground(Color.WHITE);

        chatBox = Box.createVerticalBox();
        scrollPane = new JScrollPane(chatBox);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(null);
        chatPanel.add(scrollPane, BorderLayout.CENTER);

        // Khu vực nhập tin nhắn
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        inputPanel.setBackground(new Color(240, 240, 240));

        messageField = new JTextField();
        messageField.setFont(new Font("Arial", Font.PLAIN, 16));
        messageField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        sendButton = createStyledButton("Gửi", new Color(51, 102, 255));
        imageButton = createStyledButton("📷", new Color(8, 234, 103));

        inputPanel.add(imageButton, BorderLayout.WEST);
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(chatPanel, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> sendMessage(messageField.getText(), null, true));
        messageField.addActionListener(e -> sendMessage(messageField.getText(), null, true));
        imageButton.addActionListener(e -> sendImage());

        setVisible(true);
        try {
            socketService.listenChat(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        return button;
    }

    public void sendMessage(String message, ImageIcon image, boolean isSender) {
        if ((message == null || message.trim().isEmpty()) && image == null) {
            return;
        }
        if (isSender && image == null) {
            try {
                socketService.sendMessage(message);
            } catch (IOException e) {
                NotificationUtils.error("Send message error", e.getMessage());
                throw new RuntimeException(e);
            }
        }

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        messagePanel.setBackground(isSender ? new Color(135, 206, 250) : Color.WHITE);
        messagePanel.setOpaque(true);

        if (message != null && !message.trim().isEmpty()) {
            JLabel textLabel = new JLabel("<html><p style='width:200px'>" + (isSender ? "Me: " + message : message) + "</p></html>");
            textLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            textLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            textLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            messagePanel.add(textLabel);
        }

        if (image != null) {
            JLabel imageLabel = new JLabel(image);
            imageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            messagePanel.add(imageLabel);
        }

        chatBox.add(messagePanel);
        chatBox.add(Box.createVerticalStrut(10));

        messageField.setText("");
        revalidate();
        repaint();
        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
    }

    private void sendImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Hình ảnh", "jpg", "png", "jpeg", "gif"));

        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                socketService.sendFile(file,this);
            } catch (IOException e) {
                NotificationUtils.error("Send image error", e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    public static ChatView launch(String nickname) {
        return new ChatView(nickname);
    }
}
