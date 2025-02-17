package com.ronial.app;

import com.ronial.app.commands.ActionType;
import com.ronial.app.commands.CommandExecutor;
import com.ronial.app.commands.CommandType;
import com.ronial.app.context.Context;
import com.ronial.app.context.ContextProvider;
import com.ronial.app.entities.User;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class Session implements Runnable {
    private boolean isOnline;
    private final User user;
    private final Socket socket;
    private final DataOutputStream dos;
    private final DataInputStream dis;

    public Session(Socket socket) {
        this.isOnline = true;
        this.user = new User(socket.getRemoteSocketAddress().toString());
        this.socket = socket;
        try {
            this.dos = new DataOutputStream(socket.getOutputStream());
            this.dis = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        Server server = ContextProvider.get(Server.class);
        System.out.println(this.user.getIp()+ ": in");
        CommandExecutor commandExecutor = new CommandExecutor(this);
        try {
            while (socket.isConnected()) {
                String cmd = dis.readUTF();
                commandExecutor.execute(CommandType.fromString(cmd));
            }
        } catch (Exception e) {
            System.out.println("Server:" + user.getIp() + ":" + e.getMessage());
            this.isOnline = false;
            server.releaseSessions();
        }
    }
    public List<String> getDataDIS() throws IOException {
        return Arrays.stream(this.dis.readUTF().split(";")).toList();
    }
    public void emit(String action,String message) throws IOException {
        this.dos.writeUTF(action);
        this.dos.writeUTF(message);
        this.dos.flush();
    }
    public DataInputStream getDis() {
        return dis;
    }

    public DataOutputStream getDos() {
        return dos;
    }

    public Socket getSocket() {
        return socket;
    }
    public User getUser() {
        return user;
    }

    public boolean isOnline() {
        return isOnline;
    }
}
