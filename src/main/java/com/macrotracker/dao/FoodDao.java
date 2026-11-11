package com.macrotracker.dao;

import com.macrotracker.model.Food;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FoodDao {
    private final Database database;

    public FoodDao(Database database) {
        this.database = database;
    }

    public Food insert(Food food) {
        String sql = """
                INSERT INTO foods (name, serving_size, serving_unit, calories, protein_g, carbs_g, fat_g)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            fillParameters(statement, food);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    food.setId(keys.getInt(1));
                }
            }
            return food;
        } catch (SQLException e) {
            throw new DataAccessException("Could not save food", e);
        }
    }

    public void update(Food food) {
        String sql = """
                UPDATE foods
                SET name = ?, serving_size = ?, serving_unit = ?, calories = ?, protein_g = ?, carbs_g = ?, fat_g = ?
                WHERE id = ?
                """;
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            fillParameters(statement, food);
            statement.setInt(8, food.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Could not update food", e);
        }
    }

    public void delete(int foodId) {
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM foods WHERE id = ?")) {
            statement.setInt(1, foodId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Could not delete food", e);
        }
    }

    public List<Food> findAll() {
        return query("SELECT * FROM foods ORDER BY name COLLATE NOCASE", null);
    }

    public List<Food> searchByName(String text) {
        return query("SELECT * FROM foods WHERE name LIKE ? ORDER BY name COLLATE NOCASE", "%" + text + "%");
    }

    public Optional<Food> findByName(String name) {
        List<Food> matches = query("SELECT * FROM foods WHERE name = ? COLLATE NOCASE", name);
        return matches.isEmpty() ? Optional.empty() : Optional.of(matches.get(0));
    }

    public int count() {
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM foods");
             ResultSet rows = statement.executeQuery()) {
            return rows.next() ? rows.getInt(1) : 0;
        } catch (SQLException e) {
            throw new DataAccessException("Could not count foods", e);
        }
    }

    private List<Food> query(String sql, String parameter) {
        List<Food> foods = new ArrayList<>();
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            if (parameter != null) {
                statement.setString(1, parameter);
            }
            try (ResultSet rows = statement.executeQuery()) {
                while (rows.next()) {
                    foods.add(mapRow(rows));
                }
            }
            return foods;
        } catch (SQLException e) {
            throw new DataAccessException("Could not load foods", e);
        }
    }

    private void fillParameters(PreparedStatement statement, Food food) throws SQLException {
        statement.setString(1, food.getName());
        statement.setDouble(2, food.getServingSize());
        statement.setString(3, food.getServingUnit());
        statement.setDouble(4, food.getCalories());
        statement.setDouble(5, food.getProteinGrams());
        statement.setDouble(6, food.getCarbsGrams());
        statement.setDouble(7, food.getFatGrams());
    }

    static Food mapRow(ResultSet row) throws SQLException {
        return new Food(
                row.getInt("id"),
                row.getString("name"),
                row.getDouble("serving_size"),
                row.getString("serving_unit"),
                row.getDouble("calories"),
                row.getDouble("protein_g"),
                row.getDouble("carbs_g"),
                row.getDouble("fat_g"));
    }
}