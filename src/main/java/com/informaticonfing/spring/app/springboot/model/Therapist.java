package com.informaticonfing.spring.app.springboot.model;

import jakarta.persistence.*;

@Entity
@Table(name = "therapists")
public class Therapist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Therapist() {
    }

    public Therapist(String name) {
        this.name = name;
    }

    // Getters son OBLIGATORIOS para que el JSON salga bien
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
