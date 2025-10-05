package orderscontrol.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection instance; // единственный экземпляр
    private Connection connection;

    private DBConnection() {
        try {
            String url = ConfigLoader.get("jdbc.url");
            String user = ConfigLoader.get("jdbc.username");
            String password = ConfigLoader.get("jdbc.password");

            // Создаём новое соединение
            this.connection = DriverManager.getConnection(url, user, password);
            System.out.println("Соединение с БД установлено");
        } catch (SQLException e) {
            System.err.println("Ошибка подключения к БД: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // Гарантируем, что существует только один объект DBConnection
    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // если вдруг соединение было закрыто — создаём заново
                String url = ConfigLoader.get("jdbc.url");
                String user = ConfigLoader.get("jdbc.username");
                String password = ConfigLoader.get("jdbc.password");
                connection = DriverManager.getConnection(url, user, password);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении соединения", e);
        }
        return connection;
    }
}

