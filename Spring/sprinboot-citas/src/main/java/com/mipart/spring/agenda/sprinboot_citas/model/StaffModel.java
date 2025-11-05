package com.mipart.spring.agenda.sprinboot_citas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "staff")
public class StaffModel extends BaseModel {

    private String username;
    private String passwordHash;
    private String role; // e.g. "ADMIN", "RECEPTION", "THERAPIST"

    public StaffModel() {}

    public StaffModel(String username, String passwordHash, String role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    // Getters / Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
