package com.mipart.spring.agenda.sprinboot_citas.services;

import com.mipart.spring.agenda.sprinboot_citas.model.DTOs.patientDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface PacienteService {
    patientDTO createPaciente(patientDTO dto);
    patientDTO updatePaciente(Long id, patientDTO dto);
    patientDTO getPaciente(Long id);
    Page<patientDTO> listPacientes(Pageable pageable);
}
