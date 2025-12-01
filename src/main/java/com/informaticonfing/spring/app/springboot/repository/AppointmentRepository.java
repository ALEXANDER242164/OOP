package com.informaticonfing.spring.app.springboot.repository;

import com.informaticonfing.spring.app.springboot.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
<<<<<<< Updated upstream
    List<Appointment> findByStartDateTimeBetween(LocalDateTime start, LocalDateTime end);
=======
        List<Appointment> findByStartDateTimeBetween(LocalDateTime start, LocalDateTime end);

        @Query("SELECT a FROM Appointment a WHERE " +
                        "((a.startDateTime < :end) AND (a.endDateTime > :start)) AND " +
                        "(a.room.id = :roomId OR a.therapist.id = :therapistId) AND " +
                        "a.appointmentStatus <> com.informaticonfing.spring.app.springboot.model.AppointmentStatus.CANCELADO")
        List<Appointment> findOverlappingAppointments(@Param("start") LocalDateTime start,
                        @Param("end") LocalDateTime end,
                        @Param("roomId") Long roomId,
                        @Param("therapistId") Long therapistId);

        @Query("SELECT COUNT(a) FROM Appointment a WHERE " +
                        "((a.startDateTime < :end) AND (a.endDateTime > :start)) AND " +
                        "a.appointmentStatus <> com.informaticonfing.spring.app.springboot.model.AppointmentStatus.CANCELADO")
        long countActiveAppointmentsInTimeRange(@Param("start") LocalDateTime start,
                        @Param("end") LocalDateTime end);

        @Query("SELECT COUNT(a) FROM Appointment a WHERE " +
                        "a.patient.id = :patientId AND " +
                        "a.startDateTime BETWEEN :startOfDay AND :endOfDay AND " +
                        "a.appointmentStatus <> com.informaticonfing.spring.app.springboot.model.AppointmentStatus.CANCELADO")
        long countAppointmentsByPatientAndDate(@Param("patientId") Long patientId,
                        @Param("startOfDay") LocalDateTime startOfDay,
                        @Param("endOfDay") LocalDateTime endOfDay);

                @Query("SELECT a FROM Appointment a WHERE a.startDateTime BETWEEN :start AND :end " +
                                "AND a.appointmentStatus <> com.informaticonfing.spring.app.springboot.model.AppointmentStatus.CANCELADO " +
                                "AND (a.patient.email IS NOT NULL AND a.patient.email <> '') " +
                                "AND (a.reminderSent = false OR a.reminderSent IS NULL)")
                List<Appointment> findAppointmentsForReminder(@Param("start") LocalDateTime start,
                                @Param("end") LocalDateTime end);
>>>>>>> Stashed changes
}
