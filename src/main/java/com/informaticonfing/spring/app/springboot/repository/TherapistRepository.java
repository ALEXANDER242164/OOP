package com.informaticonfing.spring.app.springboot.repository;

import com.informaticonfing.spring.app.springboot.model.Therapist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TherapistRepository extends JpaRepository<Therapist, Long> {
}
