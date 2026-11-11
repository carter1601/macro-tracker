package com.macrotracker.dao;

import com.macrotracker.model.UserProfile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProfileDao {

    private final Database database;

    public ProfileDao(Database database) {
        this.database = database;
    }

    public UserProfile insert(UserProfile profile) {
        String sql = """
                INSERT INTO profiles (name, age, sex, height_cm, weight_kg)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            fillParameters(statement, profile);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    profile.setId(keys.getInt(1));
                }
            }
            return profile;
        } catch (SQLException e) {
            throw new DataAccessException("Could not save profile", e);
        }
    }

    public void update(UserProfile profile) {
        String sql = """
                UPDATE profiles
                SET name = ?, age = ?, sex = ?, height_cm = ?, weight_kg = ?
                WHERE id = ?
                """;
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            fillParameters(statement, profile);
            statement.setInt(7, profile.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Could not update profile", e);
        }
    }

    public void delete(int profileId) {
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM profiles WHERE id = ?")) {
            statement.setInt(1, profileId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Could not delete profile", e);
        }
    }

    public List<UserProfile> findAll() {
        List<UserProfile> profiles = new ArrayList<>();
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM profiles ORDER BY name");
             ResultSet rows = statement.executeQuery()) {
            while (rows.next()) {
                profiles.add(mapRow(rows));
            }
            return profiles;
        } catch (SQLException e) {
            throw new DataAccessException("Could not load profiles", e);
        }
    }

    public Optional<UserProfile> findById(int profileId) {
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM profiles WHERE id = ?")) {
            statement.setInt(1, profileId);
            try (ResultSet rows = statement.executeQuery()) {
                return rows.next() ? Optional.of(mapRow(rows)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not load profile", e);
        }
    }

    private void fillParameters(PreparedStatement statement, UserProfile profile) throws SQLException {
        statement.setString(1, profile.getName());
        statement.setInt(2, profile.getAge());
        statement.setString(3, profile.getSex());
        statement.setDouble(4, profile.getHeightCm());
        statement.setDouble(5, profile.getWeightKg());
    }

    private UserProfile mapRow(ResultSet row) throws SQLException {
        return new UserProfile(
                row.getInt("id"),
                row.getString("name"),
                row.getInt("age"),
                row.getString("sex"),                     // "MALE" -> Sex.MALE
                row.getDouble("height_cm"),
                row.getDouble("weight_kg"));
    }
}