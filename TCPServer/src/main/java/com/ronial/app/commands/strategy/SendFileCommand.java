package com.ronial.app.commands.strategy;

import com.ronial.app.Server;
import com.ronial.app.Session;
import com.ronial.app.commands.ActionType;
import com.ronial.app.commands.Command;
import com.ronial.app.context.ContextProvider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

public class SendFileCommand implements Command {

    @Override
    public void execute(Session session) throws IOException {
        Server server = ContextProvider.get(Server.class);
        String fileName = session.getDis().readUTF();
        long fileSize = session.getDis().readLong();
        System.out.println("📥 Nhận ảnh: " + fileName + " (Kích thước: " + fileSize + " bytes)");

        byte[] imageData = new byte[(int) fileSize];
        session.getDis().readFully(imageData);

        synchronized (server.getSessions()) {
            for (Session client : server.getSessions()) {
                if (!client.equals(session)) {
                    try {
                        client.getDos().writeUTF(ActionType.SEND_IMG.name());
                        client.getDos().writeUTF(fileName);
                        client.getDos().writeLong(fileSize);
                        client.getDos().write(imageData);
                        client.getDos().flush();
                        System.out.println("📤 Đã gửi ảnh tới: " + client.getUser().getIp());
                    } catch (IOException e) {
                        System.out.println("❌ Lỗi gửi ảnh tới client: " + e.getMessage());
                    }
                }
            }
        }
        System.out.println("Send file completed");
    }
}
