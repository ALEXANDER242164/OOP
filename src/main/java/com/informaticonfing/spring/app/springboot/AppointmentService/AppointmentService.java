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
import java.time.Period;
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

        // =============== 0. Validar tipo de sesión ===============
        if (req.getSessionType() == null) {
            throw new RuntimeException("El tipo de sesión (sessionType) es obligatorio.");
        }

        // =============== 1. Resolver paciente ===============
        Patient patient;

        if (req.getPatientId() != null) {
            // Paciente EXISTENTE
            patient = patientRepo.findById(req.getPatientId())
                    .orElseThrow(() -> new RuntimeException(
                            "Paciente no encontrado con ID: " + req.getPatientId()));
        } else {
            // Paciente NUEVO (validaciones nuevas + lógica original de folio)

            // 1.1 Validar nombre y apellido
            if (req.getPatientNombre() == null || req.getPatientNombre().trim().isEmpty()
                    || req.getPatientApellido() == null || req.getPatientApellido().trim().isEmpty()) {
                throw new RuntimeException("Nombre y apellido son requeridos para un paciente nuevo.");
            }

            // 1.2 Validar fecha de nacimiento y rango de edad
            if (req.getPatientBirthDate() == null) {
                throw new RuntimeException("La fecha de nacimiento es requerida para un paciente nuevo.");
            }

            LocalDate hoy = LocalDate.now();
            LocalDate nacimiento = req.getPatientBirthDate();
            int edad = Period.between(nacimiento, hoy).getYears();
            if (edad < 0 || edad > 100) {
                throw new RuntimeException("La edad del paciente debe estar entre 0 y 100 años.");
            }

            // 1.3 Validar que haya al menos teléfono o correo
            String telefono = req.getPatientTelefono();
            String email = req.getPatientEmail();
            if ((telefono == null || telefono.trim().isEmpty())
                    && (email == null || email.trim().isEmpty())) {
                throw new RuntimeException("Debe proporcionar al menos teléfono o correo electrónico para el paciente.");
            }

            // 1.4 LÓGICA ORIGINAL DE CREACIÓN DE PACIENTE + FOLIO
            patient = new Patient();
            patient.setFirstName(req.getPatientNombre().trim());
            patient.setLastName(req.getPatientApellido().trim());
            patient.setBirthDate(nacimiento);
            patient.setPhone(telefono != null ? telefono.trim() : null);
            patient.setEmail(email != null ? email.trim() : null);

            // Guardar primero para obtener ID (igual que antes)
            patient = patientRepo.save(patient);

            // Generar folio aleatorio único de 6 dígitos (igual que tu método original)
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

        // =============== 2. Terapeuta y sala (igual que antes) ===============

        Therapist therapist = therapistRepo.findById(req.getTherapistId())
                .orElseThrow(() -> new RuntimeException(
                        "Terapeuta no encontrado con ID: " + req.getTherapistId()));

        Room room = roomRepo.findById(req.getRoomId())
                .orElseThrow(() -> new RuntimeException(
                        "Sala no encontrada con ID: " + req.getRoomId()));

        // =============== 3. Fecha, hora y validaciones de negocio ===============

        if (req.getDate() == null || req.getStartTime() == null) {
            throw new RuntimeException("La fecha y la hora de inicio son obligatorias.");
        }

        LocalDateTime start = LocalDateTime.of(req.getDate(), req.getStartTime());
        int duration = (req.getDurationMinutes() != null) ? req.getDurationMinutes() : 60;
        LocalDateTime end = start.plusMinutes(duration);

        // Aquí metes tus reglas: lunes–viernes, 09:00–17:30, máximo 6 meses, etc.
        validateDateAndTime(req.getDate(), req.getStartTime());

        // =============== 4. Comentarios (obligatorios siempre) ===============

        String comments = req.getComments();
        if (comments == null || comments.trim().isEmpty()) {
            throw new RuntimeException("Los comentarios son obligatorios para la cita.");
        }
        comments = comments.trim();
        if (comments.length() > 500) {
            throw new RuntimeException("Los comentarios no pueden exceder 500 caracteres.");
        }

        // =============== 5. Construir la cita (basado en tu código original) ===============

        Appointment a = new Appointment();
        a.setPatient(patient);
        a.setTherapist(therapist);
        a.setRoom(room);
        a.setSessionType(req.getSessionType());
        a.setStartDateTime(start);
        a.setEndDateTime(end);
        a.setComments(comments);
        // Estado por defecto
        a.setAppointmentStatus(AppointmentStatus.PENDIENTE);

        // =============== 6. Pago y comprobante según tipo de sesión ===============

        if (req.getSessionType() == SessionType.CITA_DE_TERAPIA) {
            // Solo la cita de terapia tiene pago
            a.setAmountMx(10.0);

            String proof = req.getPaymentProofPath();
            if (proof == null || proof.trim().isEmpty()) {
                throw new RuntimeException("El comprobante de pago es obligatorio para una cita de terapia.");
            }
            a.setPaymentProofPath(proof.trim());
        } else {
            // Evaluación inicial (u otros tipos sin pago)
            a.setAmountMx(null);
            a.setPaymentProofPath(null);
        }

        // Guardar cita
        Appointment saved = appointmentRepo.save(a);

        // =============== 7. Asegurar que el paciente tenga folio (como en tu original) ===============

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

        // =============== 8. Respuesta (igual que tu primer método) ===============

        return new AppointmentResponse(
                "Cita creada correctamente",
                saved.getId(),
                patient.getFolio(),
                saved.getAppointmentStatus() != null
                        ? saved.getAppointmentStatus().toString()
                        : AppointmentStatus.PENDIENTE.toString()
        );
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
                                throw new RuntimeException("Estado inválido. Valores permitidos: pendiente, completado, cancelado");
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
    private void validateDateAndTime(LocalDate date, LocalTime time) {

        LocalDate hoy = LocalDate.now();

        if (date.isBefore(hoy)) {
            throw new RuntimeException("La fecha de la cita no puede ser en el pasado.");
        }

        LocalDate maxFecha = hoy.plusMonths(6);
        if (date.isAfter(maxFecha)) {
            throw new RuntimeException("La cita no puede programarse con más de 6 meses de anticipación.");
        }

        DayOfWeek dia = date.getDayOfWeek();  // <= AQUÍ SE USA
        if (dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY) {
            throw new RuntimeException("Solo se permiten citas de lunes a viernes.");
        }

        LocalTime inicioPermitido = LocalTime.of(9, 0);
        LocalTime finPermitido = LocalTime.of(17, 30);

        if (time.isBefore(inicioPermitido) || time.isAfter(finPermitido)) {
            throw new RuntimeException("La hora de la cita debe estar entre 09:00 y 17:30.");
        }
    }

}
