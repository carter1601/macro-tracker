package com.macrotracker.model;

public class UserProfile {

    private int id;
    private String name;
    private int age;
    private String sex;
    private double heightCm;
    private double weightKg;

    public UserProfile(int id, String name, int age, String sex, double heightCm, double weightKg) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.heightCm = heightCm;
        this.weightKg = weightKg;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public double getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(double heightCm) {
        this.heightCm = heightCm;
    }

    public double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(double weightKg) {
        this.weightKg = weightKg;
    }
}