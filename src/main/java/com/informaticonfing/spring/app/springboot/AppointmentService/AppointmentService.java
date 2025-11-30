package com.informaticonfing.spring.app.springboot.AppointmentService;

import com.informaticonfing.spring.app.springboot.dto.AppointmentRequest;
import com.informaticonfing.spring.app.springboot.dto.AppointmentResponse;
import com.informaticonfing.spring.app.springboot.dto.AppointmentCalendarItem;
import com.informaticonfing.spring.app.springboot.model.*;
import com.informaticonfing.spring.app.springboot.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class AppointmentService {

        private final AppointmentRepository appointmentRepo;
        private final PatientRepository patientRepo;
        private final TherapistRepository therapistRepo;
        private final RoomRepository roomRepo;

        public AppointmentService(AppointmentRepository appointmentRepo,
                        PatientRepository patientRepo,
                        TherapistRepository therapistRepo,
                        RoomRepository roomRepo) {
                this.appointmentRepo = appointmentRepo;
                this.patientRepo = patientRepo;
                this.therapistRepo = therapistRepo;
                this.roomRepo = roomRepo;
        }

        @SuppressWarnings("null")
        @Transactional
        public AppointmentResponse create(AppointmentRequest req) {
                Patient patient;
                if (req.getPatientId() != null) {
                        patient = patientRepo.findById(req.getPatientId())
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Paciente no encontrado con ID: " + req.getPatientId()));
                } else {
                        // Crear nuevo paciente
                        if (req.getPatientNombre() == null || req.getPatientApellido() == null) {
                                throw new RuntimeException("Nombre y Apellido son requeridos para nuevo paciente");
                        }
                        patient = new Patient();
                        patient.setFirstName(req.getPatientNombre());
                        patient.setLastName(req.getPatientApellido());
                        patient.setPhone(req.getPatientTelefono());
                        patient.setEmail(req.getPatientEmail());
                        patient = patientRepo.save(patient);
                }

                Therapist therapist = therapistRepo.findById(req.getTherapistId())
                                .orElseThrow(() -> new RuntimeException(
                                                "Terapeuta no encontrado con ID: " + req.getTherapistId()));
                Room room = roomRepo.findById(req.getRoomId())
                                .orElseThrow(() -> new RuntimeException(
                                                "Sala no encontrada con ID: " + req.getRoomId()));

                LocalDateTime start = LocalDateTime.of(req.getDate(), req.getStartTime());
                LocalDateTime end = start.plusMinutes(req.getDurationMinutes() != null ? req.getDurationMinutes() : 60);

                Appointment a = new Appointment();
                a.setPatient(patient);
                a.setTherapist(therapist);
                a.setRoom(room);
                a.setSessionType(req.getSessionType());
                a.setStartDateTime(start);
                a.setEndDateTime(end);
                a.setPaymentProofPath(null);

                Appointment saved = appointmentRepo.save(a);

                return new AppointmentResponse(
                                "Cita creada correctamente",
                                saved.getId(),
                                req.getFolio() != null ? req.getFolio() : generarFolioPaciente(patient.getId()));
        }

        public List<AppointmentCalendarItem> getDay(LocalDate date) {
                LocalDateTime start = date.atStartOfDay();
                LocalDateTime end = date.atTime(LocalTime.MAX);
                return appointmentRepo.findByStartDateTimeBetween(start, end)
                                .stream()
                                .map(this::toCalendarItem)
                                .toList();
        }

        public List<AppointmentCalendarItem> getWeek(LocalDate monday) {
                LocalDateTime start = monday.atStartOfDay();
                LocalDateTime end = monday.plusDays(6).atTime(LocalTime.MAX);
                return appointmentRepo.findByStartDateTimeBetween(start, end)
                                .stream()
                                .map(this::toCalendarItem)
                                .toList();
        }

        private AppointmentCalendarItem toCalendarItem(Appointment a) {
                return new AppointmentCalendarItem(
                                a.getId(),
                                a.getSessionType(),
                                a.getPatient() != null
                                                ? a.getPatient().getFirstName() + " " + a.getPatient().getLastName()
                                                : null,
                                a.getTherapist() != null ? a.getTherapist().getName() : null,
                                a.getRoom() != null ? a.getRoom().getNombre() : null,
                                a.getStartDateTime(),
                                a.getEndDateTime());
        }

        private String generarFolioPaciente(Long patientId) {
                LocalDate now = LocalDate.now();
                return "FOL-" + now.getYear() + "-" + String.format("%04d", patientId);
        }

        public List<AppointmentResponse> findAll() {
                return appointmentRepo.findAll()
                                .stream()
                                .map(this::toResponse)
                                .toList();
        }

        private AppointmentResponse toResponse(Appointment a) {
                return new AppointmentResponse(
                                "Cita encontrada",
                                a.getId(),
                                generarFolioPaciente(a.getPatient() != null ? a.getPatient().getId() : 0L));
        }
}
