package com.macrotracker.model;

import java.time.LocalDate;

public class FoodLogEntry {

    private int id;
    private final int profileId;
    private final Food food;
    private final LocalDate date;
    private final double servings;

    public FoodLogEntry(int id, int profileId, Food food, LocalDate date, double servings) {
        this.id = id;
        this.profileId = profileId;
        this.food = food;
        this.date = date;
        this.servings = servings;
    }

    public MacroTotals getMacros() {
        return food.getMacrosForServings(servings);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProfileId() {
        return profileId;
    }

    public Food getFood() {
        return food;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getServings() {
        return servings;
    }
}