package com.ronial.app.socket;

import com.ronial.app.context.Context;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class SocketConf implements Context {
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;

    public SocketConf(String host, int port) throws IOException {
        socket = new Socket(host, port);
        dos = new DataOutputStream(socket.getOutputStream());
        dis = new DataInputStream(socket.getInputStream());
    }

    public Socket getSocket() {
        return socket;
    }
    public DataOutputStream getDos() {
        return dos;
    }
    public DataInputStream getDis() {
        return dis;
    }
    public List<String> getDataDIS() throws IOException {
        return Arrays.stream(this.dis.readUTF().split(";")).toList();
    }
}
