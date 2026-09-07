package ru.ivanov;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import ru.ivanov.models.Db.DbUserResponse;
import ru.ivanov.tools.DbManager;

import java.io.*;
import java.math.BigDecimal;
import java.sql.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DatabaseTest {

    @BeforeAll
    void connectToDb() {
        DbManager.setupDatabase();
    }

    @BeforeEach
    void prepareData() {
        DbManager.setWithTestData();
    }

    @AfterEach
    void cleanData() {
        DbManager.truncateTables();
    }

    @ParameterizedTest(name = "Поиск пользователя с email = {2}")
    @CsvFileSource(resources = "/users.csv", numLinesToSkip = 1)
    @DisplayName("Поиск пользователя по email")
    void testInsertUserAndOrderInTransaction(int id, String name, String email, String status, String createdAt) {
        DbUserResponse dbUser = DbManager.executeQuery("SQL_SELECT_BY_EMAIL", rs -> {
                    if (!rs.next()) {
                        return null;
                    }
                    DbUserResponse user = new DbUserResponse(
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("status"),
                            rs.getString("created_at")
                    );
                    if (rs.next()) {
                        throw new IllegalStateException("Запрос вернул более одной строки, нарушено условие уникальности");
                    }
                    return user;
                },
                email);
        SoftAssertions.assertSoftly(softly -> {
                    softly.assertThat(dbUser).as("Пользователь в БД").isNotNull();
                    if (dbUser != null) {
                        softly.assertThat(dbUser.name()).isEqualTo(name);
                        softly.assertThat(dbUser.email()).isEqualTo(email);
                        softly.assertThat(dbUser.status()).isEqualTo(status);
                    }
                }
        );
    }

    @Test
    @DisplayName("Поиск активных пользователей, созданных за последнюю неделю")
    void testActiveUsersCreatedLastWeek() {
        int rsCount = DbManager.executeQuery("SQL_SELECT_NEW_ACTIVE_USERS", rs -> {
            int count = 0;
            while (rs.next()) {
                count++;
            }
            return count;
        }, 7);
        Assertions.assertEquals(3, rsCount);
    }

    @Test
    @DisplayName("Поиск пользователей с заказами > 1000")
    void testGetUsersWithBigOrder() {
        String selectedName = DbManager.executeQuery("SQL_JOIN_LARGE_ORDERS", rs -> {
            if (!rs.next()) {
                return null;
            }
            String name = rs.getString("name");
            if (rs.next()) {
                throw new IllegalStateException("Запрос вернул более одной строки");
            }
            return name;
        }, 1000);
        SoftAssertions.assertSoftly(softly -> {
                    softly.assertThat(selectedName).as("Пользователь в БД").isNotNull();
                    if (selectedName != null) {
                        softly.assertThat(selectedName).isEqualTo("Anton");
                    }
                }
        );
    }

    @Test
    @DisplayName("Вставка нового пользователя с заказом")
    void testInsertNewUserWithOrder() {
        int count = DbManager.executeUpdate("SQL_INSERT_NEW_USER_WITH_ORDER", "Sergei", "sergei@mail.ru", "active",
                new Timestamp(System.currentTimeMillis()), new BigDecimal(350), "paid", new Timestamp(System.currentTimeMillis()));
        Assertions.assertEquals(1, count);
    }
}
