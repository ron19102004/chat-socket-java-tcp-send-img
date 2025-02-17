package com.ronial.app;

import com.ronial.app.commands.ActionType;
import com.ronial.app.context.Context;
import com.ronial.app.context.ContextProvider;
import com.ronial.app.utils.DateUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Server implements Context {
    private final ArrayList<Session> sessions;

    public Server(int port) throws IOException {
        ContextProvider.register(Server.class, this);
        this.sessions = new ArrayList<>();
        final ServerSocket server = new ServerSocket(port);
        while (!server.isClosed()) {
            final Socket socket = server.accept();
            Session session = new Session(socket);
            synchronized (sessions) {
                sessions.add(session);
            }
            new Thread(session).start();
        }
    }

    public ArrayList<Session> getSessions() {
        return sessions;
    }

    public void releaseSessions() {
        synchronized (sessions) {
            List<String> nickNameOffline = new ArrayList<>();
            sessions.removeIf(session -> {
                if (!session.isOnline()) {
                    nickNameOffline.add(session.getUser().getNickname());
                    return true;
                }
                return false;
            });
            nickNameOffline.forEach(nickname -> {
                systemBroadcast(nickname + " is offline");
            });
        }
        System.out.println("***Released sessions***");
        System.out.println("Sessions current: " + sessions.size());
    }

    public void systemBroadcast(String message) {
        sessions.forEach(client -> {
            try {
                String response = "[" + DateUtils.formatInstant(Instant.now()) + "]System: " + message;
                client.emit(ActionType.SEND_MESSAGE.name(), response);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void main(String[] args) throws IOException {
        new Server(8888);
    }
}
