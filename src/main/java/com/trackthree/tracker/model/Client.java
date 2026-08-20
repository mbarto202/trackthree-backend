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

    @Column(nullable = false)
    private boolean admin;

    protected Client() {}

    public Client(String code, boolean admin) {
        this.code = code;
        this.admin = admin;
    }

    public String getCode() {
        return code;
    }

    public boolean isAdmin() {
        return admin;
    }
}
