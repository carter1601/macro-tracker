package com.macrotracker.dao;

import com.macrotracker.model.Food;
import com.macrotracker.model.FoodLogEntry;
import com.macrotracker.model.MacroTotals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FoodLogDao {

    private final Database database;

    public FoodLogDao(Database database) {
        this.database = database;
    }

    public FoodLogEntry insert(FoodLogEntry entry) {
        String sql = """
                INSERT INTO food_log (profile_id, food_id, log_date, meal_type, servings)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, entry.getProfileId());
            statement.setInt(2, entry.getFood().getId());
            statement.setString(3, entry.getDate().toString());
            statement.setDouble(4, entry.getServings());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    entry.setId(keys.getInt(1));
                }
            }
            return entry;
        } catch (SQLException e) {
            throw new DataAccessException("Could not save food log entry", e);
        }
    }

    public void delete(int entryId) {
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM food_log WHERE id = ?")) {
            statement.setInt(1, entryId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Could not delete food log entry", e);
        }
    }

    public List<FoodLogEntry> findByProfileAndDate(int profileId, LocalDate date) {
        String sql = """
                SELECT l.id AS log_id, l.profile_id, l.log_date, l.servings,
                       f.id, f.name, f.serving_size, f.serving_unit,
                       f.calories, f.protein_g, f.carbs_g, f.fat_g
                FROM food_log l
                JOIN foods f ON f.id = l.food_id
                WHERE l.profile_id = ? AND l.log_date = ?
                ORDER BY l.id
                """;
        List<FoodLogEntry> entries = new ArrayList<>();
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, profileId);
            statement.setString(2, date.toString());
            try (ResultSet rows = statement.executeQuery()) {
                while (rows.next()) {
                    Food food = FoodDao.mapRow(rows);
                    entries.add(new FoodLogEntry(
                            rows.getInt("log_id"),
                            rows.getInt("profile_id"),
                            food,
                            LocalDate.parse(rows.getString("log_date")),
                            rows.getDouble("servings")));
                }
            }
            return entries;
        } catch (SQLException e) {
            throw new DataAccessException("Could not load food log", e);
        }
    }

    public Map<LocalDate, MacroTotals> sumByDay(int profileId, LocalDate from, LocalDate to) {
        String sql = """
                SELECT l.log_date,
                       SUM(f.calories  * l.servings) AS calories,
                       SUM(f.protein_g * l.servings) AS protein,
                       SUM(f.carbs_g   * l.servings) AS carbs,
                       SUM(f.fat_g     * l.servings) AS fat
                FROM food_log l
                JOIN foods f ON f.id = l.food_id
                WHERE l.profile_id = ? AND l.log_date BETWEEN ? AND ?
                GROUP BY l.log_date
                """;
        Map<LocalDate, MacroTotals> totalsByDay = new TreeMap<>();
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, profileId);
            statement.setString(2, from.toString());
            statement.setString(3, to.toString());
            try (ResultSet rows = statement.executeQuery()) {
                while (rows.next()) {
                    totalsByDay.put(
                            LocalDate.parse(rows.getString("log_date")),
                            new MacroTotals(
                                    rows.getDouble("calories"),
                                    rows.getDouble("protein"),
                                    rows.getDouble("carbs"),
                                    rows.getDouble("fat")));
                }
            }
            return totalsByDay;
        } catch (SQLException e) {
            throw new DataAccessException("Could not total the food log", e);
        }
    }

    public int countEntriesForFood(int foodId) {
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT COUNT(*) FROM food_log WHERE food_id = ?")) {
            statement.setInt(1, foodId);
            try (ResultSet rows = statement.executeQuery()) {
                return rows.next() ? rows.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not check food usage", e);
        }
    }
}