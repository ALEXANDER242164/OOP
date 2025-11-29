package com.informaticonfing.spring.app.springboot.controllers;

import com.informaticonfing.spring.app.springboot.dto.AppointmentRequest;
import com.informaticonfing.spring.app.springboot.dto.AppointmentResponse;
import com.informaticonfing.spring.app.springboot.dto.AppointmentCalendarItem;
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
import java.time.LocalDate;

@Tag(name = "Appointments", description = "Operaciones para gestionar citas: creación, verificación y consulta.")
@RestController
@RequestMapping("/api/appointments")
@CrossOrigin("*")
public class AppointmentController {

        private final AppointmentService appointmentService;

        public AppointmentController(AppointmentService appointmentService) {
                this.appointmentService = appointmentService;
        }

                @Operation(summary = "Cambiar estado de una cita", description = "Actualiza el estado de la cita a pendiente, completado o cancelado.")
                @PutMapping("/{id}/status")
                public ResponseEntity<AppointmentResponse> updateStatus(@PathVariable("id") Long id, @RequestParam("status") String status) {
                        AppointmentResponse resp = appointmentService.updateStatus(id, status);
                        return ResponseEntity.ok(resp);
                }

                @Operation(summary = "Cancelar una cita", description = "Marca la cita como 'cancelado' sin eliminar el registro.")
                @PostMapping("/{id}/cancel")
                public ResponseEntity<AppointmentResponse> cancelAppointment(@PathVariable("id") Long id) {
                        // Uso del servicio existente para actualizar el estado a 'cancelado'
                        AppointmentResponse resp = appointmentService.updateStatus(id, "cancelado");
                        return ResponseEntity.ok(resp);
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

        @Operation(summary = "Citas del día", description = "Devuelve citas para una fecha específica.")
        @GetMapping("/day")
        public List<AppointmentCalendarItem> day(@RequestParam("date") LocalDate date) {
                return appointmentService.getDay(date);
        }

        @Operation(summary = "Citas de la semana", description = "Devuelve citas desde la fecha (lunes) hasta domingo.")
        @GetMapping("/week")
        public List<AppointmentCalendarItem> week(@RequestParam("monday") LocalDate monday) {
                return appointmentService.getWeek(monday);
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
