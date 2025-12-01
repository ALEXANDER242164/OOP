package com.informaticonfing.spring.app.springboot.dto;

import com.informaticonfing.spring.app.springboot.model.SessionType;
import java.time.LocalDateTime;

public class AppointmentCalendarItem {
    private Long id;
    private SessionType sessionType;
    private String patientNombre;
    private String therapistNombre;
    private String roomNombre;
    private Long roomId;
    private LocalDateTime start;
    private LocalDateTime end;
    private String status;
    private String patientFolio;

    public AppointmentCalendarItem(Long id,
            SessionType sessionType,
            String patientNombre,
            String therapistNombre,
            String roomNombre,
            Long roomId,
            LocalDateTime start,
            LocalDateTime end,
            String status,
            String patientFolio) {
        this.id = id;
        this.sessionType = sessionType;
        this.patientNombre = patientNombre;
        this.therapistNombre = therapistNombre;
        this.roomNombre = roomNombre;
        this.roomId = roomId;
        this.start = start;
        this.end = end;
        this.status = status;
        this.patientFolio = patientFolio;
    }

    public Long getId() {
        return id;
    }

    public SessionType getSessionType() {
        return sessionType;
    }

    public String getPatientNombre() {
        return patientNombre;
    }

    public String getTherapistNombre() {
        return therapistNombre;
    }

    public String getRoomNombre() {
        return roomNombre;
    }

    public Long getRoomId() {
        return roomId;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public String getStatus() {
        return status;
    }

    public String getPatientFolio() {
        return patientFolio;
    }
}
