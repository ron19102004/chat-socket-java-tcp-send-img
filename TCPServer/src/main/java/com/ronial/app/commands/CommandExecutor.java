package com.ronial.app.commands;


import com.ronial.app.Session;
import com.ronial.app.commands.strategy.BroadCastCommand;
import com.ronial.app.commands.strategy.ClientConnectCommand;
import com.ronial.app.commands.strategy.SendFileCommand;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class CommandExecutor {
    private final HashMap<CommandType, Class<? extends Command>> commands;
    private final Session session;

    public CommandExecutor(Session session) {
        this.session = session;
        this.commands = new HashMap<>();
        putCommand(CommandType.CLIENT_CONNECT_COMMAND, ClientConnectCommand.class);
        putCommand(CommandType.SEND_FILE_COMMAND, SendFileCommand.class);
        putCommand(CommandType.BROADCAST_COMMAND, BroadCastCommand.class);
    }

    private void putCommand(CommandType type, Class<? extends Command> command) {
        this.commands.put(type, command);
    }

    public void execute(CommandType commandType) throws NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException, IOException {
        Class<? extends Command> commandClass = commands.get(commandType);
        if (commandClass != null) {
            Command command = (commandClass.getDeclaredConstructor()).newInstance();
            command.execute(session);
        }
    }
}
