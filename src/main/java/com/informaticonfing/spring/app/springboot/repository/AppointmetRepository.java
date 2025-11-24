package com.informaticonfing.spring.app.springboot.repository;

import com.informaticonfing.spring.app.springboot.model.Appointment;
import com.informaticonfing.spring.app.springboot.model.Patient;
import com.informaticonfing.spring.app.springboot.model.Room;
import com.informaticonfing.spring.app.springboot.model.Therapist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmetRepository extends JpaRepository<Appointment, Long> {

  // Busca si hay choque de horario con el mismo paciente, terapeuta o sala
  @Query("""
      select a from Appointment a
      where (a.patient = :patient or a.therapist = :therapist or a.room = :room)
        and a.startDateTime < :newEnd
        and a.endDateTime > :newStart
      """)
  List<Appointment> findConflicts(
      Patient patient,
      Therapist therapist,
      Room room,
      LocalDateTime newStart,
      LocalDateTime newEnd);
}
