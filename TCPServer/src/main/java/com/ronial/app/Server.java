package com.ronial.app;

import com.ronial.app.context.Context;
import com.ronial.app.context.ContextProvider;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Context {
    private final ArrayList<Session> sessions;
    public Server(int port) throws IOException {
        ContextProvider.register(Server.class,this);
        this.sessions = new ArrayList<>();
        final ServerSocket server = new ServerSocket(port);
        while (!server.isClosed()){
            final Socket socket = server.accept();
            Session session = new Session(socket);
            synchronized (sessions){
                sessions.add(session);
            }
            new Thread(session).start();
        }
    }

    public ArrayList<Session> getSessions() {
        return sessions;
    }
    public void releaseSessions(){
        synchronized (sessions){
            sessions.removeIf(session -> !session.isOnline());
        }
        System.out.println("***Released sessions***");
        System.out.println("Sessions current: " + sessions.size());
    }

    public static void main(String[] args) throws IOException {
        new Server(8888);
    }
}
