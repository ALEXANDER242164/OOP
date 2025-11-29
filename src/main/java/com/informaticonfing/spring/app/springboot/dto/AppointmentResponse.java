package com.informaticonfing.spring.app.springboot.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "AppointmentResponse",
        description = "Respuesta generada después de registrar una cita en el sistema."
)
public class AppointmentResponse {

    @Schema(
            description = "Mensaje del sistema indicando el resultado de la operación.",
            example = "Cita creada correctamente."
    )
    private String message;

    @Schema(
            description = "ID único asignado a la cita recién creada.",
            example = "42"
    )
    private Long appointmentId;

    @Schema(
            description = "Folio del paciente asociado a la cita. Si es primera cita, fue generado automáticamente.",
            example = "FOL-2025-0043"
    )
    private String patientFolio;
    
    @Schema(description = "Estado de la cita: pendiente, completado o cancelado", example = "pendiente")
    private String appointmentStatus;

    // 👇 ESTE es el constructor que está usando tu service
    public AppointmentResponse(String message, Long appointmentId, String patientFolio, String appointmentStatus) {
        this.message = message;
        this.appointmentId = appointmentId;
        this.patientFolio = patientFolio;
        this.appointmentStatus = appointmentStatus;
    }

    // getters (por si el controller los serializa a JSON)
    public String getMessage() {
        return message;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public String getPatientFolio() {
        return patientFolio;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }
}
