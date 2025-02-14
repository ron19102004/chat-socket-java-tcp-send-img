package com.ronial.app.commands.strategy;

import com.ronial.app.Server;
import com.ronial.app.Session;
import com.ronial.app.commands.ActionType;
import com.ronial.app.commands.Command;
import com.ronial.app.context.ContextProvider;

import java.io.IOException;
import java.util.Objects;

public class BroadCastCommand implements Command {
    @Override
    public void execute(Session session) throws IOException {
        String message = session.getDataDIS().getFirst();
        Server server = ContextProvider.get(Server.class);
        server.getSessions().forEach(client -> {
            if (!client.equals(session)) {
                System.out.println(client.getUser());
                System.out.println(session.getUser());
                try {
                    String response = session.getUser().getNickname() + ": " + message;
                    client.emit(ActionType.SEND_MESSAGE.name(), response);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
