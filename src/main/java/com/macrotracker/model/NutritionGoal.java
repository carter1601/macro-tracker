package com.macrotracker.model;

import java.time.LocalDate;

public class NutritionGoal {

    private int id;
    private final int profileId;
    private final double calories;
    private final double proteinGrams;
    private final double carbsGrams;
    private final double fatGrams;
    private final LocalDate createdDate;

    public NutritionGoal(int id, int profileId, double calories,
                         double proteinGrams, double carbsGrams, double fatGrams,
                         LocalDate createdDate) {
        this.id = id;
        this.profileId = profileId;
        this.calories = calories;
        this.proteinGrams = proteinGrams;
        this.carbsGrams = carbsGrams;
        this.fatGrams = fatGrams;
        this.createdDate = createdDate;
    }

    public MacroTotals getTargets() {
        return new MacroTotals(calories, proteinGrams, carbsGrams, fatGrams);
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

    public double getCalories() {
        return calories;
    }

    public double getProteinGrams() {
        return proteinGrams;
    }

    public double getCarbsGrams() {
        return carbsGrams;
    }

    public double getFatGrams() {
        return fatGrams;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }
}