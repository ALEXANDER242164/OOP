package com.informaticonfing.spring.app.springboot.dto;

import com.informaticonfing.spring.app.springboot.model.SessionType;
import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentDetail {
    private Long id;
    private SessionType sessionType;
    private Long patientId;
    private String patientNombre;
    private Long therapistId;
    private String therapistNombre;
    private Long roomId;
    private String roomNombre;
    private LocalDate date;
    private LocalTime startTime;
    private Integer durationMinutes;
    private String comments;

    public AppointmentDetail() {}

    public AppointmentDetail(Long id, SessionType sessionType, Long patientId, String patientNombre, Long therapistId, String therapistNombre, Long roomId, String roomNombre, LocalDate date, LocalTime startTime, Integer durationMinutes, String comments) {
        this.id = id;
        this.sessionType = sessionType;
        this.patientId = patientId;
        this.patientNombre = patientNombre;
        this.therapistId = therapistId;
        this.therapistNombre = therapistNombre;
        this.roomId = roomId;
        this.roomNombre = roomNombre;
        this.date = date;
        this.startTime = startTime;
        this.durationMinutes = durationMinutes;
        this.comments = comments;
    }

    public Long getId() { return id; }
    public SessionType getSessionType() { return sessionType; }
    public Long getPatientId() { return patientId; }
    public String getPatientNombre() { return patientNombre; }
    public Long getTherapistId() { return therapistId; }
    public String getTherapistNombre() { return therapistNombre; }
    public Long getRoomId() { return roomId; }
    public String getRoomNombre() { return roomNombre; }
    public LocalDate getDate() { return date; }
    public LocalTime getStartTime() { return startTime; }
    public Integer getDurationMinutes() { return durationMinutes; }
    public String getComments() { return comments; }

    public void setId(Long id) { this.id = id; }
    public void setSessionType(SessionType sessionType) { this.sessionType = sessionType; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public void setPatientNombre(String patientNombre) { this.patientNombre = patientNombre; }
    public void setTherapistId(Long therapistId) { this.therapistId = therapistId; }
    public void setTherapistNombre(String therapistNombre) { this.therapistNombre = therapistNombre; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }
    public void setRoomNombre(String roomNombre) { this.roomNombre = roomNombre; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
    public void setComments(String comments) { this.comments = comments; }
}
