package com.ronial.app.socket;

public enum ActionType {
    SEND_IMG,
    NOTIFICATION,
    CLIENT_ON,
    CLIENT_OFF,
    SEND_MESSAGE;

    public static ActionType fromString(String s) {
        ActionType actionType = null;
        try {
            actionType = valueOf(s.toUpperCase());
        } catch (EnumConstantNotPresentException var3) {
            System.out.println(var3.getMessage());
        }
        return actionType;
    }
    @Override
    public String toString() {
        return this.name();
    }
}
