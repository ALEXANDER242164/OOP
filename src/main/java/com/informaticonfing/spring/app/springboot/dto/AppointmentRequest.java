package com.informaticonfing.spring.app.springboot.dto;

import com.informaticonfing.spring.app.springboot.model.SessionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

@Schema(name = "AppointmentRequest", description = "Datos necesarios para registrar una nueva cita en el sistema.")
public class AppointmentRequest {

        @Schema(description = "Tipo de sesión solicitada (Evaluación inicial o Cita terapéutica).", example = "INITIAL_EVALUATION")
        @NotNull
        private SessionType sessionType;

        @Schema(description = "ID del paciente que solicita la cita.", example = "12")
        @NotNull(message = "El patientId es requerido")
        private Long patientId;

        @Schema(description = "ID del terapeuta asignado a la cita.", example = "5")
        @NotNull(message = "El therapistId es requerido")
        private Long therapistId;

        @Schema(description = "ID de la sala donde se llevará a cabo la cita.", example = "3")
        @NotNull(message = "El roomId es requerido")
        private Long roomId;

        @Schema(description = "Fecha programada para la cita.", example = "2025-11-15")
        @NotNull
        private LocalDate date;

        @Schema(description = "Hora de inicio de la cita (formato 24h).", example = "10:30")
        @NotNull
        private LocalTime startTime;

        @Schema(description = "Duración de la cita en minutos. Por defecto 60.", example = "60")
        private Integer durationMinutes = 60;

        @Schema(description = "Cantidad a pagar en pesos mexicanos.", example = "300.00")
        private Double amountMx;

        @Schema(description = "Folio del paciente si ya existe. Si es primera cita, el sistema generará uno automáticamente.", example = "FOL-2025-0012")
        private String folio;

        // --------------------- GETTERS & SETTERS ---------------------

        public SessionType getSessionType() {
                return sessionType;
        }

        public void setSessionType(SessionType sessionType) {
                this.sessionType = sessionType;
        }

        public Long getPatientId() {
                return patientId;
        }

        public void setPatientId(Long patientId) {
                this.patientId = patientId;
        }

        public Long getTherapistId() {
                return therapistId;
        }

        public void setTherapistId(Long therapistId) {
                this.therapistId = therapistId;
        }

        public Long getRoomId() {
                return roomId;
        }

        public void setRoomId(Long roomId) {
                this.roomId = roomId;
        }

        public LocalDate getDate() {
                return date;
        }

        public void setDate(LocalDate date) {
                this.date = date;
        }

        public LocalTime getStartTime() {
                return startTime;
        }

        public void setStartTime(LocalTime startTime) {
                this.startTime = startTime;
        }

        public Integer getDurationMinutes() {
                return durationMinutes;
        }

        public void setDurationMinutes(Integer durationMinutes) {
                this.durationMinutes = durationMinutes;
        }

        public Double getAmountMx() {
                return amountMx;
        }

        public void setAmountMx(Double amountMx) {
                this.amountMx = amountMx;
        }

        public String getFolio() {
                return folio;
        }

        public void setFolio(String folio) {
                this.folio = folio;
        }
}
