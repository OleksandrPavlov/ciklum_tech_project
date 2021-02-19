package com.ciklum.pavlov.util;

import java.sql.Connection;

public final class ThreadLocalConnection {
    private static final ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    private ThreadLocalConnection() {
    }

    public static Connection getConnection() {
        return threadLocal.get();
    }

    public static void setConnection(Connection connection) {
        threadLocal.set(connection);
    }

    public static void removeConnection() {
        threadLocal.remove();
    }
}
