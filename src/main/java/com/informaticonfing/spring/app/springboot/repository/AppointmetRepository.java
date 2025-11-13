package com.informaticonfing.spring.app.springboot.repository;
import com.informaticonfing.spring.app.springboot.model.appoinment;
import com.informaticonfing.spring.app.springboot.model.patient;
import com.informaticonfing.spring.app.springboot.model.Room;
import com.informaticonfing.spring.app.springboot.model.Therapist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;




public interface AppointmetRepository extends JpaRepository<appoinment, Long> {

    // Busca si hay choque de horario con el mismo paciente, terapeuta o sala
    @Query("""
           select a from appoinment a
           where (a.patient = :patient or a.therapist = :therapist or a.room = :room)
             and a.startDateTime < :newEnd
             and a.endDateTime > :newStart
           """)
    List<appoinment> findConflicts(
            patient patient,
            Therapist therapist,
            Room room,
            LocalDateTime newStart,
            LocalDateTime newEnd
    );
}
