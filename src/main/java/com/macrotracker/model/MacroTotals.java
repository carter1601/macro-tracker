package com.macrotracker.model;

public class MacroTotals {

    private final double calories;
    private final double protein;
    private final double carbs;
    private final double fat;

    public MacroTotals(double calories, double protein, double carbs, double fat) {
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
    }

    public double getCalories() {
        return calories;
    }

    public double getProtein() {
        return protein;
    }

    public double getCarbs() {
        return carbs;
    }

    public double getFat() {
        return fat;
    }

    public MacroTotals plus(MacroTotals other) {
        return new MacroTotals(
                calories + other.calories,
                protein + other.protein,
                carbs + other.carbs,
                fat + other.fat);
    }

    public MacroTotals minus(MacroTotals other) {
        return new MacroTotals(
                calories - other.calories,
                protein - other.protein,
                carbs - other.carbs,
                fat - other.fat);
    }

    public MacroTotals times(double factor) {
        return new MacroTotals(calories * factor, protein * factor, carbs * factor, fat * factor);
    }

    @Override
    public String toString() {
        return String.format("%.0f calories, %.1f g protein, %.1f g carbs, %.1f g fat",
                calories, protein, carbs, fat);
    }
}