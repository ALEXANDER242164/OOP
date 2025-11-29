package com.informaticonfing.spring.app.springboot.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.informaticonfing.spring.app.springboot.model.AppointmentStatus;

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

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time", nullable = false)
    private LocalDateTime endDateTime;

    @Column(name = "amount_mx")
    private Double amountMx;

    @Column(name = "payment_proof_path")
    private String paymentProofPath;

    @Column(name = "comments", length = 500)
    private String comments;




    @Convert(converter = com.informaticonfing.spring.app.springboot.model.AppointmentStatusConverter.class)
    @Column(name = "appointment_status", length = 16,nullable = false)
    private AppointmentStatus appointmentStatus;

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

    public Double getAmountMx() { return amountMx; }
    public void setAmountMx(Double amountMx) { this.amountMx = amountMx; }




    public String getPaymentProofPath() { return paymentProofPath; }

    public void setPaymentProofPath(String paymentProofPath) {
        this.paymentProofPath = paymentProofPath;
    }


    public String getComments() { return comments; }

    public void setComments(String comments) { this.comments = comments; }

    public AppointmentStatus getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(AppointmentStatus appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }
}
