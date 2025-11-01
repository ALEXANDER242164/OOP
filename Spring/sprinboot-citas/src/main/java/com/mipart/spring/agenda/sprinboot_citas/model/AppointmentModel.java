package com.mipart.spring.agenda.sprinboot_citas.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class AppointmentModel extends BaseModel {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status; // SCHEDULED, CANCELLED, COMPLETED
    @Column(length = 1000)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private PatientModel patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "therapist_id")
    private TherapistModel therapist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private RoomMdel room;

    public AppointmentModel() {}

    public AppointmentModel(LocalDateTime startTime, LocalDateTime endTime, String status, String notes) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.notes = notes;
    }

    // Getters / Setters
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public PatientModel getPatient() { return patient; }
    public void setPatient(PatientModel patient) { this.patient = patient; }

    public TherapistModel getTherapist() { return therapist; }
    public void setTherapist(TherapistModel therapist) { this.therapist = therapist; }

    public RoomMdel getRoom() { return room; }
    public void setRoom(RoomMdel room) { this.room = room; }

    // Utility for conflict detection (simple)
    public boolean conflictsWith(AppointmentModel other) {
        return this.startTime.isBefore(other.endTime) && other.startTime.isBefore(this.endTime);
    }
}