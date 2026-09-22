package ru.ivanov.queues;

import org.testcontainers.containers.PostgreSQLContainer;
import ru.ivanov.API.tools.QueryManager;

import java.sql.*;
import java.util.List;


public class DbManager {
    private final PostgreSQLContainer<?> postgres;

    public DbManager(PostgreSQLContainer<?> postgres) {
        this.postgres = postgres;
        try (Connection conn = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
             Statement stmt = conn.createStatement()) {
            stmt.execute(QueryManager.get("CREATE_ORDERS_TABLE_SQL"));
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка инициализации БД", e);
        }
    }

    public int getNumberOfOrders() {
        try (Connection conn = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(QueryManager.get("COUNT_ORDERS_SQL"))) {
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка чтения количества строк в БД", e);
        }
    }

    public void insertIntoOrders(List<Message> messages) {
        try (Connection conn = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())) {
            try (PreparedStatement stmt = conn.prepareStatement(QueryManager.get("INSERT_ORDER_SQL"))) {
                for (Message message : messages) {
                    stmt.setString(1, message.orderId());
                    stmt.setString(2, message.status());
                    stmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка записи в заказа", e);
        }
    }
}
