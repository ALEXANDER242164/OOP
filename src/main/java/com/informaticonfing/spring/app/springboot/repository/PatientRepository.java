package com.informaticonfing.spring.app.springboot.repository;

// Cambia .entity.Patient por .model.Patient
import com.informaticonfing.spring.app.springboot.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
}
