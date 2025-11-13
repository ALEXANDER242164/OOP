package com.mipart.spring.agenda.sprinboot_citas.services;

import com.mipart.spring.agenda.sprinboot_citas.model.DTOs.patientDTO;
import com.mipart.spring.agenda.sprinboot_citas.model.PatientModel;
import com.mipart.spring.agenda.sprinboot_citas.repositories.PatientRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PacienteServiceImpl implements PacienteService {
    private final PatientRepository repo;

    public PacienteServiceImpl(PatientRepository repo) {
        this.repo = repo;
    }

    private patientDTO mapToDto(PatientModel p) {
        patientDTO d = new patientDTO();
        d.setId(p.getId());
        d.setFirstname(p.getFirstName());
        d.setLastname(p.getLastName());
        d.setEmail(p.getEmail());
        d.setPhone(p.getPhone());
        d.setBirthDate(p.getBirthDate());
        d.setRoom(p.getRoom());
        d.setCreatedAt(p.getCreatedAt());
        d.setUpDateTime(p.getUpdatedAt());
        return d;
    }

    private PatientModel mapToEntity(patientDTO d) {
        PatientModel p = new PatientModel();
        p.setFirstName(d.getFirstname());
        p.setLastName(d.getLastname());
        p.setEmail(d.getEmail());
        p.setPhone(d.getPhone());
        p.setBirthDate(d.getBirthDate());
        p.setRoom(d.getRoom());
        return p;
    }

    @Override
    @Transactional
    public patientDTO createPaciente(patientDTO dto) {
        PatientModel entity = mapToEntity(dto);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpDateTime(LocalDateTime.now());
        PatientModel saved = repo.save(entity);
        return mapToDto(saved);
    }

    @Override
    @Transactional
    public patientDTO updatePaciente(Long id, patientDTO dto) {
        PatientModel existing = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado con id " + id));

        // Actualizamos los campos del paciente
        existing.setFirstName(dto.getFirstname());
        existing.setLastName(dto.getLastname());
        existing.setEmail(dto.getEmail());
        existing.setPhone(dto.getPhone());
        existing.setBirthDate(dto.getBirthDate());
        existing.setRoom(dto.getRoom());
        existing.setUpDateTime(LocalDateTime.now()); // Actualizamos la fecha de modificación

        PatientModel updated = repo.save(existing);
        return mapToDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public patientDTO getPaciente(Long id) {
        PatientModel patient = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado con id " + id));
        return mapToDto(patient);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<patientDTO> listPacientes(Pageable pageable) {
        return repo.findAll(pageable).map(this::mapToDto);
    }
}
