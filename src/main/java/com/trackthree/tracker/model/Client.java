package com.trackthree.tracker.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @Column(nullable = false, length = 64)
    private String code;

    @Column(length = 100)
    private String name;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private boolean admin;

    protected Client() {
    }

    public Client(String code, boolean admin) {
        this(code, null, admin);
    }

    public Client(String code, String name, boolean admin) {
        this.code = code;
        this.name = name;
        this.admin = admin;
        this.active = true;
    }

    public String getCode() {
        return code;
    }

    public boolean isAdmin() {
        return admin;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
