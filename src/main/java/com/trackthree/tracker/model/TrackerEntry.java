package com.trackthree.tracker.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class TrackerEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private int calories;
    private int protein;
    private int water;

    public TrackerEntry() {}

    public TrackerEntry(LocalDate date, int calories, int protein, int water) {
        this.date = date;
        this.calories = calories;
        this.protein = protein;
        this.water = water;
    }

    public Long getId() {
        return id;
    }

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
