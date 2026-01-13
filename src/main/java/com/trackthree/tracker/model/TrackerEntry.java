package com.trackthree.tracker.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(
    name = "tracker_entry",
    uniqueConstraints = @UniqueConstraint(columnNames = {"client_code", "date"})
)
public class TrackerEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_code", nullable = false, length = 64)
    private String clientCode;

    @Column(nullable = false)
    private LocalDate date;

    private int calories;
    private int protein;
    private int water;

    public TrackerEntry() {}

    public TrackerEntry(String clientCode, LocalDate date, int calories, int protein, int water) {
        this.clientCode = clientCode;
        this.date = date;
        this.calories = calories;
        this.protein = protein;
        this.water = water;
    }

     public Long getId() { return id; }

    public String getClientCode() { return clientCode; }
    public void setClientCode(String clientCode) { this.clientCode = clientCode; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public int getCalories() { return calories; }
    public void setCalories(int calories) { this.calories = calories; }

    public int getProtein() { return protein; }
    public void setProtein(int protein) { this.protein = protein; }

    public int getWater() { return water; }
    public void setWater(int water) { this.water = water; }
}
