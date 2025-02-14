package com.ronial.app.commands;

public enum CommandType {
    CLIENT_CONNECT_COMMAND,
    BROADCAST_COMMAND,
    SEND_FILE_COMMAND;

    public static CommandType fromString(String s) {
        CommandType command = null;
        try {
            command = valueOf(s.toUpperCase());
        } catch (EnumConstantNotPresentException var3) {
            System.out.println(var3.getMessage());
        }
        return command;
    }
}