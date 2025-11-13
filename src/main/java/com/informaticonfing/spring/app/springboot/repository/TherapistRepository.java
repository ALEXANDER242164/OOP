package com.informaticonfing.spring.app.springboot.repository;
import com.informaticonfing.spring.app.springboot.model.Therapist;
import org.springframework.data.jpa.repository.JpaRepository;



public interface TherapistRepository extends JpaRepository<Therapist, Long> {
}
