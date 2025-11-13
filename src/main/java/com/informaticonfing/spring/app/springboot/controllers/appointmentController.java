package com.informaticonfing.spring.app.springboot.controllers;

import com.informaticonfing.spring.app.springboot.dto.AppointmentRequest;
import com.informaticonfing.spring.app.springboot.dto.AppointmentResponse;
import com.informaticonfing.spring.app.springboot.AppointmentService.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Appointments",
        description = "Operaciones para gestionar citas: creación, verificación y consulta."
)
@RestController
@RequestMapping("/api/appointments")
@CrossOrigin("*")
public class appointmentController {

    private final AppointmentService appointmentService;

    public appointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Operation(
            summary = "Crear una nueva cita",
            description = "Registra una cita en el sistema validando horarios, disponibilidad, sala, terapeuta y reglas de negocio."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cita creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o conflicto con otra cita")
    })
    @PostMapping
    public ResponseEntity<AppointmentResponse> create(
            @Valid @RequestBody AppointmentRequest request
    ) {
        AppointmentResponse resp = appointmentService.create(request);
        return ResponseEntity.ok(resp);
    }

    @Operation(
            summary = "Verificar estado del backend",
            description = "Devuelve un mensaje simple indicando que la API está activa y funcionando."
    )
    @ApiResponse(responseCode = "200", description = "Estado del backend OK")
    @GetMapping("/info")
    public String info() {
        return "Backend funcionando correctamente ✅";
    }
}
