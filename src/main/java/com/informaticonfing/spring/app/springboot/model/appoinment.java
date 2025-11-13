package com.informaticonfing.spring.app.springboot.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class appoinment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SessionType sessionType;

    @ManyToOne
    private patient patient;

    @ManyToOne
    private Therapist therapist;

    @ManyToOne
    private Room room;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    private String paymentProofPath;

    public appoinment() {}

    // getters y setters
    public Long getId() { return id; }
    public SessionType getSessionType() { return sessionType; }
    public void setSessionType(SessionType sessionType) { this.sessionType = sessionType; }
    public patient getPatient() { return patient; }
    public void setPatient(patient patient) { this.patient = patient; }
    public Therapist getTherapist() { return therapist; }
    public void setTherapist(Therapist therapist) { this.therapist = therapist; }
    public Room getRoom() { return room; }
    public void setRoom(Room room) { this.room = room; }
    public LocalDateTime getStartDateTime() { return startDateTime; }
    public void setStartDateTime(LocalDateTime startDateTime) { this.startDateTime = startDateTime; }
    public LocalDateTime getEndDateTime() { return endDateTime; }
    public void setEndDateTime(LocalDateTime endDateTime) { this.endDateTime = endDateTime; }
    public String getPaymentProofPath() { return paymentProofPath; }
    public void setPaymentProofPath(String paymentProofPath) { this.paymentProofPath = paymentProofPath; }
}
