package com.mipart.spring.agenda.sprinboot_citas.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mipart.spring.agenda.sprinboot_citas.model.PatientModel;
import java.util.List;


import com.mipart.spring.agenda.sprinboot_citas.services.PatientService;

import jakarta.validation.Valid;




@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService service;

    public PatientController(PatientService service) { this.service = service; }

    @GetMapping
    public List<PatientModel> listAll() {
        return service.listAll();
    }

    @GetMapping("/{id}")
    public PatientModel getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public PatientModel update(@PathVariable Long id, @Valid @RequestBody PatientModel body) {
        return service.update(id, body);
    }

        // NO crear POST aquí: pacientes ya creados en la BD, lo hace otro equipo.
}
