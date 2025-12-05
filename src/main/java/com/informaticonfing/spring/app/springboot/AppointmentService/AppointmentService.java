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

                // Validar Comentarios
                String comments = req.getComments();
                if (comments != null) {
                        comments = comments.trim();
                        if (comments.length() > 500) {
                                throw new RuntimeException("Los comentarios no pueden exceder 500 caracteres.");
                        }
                }

                Patient patient;
                if (req.getPatientId() != null) {
                        patient = patientRepo.findById(req.getPatientId())
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Paciente no encontrado."));

                        // Validar que el paciente no tenga otra cita el mismo día
                        LocalDateTime startOfDay = req.getDate().atStartOfDay();
                        LocalDateTime endOfDay = req.getDate().atTime(LocalTime.MAX);
                        long existingAppts = appointmentRepo.countAppointmentsByPatientAndDate(
                                        patient.getId(), startOfDay, endOfDay);
                        if (existingAppts > 0) {
                                throw new RuntimeException(
                                                "El paciente ya tiene una cita programada para este día. No se permite más de una cita diaria.");
                        }

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
                                                "Terapeuta no encontrado."));
                Room room = roomRepo.findById(req.getRoomId())
                                .orElseThrow(() -> new RuntimeException(
                                                "Sala no encontrada."));

                LocalDateTime start = LocalDateTime.of(req.getDate(), req.getStartTime());
                LocalDateTime end = start.plusMinutes(req.getDurationMinutes() != null ? req.getDurationMinutes() : 60);

                // 4. Validar Máximo de Citas Globales (Regla de Negocio: Máx 2 simultáneas)
                long activeAppointmentsCount = appointmentRepo.countActiveAppointmentsInTimeRange(start, end);
                if (activeAppointmentsCount >= 2) {
                        throw new RuntimeException(
                                        "Lo sentimos, ya se ha alcanzado el límite máximo de citas (2) para este horario.");
                }

                // 5. Validar Solapamiento Específico (Sala o Terapeuta)
                List<Appointment> overlaps = appointmentRepo.findOverlappingAppointments(
                                start, end, req.getRoomId(), req.getTherapistId());

                if (!overlaps.isEmpty()) {
                        // Check specific conflict
                        for (Appointment overlap : overlaps) {
                                if (overlap.getRoom().getId().equals(req.getRoomId())) {
                                        throw new RuntimeException("La sala está ocupada en dicho rango de horario.");
                                }
                                if (overlap.getTherapist().getId().equals(req.getTherapistId())) {
                                        throw new RuntimeException(
                                                        "El terapeuta está ocupado en dicho rango de horario.");
                                }
                        }
                        // Fallback
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
                a.setPaymentProofPath(req.getPaymentProof());
                a.setComments(comments);
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
                                a.getRoom() != null ? a.getRoom().getId() : null,
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
                                .orElseThrow(() -> new RuntimeException("Cita no encontrada."));
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

        @Transactional
        public AppointmentResponse reschedule(Long appointmentId, com.informaticonfing.spring.app.springboot.dto.RescheduleRequest req) {
                Appointment a = appointmentRepo.findById(appointmentId)
                                .orElseThrow(() -> new RuntimeException("Cita no encontrada."));

                // Validaciones similares a create (no fines de semana)
                java.time.DayOfWeek day = req.getDate().getDayOfWeek();
                if (day == java.time.DayOfWeek.SATURDAY || day == java.time.DayOfWeek.SUNDAY) {
                        throw new RuntimeException("No se pueden reprogramar citas a fines de semana.");
                }

                // Validar inicio en punto de hora (mantener consistencia)
                if (req.getStartTime().getMinute() != 0) {
                        throw new RuntimeException("Las citas deben iniciar en punto de la hora (ej. 09:00, 10:00).");
                }

                if (req.getStartTime().getHour() < 9 || req.getStartTime().getHour() > 16) {
                        throw new RuntimeException("El horario de atención es de 09:00 a 17:30. Última cita a las 16:00.");
                }


                Therapist therapist = therapistRepo.findById(req.getTherapistId())
                                .orElseThrow(() -> new RuntimeException("Terapeuta no encontrado."));

                Room newRoom = roomRepo.findById(req.getRoomId() != null ? req.getRoomId() : a.getRoom().getId())
                                .orElseThrow(() -> new RuntimeException("Sala no encontrada."));

                LocalDateTime start = LocalDateTime.of(req.getDate(), req.getStartTime());
                LocalDateTime end = start.plusMinutes(req.getDurationMinutes() != null ? req.getDurationMinutes() : 60);

                // Revisar conflictos (ignorando la propia cita) usando la sala nueva
                Long roomIdForCheck = newRoom.getId();
                List<Appointment> overlaps = appointmentRepo.findOverlappingAppointments(start, end, roomIdForCheck, req.getTherapistId())
                                .stream()
                                .filter(o -> !o.getId().equals(appointmentId))
                                .toList();

                if (!overlaps.isEmpty()) {
                        for (Appointment overlap : overlaps) {
                                if (overlap.getRoom().getId().equals(a.getRoom().getId())) {
                                        throw new RuntimeException("La sala está ocupada en dicho rango de horario.");
                                }
                                if (overlap.getTherapist().getId().equals(req.getTherapistId())) {
                                        throw new RuntimeException("El terapeuta está ocupado en dicho rango de horario.");
                                }
                        }
                        throw new RuntimeException("El horario, sala o terapeuta no están disponibles (Conflicto con otra cita).");
                }

                // Aplicar cambios (actualizar la entidad existente)
                a.setTherapist(therapist);
                a.setRoom(newRoom);
                a.setStartDateTime(start);
                a.setEndDateTime(end);
                a.setComments(req.getComments());

                Appointment saved = appointmentRepo.save(a);
                String folio = saved.getPatient() != null ? saved.getPatient().getFolio() : null;
                return new AppointmentResponse("Cita reprogramada", saved.getId(), folio,
                                saved.getAppointmentStatus() != null ? saved.getAppointmentStatus().toString()
                                                : AppointmentStatus.PENDIENTE.toString());
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
