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

                // Asegurar que el paciente tenga folio (por si era paciente existente sin folio)
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
                                saved.getAppointmentStatus() != null ? saved.getAppointmentStatus().toString() : AppointmentStatus.PENDIENTE.toString());
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

        public com.informaticonfing.spring.app.springboot.dto.AppointmentDetail getAppointmentDetail(Long id) {
                Appointment a = appointmentRepo.findById(id)
                                .orElseThrow(() -> new RuntimeException("Cita no encontrada."));
                LocalDate date = a.getStartDateTime().toLocalDate();
                java.time.LocalTime startTime = a.getStartDateTime().toLocalTime();
                long minutes = java.time.Duration.between(a.getStartDateTime(), a.getEndDateTime()).toMinutes();
                Integer duration = (int) minutes;
                return new com.informaticonfing.spring.app.springboot.dto.AppointmentDetail(
                                a.getId(),
                                a.getSessionType(),
                                a.getPatient() != null ? a.getPatient().getId() : null,
                                a.getPatient() != null ? a.getPatient().getFirstName() + " " + a.getPatient().getLastName() : null,
                                a.getTherapist() != null ? a.getTherapist().getId() : null,
                                a.getTherapist() != null ? a.getTherapist().getName() : null,
                                a.getRoom() != null ? a.getRoom().getId() : null,
                                a.getRoom() != null ? a.getRoom().getNombre() : null,
                                date,
                                startTime,
                                duration,
                                a.getComments()
                );
        }

        @Transactional
        public AppointmentResponse updateAppointment(Long appointmentId, AppointmentRequest req) {
                Appointment a = appointmentRepo.findById(appointmentId)
                                .orElseThrow(() -> new RuntimeException("Cita no encontrada."));

                // Validar Fin de Semana
                java.time.DayOfWeek day = req.getDate().getDayOfWeek();
                if (day == java.time.DayOfWeek.SATURDAY || day == java.time.DayOfWeek.SUNDAY) {
                        throw new RuntimeException("No se pueden agendar citas en fines de semana.");
                }

                // Validar Minutos = 0
                if (req.getStartTime().getMinute() != 0) {
                        throw new RuntimeException("Las citas deben iniciar en punto de la hora (ej. 09:00, 10:00).");
                }

                // Validar Horario
                if (req.getStartTime().getHour() < 9 || req.getStartTime().getHour() > 16) {
                        throw new RuntimeException("El horario de atención es de 09:00 a 17:30. Última cita a las 16:00.");
                }

                // Validar comentarios
                String comments = req.getComments();
                if (comments != null) {
                        comments = comments.trim();
                        if (comments.length() > 500) {
                                throw new RuntimeException("Los comentarios no pueden exceder 500 caracteres.");
                        }
                }

                Therapist therapist = therapistRepo.findById(req.getTherapistId())
                                .orElseThrow(() -> new RuntimeException("Terapeuta no encontrado."));
                Room room = roomRepo.findById(req.getRoomId())
                                .orElseThrow(() -> new RuntimeException("Sala no encontrada."));

                java.time.LocalDateTime start = java.time.LocalDateTime.of(req.getDate(), req.getStartTime());
                java.time.LocalDateTime end = start.plusMinutes(req.getDurationMinutes() != null ? req.getDurationMinutes() : 60);

                // Contar citas activas en rango y excluir la propia si aplica
                long activeAppointmentsCount = appointmentRepo.countActiveAppointmentsInTimeRange(start, end);
                boolean selfOverlaps = a.getStartDateTime().isBefore(end) && a.getEndDateTime().isAfter(start);
                if (selfOverlaps) {
                        activeAppointmentsCount = Math.max(0, activeAppointmentsCount - 1);
                }
                if (activeAppointmentsCount >= 2) {
                        throw new RuntimeException("Lo sentimos, ya se ha alcanzado el límite máximo de citas (2) para este horario.");
                }

                // Verificar solapamientos con otros
                java.util.List<Appointment> overlaps = appointmentRepo.findOverlappingAppointments(start, end, req.getRoomId(), req.getTherapistId())
                                .stream()
                                .filter(x -> !x.getId().equals(appointmentId))
                                .toList();
                if (!overlaps.isEmpty()) {
                        for (Appointment overlap : overlaps) {
                                if (overlap.getRoom() != null && overlap.getRoom().getId().equals(req.getRoomId())) {
                                        throw new RuntimeException("La sala está ocupada en dicho rango de horario.");
                                }
                                if (overlap.getTherapist() != null && overlap.getTherapist().getId().equals(req.getTherapistId())) {
                                        throw new RuntimeException("El terapeuta está ocupado en dicho rango de horario.");
                                }
                        }
                        throw new RuntimeException("El horario, sala o terapeuta no están disponibles (Conflicto con otra cita).");
                }

                // Actualizar campos permitidos
                a.setTherapist(therapist);
                a.setRoom(room);
                a.setStartDateTime(start);
                a.setEndDateTime(end);
                a.setComments(comments);

                Appointment saved = appointmentRepo.save(a);

                String folio = saved.getPatient() != null ? saved.getPatient().getFolio() : null;
                return new AppointmentResponse("Cita actualizada correctamente", saved.getId(), folio,
                                saved.getAppointmentStatus() != null ? saved.getAppointmentStatus().toString() : AppointmentStatus.PENDIENTE.toString());
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
                                throw new IllegalArgumentException("Estado inválido. Valores permitidos: pendiente, completado, cancelado");
                        }
                        a.setAppointmentStatus(s);
                        Appointment saved = appointmentRepo.save(a);
                        String folio = saved.getPatient() != null ? saved.getPatient().getFolio() : null;
                        return new AppointmentResponse("Estado actualizado", saved.getId(), folio, saved.getAppointmentStatus().toString());
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
                                a.getAppointmentStatus() != null ? a.getAppointmentStatus().toString() : AppointmentStatus.PENDIENTE.toString());
        }
}
