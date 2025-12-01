package com.informaticonfing.spring.app.springboot.controllers;

import com.informaticonfing.spring.app.springboot.AppointmentService.ReminderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Reminders", description = "Enviar recordatorios por correo (test / inmediato)")
@RestController
@ConditionalOnBean(com.informaticonfing.spring.app.springboot.AppointmentService.ReminderService.class)
@RequestMapping("/api/reminders")
public class ReminderController {

    private final ReminderService reminderService;

    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @Operation(summary = "Enviar recordatorio inmediato para una cita por id")
    @PostMapping("/send/{appointmentId}")
    public ResponseEntity<?> sendForAppointment(@PathVariable("appointmentId") Long appointmentId) {
        String result = reminderService.sendReminderForAppointment(appointmentId);
        if ("OK".equals(result)) return ResponseEntity.ok("Recordatorio enviado");
        return ResponseEntity.badRequest().body(result);
    }

    @Operation(summary = "Enviar correo de prueba a una dirección")
    @PostMapping("/send-test")
    public ResponseEntity<?> sendTest(@RequestParam("to") String to, @RequestParam(value = "body", required = false) String body) {
        String result = reminderService.sendTestEmail(to, body);
        if ("OK".equals(result)) return ResponseEntity.ok("Correo de prueba enviado");
        return ResponseEntity.badRequest().body(result);
    }
}
