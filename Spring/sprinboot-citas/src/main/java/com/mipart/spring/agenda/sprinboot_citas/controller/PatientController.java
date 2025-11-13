package com.mipart.spring.agenda.sprinboot_citas.controller;


import com.mipart.spring.agenda.sprinboot_citas.model.DTOs.patientDTO;
import com.mipart.spring.agenda.sprinboot_citas.services.PacienteService;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;


@RestController
@RequestMapping("/api/v1/pacientes")
@Validated
public class PatientController {

    private final PacienteService svc;

    public PatientController(PacienteService svc) {
        this.svc = svc;
    }

    // Crear paciente, vemos que el prefijo o que debe llevar simepre antes de las peticiones http, deben ser /api/v1/pacientes
    //esto cada vez que se haga una peticion a la api.
    @PostMapping
    public ResponseEntity<patientDTO> createPaciente(@Valid @RequestBody patientDTO dto) {
        patientDTO created = svc.createPaciente(dto);
        URI location = URI.create(String.format("/api/v1/pacientes/%d", created.getId()));
        return ResponseEntity.created(location).body(created);
    }

    // Actualizar paciente (PUT /api/v1/pacientes/{id})
    @PutMapping("/{id}")
    public ResponseEntity<patientDTO> updatePaciente(
            @PathVariable Long id,
            @Valid @RequestBody patientDTO dto) {
        patientDTO updated = svc.updatePaciente(id, dto);
        return ResponseEntity.ok(updated);
    }

    // Obtener paciente por id (GET /api/v1/pacientes/{id})
    @GetMapping("/{id}")
    public ResponseEntity<patientDTO> getPaciente(@PathVariable Long id) {
        patientDTO dto = svc.getPaciente(id);
        return ResponseEntity.ok(dto);
    }

    // Lista de pacientes con paginación (GET /api/v1/pacientes?page=0&size=10)
    @GetMapping
    public ResponseEntity<Page<patientDTO>> listPacientes(Pageable pageable) {
        Page<patientDTO> page = svc.listPacientes(pageable);
        return ResponseEntity.ok(page);
    }


}
