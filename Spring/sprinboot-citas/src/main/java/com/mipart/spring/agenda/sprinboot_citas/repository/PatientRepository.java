package com.mipart.spring.agenda.sprinboot_citas.repository;

import com.mipart.spring.agenda.sprinboot_citas.model.PatientModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<PatientModel, Long> {
    Optional<PatientModel> findByEmail(String email);
    boolean existsByEmail(String email);
}