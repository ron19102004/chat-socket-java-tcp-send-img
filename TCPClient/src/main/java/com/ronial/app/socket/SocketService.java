package com.ronial.app.socket;

import com.ronial.app.context.Context;
import com.ronial.app.views.ChatView;
import com.ronial.app.views.LaunchView;

import java.io.File;
import java.io.IOException;

public interface SocketService extends Context {
    void connect(LaunchView view) throws IOException;
    void listenChat(ChatView view) throws IOException;
    void sendMessage(String message) throws IOException;
    void sendFile(File file, ChatView view) throws IOException;
}
