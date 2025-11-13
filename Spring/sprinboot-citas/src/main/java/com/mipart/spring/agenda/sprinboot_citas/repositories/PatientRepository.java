package com.mipart.spring.agenda.sprinboot_citas.repositories;

import com.mipart.spring.agenda.sprinboot_citas.model.PatientModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<PatientModel, Long> {
    
}