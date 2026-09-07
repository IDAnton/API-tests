package ru.ivanov.tools;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.sql.*;
import java.util.Properties;

public class DbManager {
    private static final Properties dbProperties = new Properties();
    public static final String usersCsvFile = "src/test/resources/users.csv";
    private static final String ordersCsvFile = "src/test/resources/orders.csv";

    static {
        try (InputStream input = DbManager.class.getClassLoader().getResourceAsStream("database.properties")) {
            if (input == null) {
                throw new RuntimeException("Не найден database.properties");
            }
            dbProperties.load(input);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                dbProperties.getProperty("db.url"),
                dbProperties.getProperty("db.user"),
                dbProperties.getProperty("db.password")
        );
    }

    public static <T> T executeQuery(String queryKey, ResultSetHandler<T> handler, Object... params) {
        String sql = QueryManager.get(queryKey);
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                return handler.handle(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка выполнения SQL запроса: " + queryKey, e);
        }
    }

    public static int executeUpdate(String queryKey, Object... params) {
        String sql = QueryManager.get(queryKey);
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка выполнения SQL запроса: " + queryKey, e);
        }
    }

    public static void setupDatabase() {
        truncateTables();
    }

    public static void setWithTestData() {
        createTableFromCsv(usersCsvFile, QueryManager.get("SQL_INSERT_USER"));
        createTableFromCsv(ordersCsvFile, QueryManager.get("SQL_INSERT_ORDERS"));
    }

    public static void truncateTables() {
        String sql = QueryManager.get("SQL_TRUNCATE");
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка очистки базы", e);
        }
    }

    private static void createTableFromCsv(String csvFile, String SQL_STATEMENT) {
        try (Reader reader = new FileReader(csvFile);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build());
             Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_STATEMENT)) {
            for (CSVRecord record : csvParser) {
                for (int i = 1; i < record.size(); i++) {
                    stmt.setString(i, record.get(i));
                }
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (IOException e) {
            throw new RuntimeException("Error while reading csv", e);
        } catch (SQLException e) {
            throw new RuntimeException("Error while writing to DV from csv", e);
        }
    }

    @FunctionalInterface
    public interface ResultSetHandler<T> {
        T handle(ResultSet rs) throws SQLException;
    }
}
