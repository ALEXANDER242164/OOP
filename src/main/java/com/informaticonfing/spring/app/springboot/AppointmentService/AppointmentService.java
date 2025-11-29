package com.informaticonfing.spring.app.springboot.AppointmentService;

import com.informaticonfing.spring.app.springboot.dto.AppointmentRequest;
import com.informaticonfing.spring.app.springboot.dto.AppointmentResponse;
import com.informaticonfing.spring.app.springboot.dto.AppointmentCalendarItem;
import com.informaticonfing.spring.app.springboot.model.*;
import com.informaticonfing.spring.app.springboot.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import com.informaticonfing.spring.app.springboot.model.AppointmentStatus;

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

        @Transactional
        public AppointmentResponse create(AppointmentRequest req) {
                // 1. Validar Fin de Semana
                DayOfWeek day = req.getDate().getDayOfWeek();
                if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
                        throw new RuntimeException("No se pueden agendar citas en fines de semana.");
                }

                // 2. Validar Bloques de 1 Hora (Minutos = 0)
                if (req.getStartTime().getMinute() != 0) {
                        throw new RuntimeException("Las citas deben iniciar en punto de la hora (ej. 09:00, 10:00).");
                }

                // 3. Validar Horario (9:00 - 17:30)
                // Como son bloques de 1 hora, la última cita puede empezar a las 16:00 (termina
                // 17:00).
                if (req.getStartTime().getHour() < 9 || req.getStartTime().getHour() > 16) {
                        throw new RuntimeException(
                                        "El horario de atención es de 09:00 a 17:30. Última cita a las 16:00.");
                }

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
                        // Guardar primero para obtener ID
                        patient = patientRepo.save(patient);
                        // Generar folio aleatorio único de 6 dígitos
                        Random rnd = new Random();
                        String generatedFolio;
                        int attempts = 0;
                        do {
                                generatedFolio = String.format("%06d", rnd.nextInt(1_000_000));
                                attempts++;
                                if (attempts > 100) {
                                        // Fallback determinístico si hay demasiados intentos
                                        generatedFolio = String.format("%06d", patient.getId());
                                        break;
                                }
                        } while (patientRepo.existsByFolio(generatedFolio));
                        patient.setFolio(generatedFolio);
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

                // 4. Validar Solapamiento (Incluyendo Canceladas para bloquear el horario)
                List<Appointment> overlaps = appointmentRepo.findOverlappingAppointments(
                                start, end, req.getRoomId(), req.getTherapistId());

                if (!overlaps.isEmpty()) {
                        throw new RuntimeException(
                                        "El horario, sala o terapeuta no están disponibles (Conflicto con otra cita).");
                }

                Appointment a = new Appointment();
                a.setPatient(patient);
                a.setTherapist(therapist);
                a.setRoom(room);
                a.setSessionType(req.getSessionType());
                a.setStartDateTime(start);
                a.setEndDateTime(end);
                a.setPaymentProofPath(null);
                // Set default status
                a.setAppointmentStatus(AppointmentStatus.PENDIENTE);

                Appointment saved = appointmentRepo.save(a);

                // Asegurar que el paciente tenga folio (por si era paciente existente sin
                // folio)
                if (patient.getFolio() == null || patient.getFolio().isBlank()) {
                        Random rnd2 = new Random();
                        String gen;
                        int attempts2 = 0;
                        do {
                                gen = String.format("%06d", rnd2.nextInt(1_000_000));
                                attempts2++;
                                if (attempts2 > 100) {
                                        gen = String.format("%06d", patient.getId());
                                        break;
                                }
                        } while (patientRepo.existsByFolio(gen));
                        patient.setFolio(gen);
                        patientRepo.save(patient);
                }

                return new AppointmentResponse(
                                "Cita creada correctamente",
                                saved.getId(),
                                patient.getFolio(),
                                saved.getAppointmentStatus() != null ? saved.getAppointmentStatus().toString()
                                                : AppointmentStatus.PENDIENTE.toString());
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
                                a.getEndDateTime(),
                                a.getAppointmentStatus() != null ? a.getAppointmentStatus().toString() : "PENDIENTE",
                                a.getPatient() != null ? a.getPatient().getFolio() : null);
        }

        private String generarFolioPaciente(Long patientId) {
                return String.format("%06d", patientId);
        }

        public List<AppointmentResponse> findAll() {
                return appointmentRepo.findAll()
                                .stream()
                                .map(this::toResponse)
                                .toList();
        }

        @Transactional
        public AppointmentResponse updateStatus(Long appointmentId, String statusStr) {
                Appointment a = appointmentRepo.findById(appointmentId)
                                .orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + appointmentId));
                AppointmentStatus s = AppointmentStatus.fromDbValue(statusStr);
                if (s == null) {
                        throw new RuntimeException(
                                        "Estado inválido. Valores permitidos: pendiente, completado, cancelado");
                }
                a.setAppointmentStatus(s);
                Appointment saved = appointmentRepo.save(a);
                String folio = saved.getPatient() != null ? saved.getPatient().getFolio() : null;
                return new AppointmentResponse("Estado actualizado", saved.getId(), folio,
                                saved.getAppointmentStatus().toString());
        }

        private AppointmentResponse toResponse(Appointment a) {
                String folio = a.getPatient() != null ? a.getPatient().getFolio() : null;
                if (folio == null || folio.isBlank()) {
                        folio = generarFolioPaciente(a.getPatient() != null ? a.getPatient().getId() : 0L);
                }
                return new AppointmentResponse(
                                "Cita encontrada",
                                a.getId(),
                                folio,
                                a.getAppointmentStatus() != null ? a.getAppointmentStatus().toString()
                                                : AppointmentStatus.PENDIENTE.toString());
        }
}
