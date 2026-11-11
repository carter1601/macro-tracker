package com.macrotracker.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    public static final String DEFAULT_URL = "jdbc:sqlite:macrotracker.db";

    private final String jdbcUrl;

    public Database(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(jdbcUrl);
        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        }
        return connection;
    }

    public void initializeSchema() {
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS profiles (
                id             INTEGER PRIMARY KEY AUTOINCREMENT,
                name           TEXT    NOT NULL,
                age            INTEGER NOT NULL,
                sex            TEXT    NOT NULL,
                height_cm      REAL    NOT NULL,
                weight_kg      REAL    NOT NULL,
            )
            """,
                """
            CREATE TABLE IF NOT EXISTS goals (
                id           INTEGER PRIMARY KEY AUTOINCREMENT,
                profile_id   INTEGER NOT NULL REFERENCES profiles(id) ON DELETE CASCADE,
                calories     REAL    NOT NULL,
                protein_g    REAL    NOT NULL,
                carbs_g      REAL    NOT NULL,
                fat_g        REAL    NOT NULL,
                created_date TEXT    NOT NULL
            )
            """,
                """
            CREATE TABLE IF NOT EXISTS foods (
                id           INTEGER PRIMARY KEY AUTOINCREMENT,
                name         TEXT    NOT NULL UNIQUE COLLATE NOCASE,
                serving_size REAL    NOT NULL,
                serving_unit TEXT    NOT NULL,
                calories     REAL    NOT NULL,
                protein_g    REAL    NOT NULL,
                carbs_g      REAL    NOT NULL,
                fat_g        REAL    NOT NULL
            )
            """,
                """
            CREATE TABLE IF NOT EXISTS food_log (
                id         INTEGER PRIMARY KEY AUTOINCREMENT,
                profile_id INTEGER NOT NULL REFERENCES profiles(id) ON DELETE CASCADE,
                food_id    INTEGER NOT NULL REFERENCES foods(id),
                log_date   TEXT    NOT NULL,
                servings   REAL    NOT NULL
            )
            """,
                "CREATE INDEX IF NOT EXISTS idx_food_log_profile_date ON food_log(profile_id, log_date)"
        };

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            for (String sql : createStatements) {
                statement.execute(sql);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not create the database tables", e);
        }
    }
}