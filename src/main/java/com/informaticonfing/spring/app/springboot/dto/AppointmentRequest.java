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
    //-----identificadores para el paciente------

    @Schema(description = "ID del paciente que solicita la cita. Si es nulo, se creará un nuevo paciente con los datos proporcionados.", example = "12")
    private Long patientId;

    @Schema(description = "Nombre del paciente (Requerido si patientId es nulo).", example = "Juan")
    private String patientNombre;

    @Schema(description = "Apellido del paciente (Requerido si patientId es nulo).", example = "Pérez")
    private String patientApellido;

    @Schema(
            description = "Fecha de nacimiento del paciente (requerida cuando patientId es null). La edad no puede ser mayor a 100 años.",
            example = "1995-04-10"
    )
    private LocalDate patientBirthDate;

    @Schema(
            description = "Teléfono del paciente. Debe haber al menos teléfono o correo.",
            example = "999-123-4567"
    )
    private String patientTelefono;

    @Schema(
            description = "Correo electrónico del paciente. Debe haber al menos teléfono o correo.",
            example = "juan@example.com"
    )
    private String patientEmail;


    //-------Datos para la cita----
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


    //los pagos y comentarios  de los pacientes

    @Schema(description = "Cantidad a pagar en pesos mexicanos.", example = "300.00")
    private Double amountMx;

    @Schema(description = "Comentarios sobre la cita. Son obligatorios tanto para EVALUACION_INICIAL como para CITA_DE_TERAPIA (máx. 500 caracteres).",
            example = "Paciente refiere ansiedad moderada, primera sesión."
    )
    private String comments;

    @Schema(description = "Ruta o identificador del comprobante de pago. Es obligatorio para CITA_DE_TERAPIA y null para EVALUACION_INICIAL.",
            example = "uploads/comprobantes/pago-123.pdf"
    )
    private String paymentProofPath;

    @Schema(description = "Folio del paciente si ya existe. Si es primera cita, el sistema generará uno automáticamente.", example = "FOL-2025-0012")
    private String folio;

    // --------------------- GETTERS & SETTERS ---------------------

    public SessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(SessionType sessionType) {
        this.sessionType = sessionType;
    }

    //-----------------------------------
    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
//-------------------------

    public Long getTherapistId() {
        return therapistId;
    }

    public void setTherapistId(Long therapistId) {
        this.therapistId = therapistId;
    }
//---------------------------------------------

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

    // ---------- FOLIO ----------
    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    // ---------- DATOS DEL PACIENTE ----------
    public String getPatientNombre() {
        return patientNombre;
    }

    public void setPatientNombre(String patientNombre) {
        this.patientNombre = patientNombre;
    }

    public String getPatientApellido() {
        return patientApellido;
    }

    public void setPatientApellido(String patientApellido) {
        this.patientApellido = patientApellido;
    }

    public String getPatientTelefono() {
        return patientTelefono;
    }

    public void setPatientTelefono(String patientTelefono) {
        this.patientTelefono = patientTelefono;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

// ---------- CAMPOS NUEVOS AGREGADOS ----------

    // Fecha de nacimiento (no estaba en tu primer código)


    public LocalDate getPatientBirthDate() {
        return patientBirthDate;
    }

    public void setPatientBirthDate(LocalDate patientBirthDate) {
        this.patientBirthDate = patientBirthDate;
    }

    // Comentarios


    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    // Comprobante de pago (no estaba en tu primer código)

    public String getPaymentProofPath() {
        return paymentProofPath;
    }

    public void setPaymentProofPath(String paymentProofPath) {
        this.paymentProofPath = paymentProofPath;
    }
}