package com.informaticonfing.spring.app.springboot.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment { // Nombre corregido (Mayúscula)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SessionType sessionType;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient; // Corregido: 'Patient' con P mayúscula

    @ManyToOne
    @JoinColumn(name = "therapist_id")
    private Therapist therapist;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    private String paymentProofPath;

    public Appointment() {
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(SessionType sessionType) {
        this.sessionType = sessionType;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Therapist getTherapist() {
        return therapist;
    }

    public void setTherapist(Therapist therapist) {
        this.therapist = therapist;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getPaymentProofPath() {
        return paymentProofPath;
    }

    public void setPaymentProofPath(String paymentProofPath) {
        this.paymentProofPath = paymentProofPath;
    }
}
