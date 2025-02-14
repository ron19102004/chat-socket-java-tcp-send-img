package com.ronial.app.views;

import com.ronial.app.context.ContextProvider;
import com.ronial.app.socket.SocketService;
import com.ronial.app.utils.StatusUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class LaunchView extends JFrame {
    private SocketService socketService;
    private JTextField nicknameTextField;
    private JButton submitBtn;

    public LaunchView() {
        socketService = ContextProvider.get(SocketService.class);
        initComponents();
        eventHandlers();
        setTitle("Chat Application");
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Box Chat", SwingConstants.CENTER);
        titleLabel.setBackground(Color.WHITE);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 36));
        titleLabel.setForeground(new Color(51, 102, 255));
        add(titleLabel, BorderLayout.NORTH);

        // Center Panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // Nickname Label
        JLabel labelNickname = new JLabel("Nickname:");
        labelNickname.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        centerPanel.add(labelNickname, gbc);

        // Nickname TextField
        nicknameTextField = new JTextField("Enter your nickname", 30);
        nicknameTextField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        nicknameTextField.setPreferredSize(new Dimension(350, 60));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        centerPanel.add(nicknameTextField, gbc);

        // Submit Button
        submitBtn = new JButton("Join Chat");
        submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 24));
        submitBtn.setBackground(new Color(51, 102, 255));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFocusPainted(false);
        submitBtn.setPreferredSize(new Dimension(200, 50));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        centerPanel.add(submitBtn, gbc);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void eventHandlers() {
        StatusUtils nicknameTextFieldStatus = new StatusUtils();
        nicknameTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!nicknameTextFieldStatus.isTrue()) {
                    nicknameTextField.setText("");
                    nicknameTextFieldStatus.changeStatus();
                }
            }
        });
        nicknameTextField.addActionListener(e -> submitActionPerformed());
        submitBtn.addActionListener(e -> submitActionPerformed());
    }
    private void submitActionPerformed() {
        try {
            socketService.connect(this);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Failed to connect!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public String getNickname() {
        return nicknameTextField.getText();
    }

    public static LaunchView launch() {
        return new LaunchView();
    }
}
