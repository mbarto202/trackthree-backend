package com.trackthree.model;

import java.time.LocalDate;

public class TrackerEntry {
    private LocalDate date;
    private int calories;
    private int protein;
    private int water;

    // Constructors
    public TrackerEntry() {}

    public TrackerEntry(LocalDate date, int calories, int protein, int water) {
        this.date = date;
        this.calories = calories;
        this.protein = protein;
        this.water = water;
    }

    // Getters and setters
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public int getWater() {
        return water;
    }

    public void setWater(int water) {
        this.water = water;
    }
}
