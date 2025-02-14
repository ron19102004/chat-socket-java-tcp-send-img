package com.ronial.app.utils;

public class LaunchThreadUtils {
    private LaunchThreadUtils() {}
    public static void launch(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
