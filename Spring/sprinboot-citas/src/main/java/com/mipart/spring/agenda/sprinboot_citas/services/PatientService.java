package com.mipart.spring.agenda.sprinboot_citas.services;

import com.mipart.spring.agenda.sprinboot_citas.model.PatientModel;
import com.mipart.spring.agenda.sprinboot_citas.repository.PatientRepository;
import com.mipart.spring.agenda.sprinboot_citas.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository repo;

    public PatientService(PatientRepository repo) { this.repo = repo; }

    @Transactional(readOnly = true)
    public List<PatientModel> listAll() { return repo.findAll(); }

    @Transactional(readOnly = true)
    public PatientModel getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + id));
    }

    /**
     * @param id
     * @param updated
     * @return
     */
    @Transactional
    public PatientModel update(Long id, PatientModel updated) {
        PatientModel p = getById(id);
        if (updated.getFirstName() != null) p.setFirstName(updated.getFirstName());
        if (updated.getLastName() != null) p.setLastName(updated.getLastName());
        if (updated.getBirthDate() != null) p.setBirthDate(updated.getBirthDate());
        if (updated.getEmail() != null) p.setEmail(updated.getEmail());
        if (updated.getPhone() != null) p.setPhone(updated.getPhone());
        return repo.save(p);
    }

    // optional create if needed
    @Transactional
    public PatientModel create(PatientModel p) {
        if (p.getEmail() != null && repo.existsByEmail(p.getEmail())) {
            throw new IllegalArgumentException("Email already used");
        }
        return repo.save(p);
    }
}
