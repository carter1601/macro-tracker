package com.macrotracker.model;

public class Food {

    private int id;
    private String name;
    private double servingSize;     // e.g. 100
    private String servingUnit;     // e.g. "g", "cup", "slice"
    private double calories;
    private double proteinGrams;
    private double carbsGrams;
    private double fatGrams;

    public Food(int id, String name, double servingSize, String servingUnit,
                double calories, double proteinGrams, double carbsGrams, double fatGrams) {
        this.id = id;
        this.name = name;
        this.servingSize = servingSize;
        this.servingUnit = servingUnit;
        this.calories = calories;
        this.proteinGrams = proteinGrams;
        this.carbsGrams = carbsGrams;
        this.fatGrams = fatGrams;
    }

    public MacroTotals getMacrosPerServing() {
        return new MacroTotals(calories, proteinGrams, carbsGrams, fatGrams);
    }

    public MacroTotals getMacrosForServings(double servings) {
        return getMacrosPerServing().times(servings);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getServingSize() {
        return servingSize;
    }

    public void setServingSize(double servingSize) {
        this.servingSize = servingSize;
    }

    public String getServingUnit() {
        return servingUnit;
    }

    public void setServingUnit(String servingUnit) {
        this.servingUnit = servingUnit;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getProteinGrams() {
        return proteinGrams;
    }

    public void setProteinGrams(double proteinGrams) {
        this.proteinGrams = proteinGrams;
    }

    public double getCarbsGrams() {
        return carbsGrams;
    }

    public void setCarbsGrams(double carbsGrams) {
        this.carbsGrams = carbsGrams;
    }

    public double getFatGrams() {
        return fatGrams;
    }

    public void setFatGrams(double fatGrams) {
        this.fatGrams = fatGrams;
    }
}