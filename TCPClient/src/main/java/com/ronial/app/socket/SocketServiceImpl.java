package com.ronial.app.socket;

import com.ronial.app.utils.LaunchThreadUtils;
import com.ronial.app.utils.NotificationUtils;
import com.ronial.app.views.ChatView;
import com.ronial.app.views.LaunchView;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.List;

public class SocketServiceImpl implements SocketService {
    private SocketConf socketConf;

    public SocketServiceImpl(SocketConf socketConf) {
        this.socketConf = socketConf;
    }

    @Override
    public void connect(LaunchView view) throws IOException {
        socketConf.getDos().writeUTF(CommandType.CLIENT_CONNECT_COMMAND.name());
        socketConf.getDos().writeUTF(view.getNickname());
        socketConf.getDos().flush();
        LaunchThreadUtils.launch(() -> {
            boolean isConnected = false;
            try {
                while (!isConnected) {
                    String act = socketConf.getDis().readUTF();
                    ActionType ac = ActionType.fromString(act);
                    List<String> data = socketConf.getDataDIS();
                    switch (ac) {
                        case CLIENT_ON -> {
                            isConnected = true;
                            NotificationUtils.success("Client connect", data.getFirst());
                            view.dispose();
                            ChatView.launch(view.getNickname());
                        }
                        case CLIENT_OFF -> {
                            NotificationUtils.error("Client connect", data.getFirst());
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void listenChat(ChatView view) {
        LaunchThreadUtils.launch(() -> {
            try {
                while (socketConf.getSocket().isConnected()) {
                    String act = socketConf.getDis().readUTF();
                    ActionType ac = ActionType.fromString(act);
                    switch (ac) {
                        case SEND_MESSAGE -> {
                            List<String> data = socketConf.getDataDIS();
                            view.sendMessage(data.getFirst(), null, false);
                        }

                        case SEND_IMG -> {
                            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                                // 1️⃣ Nhận tên file
                                String fileName = socketConf.getDis().readUTF();
                                System.out.println("📥 Nhận ảnh: " + fileName);

                                // 2️⃣ Nhận kích thước file
                                long fileSize = socketConf.getDis().readLong();
                                System.out.println("📥 Kích thước ảnh: " + fileSize + " bytes");

                                // 3️⃣ Nhận dữ liệu ảnh
                                byte[] imageData = new byte[(int) fileSize];
                                socketConf.getDis().readFully(imageData);

                                // 4️⃣ Hiển thị ảnh
                                ImageIcon imageIcon = new ImageIcon(imageData);
                                view.sendMessage(null, imageIcon, false);
                            } catch (IOException e) {
                                System.out.println("❌ Lỗi nhận ảnh: " + e.getMessage());
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void sendMessage(String message) throws IOException {
        socketConf.getDos().writeUTF(CommandType.BROADCAST_COMMAND.name());
        socketConf.getDos().writeUTF(message);
        socketConf.getDos().flush();
    }

    @Override
    public void sendFile(File file, ChatView view) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        socketConf.getDos().writeUTF(CommandType.SEND_FILE_COMMAND.name());
        socketConf.getDos().writeUTF(file.getName()); // Send filename first
        long fileSize = file.length();
        socketConf.getDos().writeLong(fileSize);
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = fis.read(buffer)) != -1) {
            socketConf.getDos().write(buffer, 0, bytesRead);
        }
        socketConf.getDos().flush();
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(file.getAbsolutePath())
                .getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
        view.sendMessage(null, imageIcon, true);
    }
}
