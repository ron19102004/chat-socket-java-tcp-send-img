package com.ronial.app;

import com.ronial.app.context.ContextProvider;
import com.ronial.app.socket.SocketConf;
import com.ronial.app.socket.SocketService;
import com.ronial.app.socket.SocketServiceImpl;
import com.ronial.app.views.LaunchView;

import javax.swing.*;
import java.io.IOException;

public class Client {
    public static void main(String[] args) throws IOException, UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        ContextProvider.register(SocketService.class,
                new SocketServiceImpl(new SocketConf("localhost", 8888)));
        LaunchView.launch();
    }
}