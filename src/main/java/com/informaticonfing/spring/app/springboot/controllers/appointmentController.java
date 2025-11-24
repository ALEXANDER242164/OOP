package com.informaticonfing.spring.app.springboot.controllers;

import com.informaticonfing.spring.app.springboot.dto.AppointmentRequest;
import com.informaticonfing.spring.app.springboot.dto.AppointmentResponse;
import com.informaticonfing.spring.app.springboot.AppointmentService.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Appointments", description = "Operaciones para gestionar citas: creación, verificación y consulta.")
@RestController
@RequestMapping("/api/appointments")
@CrossOrigin("*")
public class appointmentController {

        private final AppointmentService appointmentService;

        public appointmentController(AppointmentService appointmentService) {
                this.appointmentService = appointmentService;
        }

        @Operation(summary = "Crear una nueva cita", description = "Registra una cita en el sistema validando horarios, disponibilidad, sala, terapeuta y reglas de negocio.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Cita creada correctamente"),
                        @ApiResponse(responseCode = "400", description = "Datos inválidos o conflicto con otra cita")
        })
        @PostMapping
        public ResponseEntity<AppointmentResponse> create(
                        @Valid @RequestBody AppointmentRequest request) {
                AppointmentResponse resp = appointmentService.create(request);
                return ResponseEntity.ok(resp);
        }

        @Operation(summary = "Verificar estado del backend", description = "Devuelve un mensaje simple indicando que la API está activa y funcionando.")
        @ApiResponse(responseCode = "200", description = "Estado del backend OK")
        @GetMapping("/info")
        public String info() {
                return "Backend funcionando correctamente ✅";
        }

        @Operation(summary = "Listar todas las citas", description = "Obtiene el listado completo de citas registradas.")
        @GetMapping
        public ResponseEntity<List<AppointmentResponse>> getAll() {
                return ResponseEntity.ok(appointmentService.findAll());
        }

        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public Map<String, String> handleValidationExceptions(
                        MethodArgumentNotValidException ex) {
                Map<String, String> errors = new HashMap<>();
                ex.getBindingResult().getAllErrors().forEach((error) -> {
                        String fieldName = ((FieldError) error).getField();
                        String errorMessage = error.getDefaultMessage();
                        errors.put(fieldName, errorMessage);
                });
                return errors;
        }
}
