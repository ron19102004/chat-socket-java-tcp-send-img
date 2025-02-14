package com.ronial.app.commands;

import com.ronial.app.Session;

import java.io.IOException;

@FunctionalInterface
public interface Command {
    void execute(final Session session) throws IOException;
}
