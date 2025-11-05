package com.mipart.spring.agenda.sprinboot_citas.model;

import jakarta.persistence.*;
import java.time.LocalTime;


public class ScheduleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dayOfWeek; // Ejemplo: "Lunes", "Martes"
    private LocalTime startTime; // Hora de inicio
    private LocalTime endTime;   // Hora de fin

    // Constructor vacío (requerido por JPA)
    public ScheduleModel() {}

    public ScheduleModel(String dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
}
