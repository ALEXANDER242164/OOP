package com.informaticonfing.spring.app.springboot.repository;
import com.informaticonfing.spring.app.springboot.model.patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<patient, Long> {
};
