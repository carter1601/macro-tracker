package com.macrotracker.dao;

import com.macrotracker.model.NutritionGoal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;

public class GoalDao {

    private final Database database;

    public GoalDao(Database database) {
        this.database = database;
    }

    public NutritionGoal insert(NutritionGoal goal) {
        String sql = """
                INSERT INTO goals (profile_id, calories, protein_g, carbs_g, fat_g, created_date)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, goal.getProfileId());
            statement.setDouble(2, goal.getCalories());
            statement.setDouble(3, goal.getProteinGrams());
            statement.setDouble(4, goal.getCarbsGrams());
            statement.setDouble(5, goal.getFatGrams());
            statement.setString(6, goal.getCreatedDate().toString());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    goal.setId(keys.getInt(1));
                }
            }
            return goal;
        } catch (SQLException e) {
            throw new DataAccessException("Could not save goal", e);
        }
    }

    public Optional<NutritionGoal> findLatestForProfile(int profileId) {
        String sql = "SELECT * FROM goals WHERE profile_id = ? ORDER BY id DESC LIMIT 1";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, profileId);
            try (ResultSet rows = statement.executeQuery()) {
                if (!rows.next()) {
                    return Optional.empty();
                }
                return Optional.of(new NutritionGoal(
                        rows.getInt("id"),
                        rows.getInt("profile_id"),
                        rows.getDouble("calories"),
                        rows.getDouble("protein_g"),
                        rows.getDouble("carbs_g"),
                        rows.getDouble("fat_g"),
                        LocalDate.parse(rows.getString("created_date"))));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not load goal", e);
        }
    }
}