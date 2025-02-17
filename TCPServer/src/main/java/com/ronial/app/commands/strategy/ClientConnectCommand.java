package com.ronial.app.commands.strategy;

import com.ronial.app.Server;
import com.ronial.app.Session;
import com.ronial.app.commands.ActionType;
import com.ronial.app.commands.Command;
import com.ronial.app.context.ContextProvider;

import java.io.IOException;
import java.util.Optional;

public class ClientConnectCommand implements Command {
    @Override
    public void execute(Session session) throws IOException {
        Server server = ContextProvider.get(Server.class);
        String nickname = session.getDataDIS().getFirst().trim();
        if (nickname.isEmpty() || nickname.isBlank()){
            session.emit(ActionType.CLIENT_OFF.name(),  "Nickname is required");
            return;
        }
        session.getUser().setNickname(nickname);
        session.emit(ActionType.CLIENT_ON.name(),  nickname + " is connected");
        server.systemBroadcast(nickname + " is online");
    }
}
