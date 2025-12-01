package com.informaticonfing.spring.app.springboot.AppointmentService;

import com.informaticonfing.spring.app.springboot.model.Appointment;
import com.informaticonfing.spring.app.springboot.repository.AppointmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@ConditionalOnBean(JavaMailSender.class)
public class ReminderService {

    private final Logger log = LoggerFactory.getLogger(ReminderService.class);

    private final AppointmentRepository appointmentRepo;
    private final JavaMailSender mailSender;

    public ReminderService(AppointmentRepository appointmentRepo, JavaMailSender mailSender) {
        this.appointmentRepo = appointmentRepo;
        this.mailSender = mailSender;
    }

    // Ejecuta cada 30 minutos para no perder ventanas
    @Scheduled(fixedRateString = "PT30M")
    public void sendReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime windowStart = now.plusHours(12).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime windowEnd = windowStart.plusHours(1);

        log.info("Buscando citas entre {} y {} para recordar", windowStart, windowEnd);

        List<Appointment> list = appointmentRepo.findAppointmentsForReminder(windowStart, windowEnd);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (Appointment a : list) {
            try {
                if (a.getPatient() == null || a.getPatient().getEmail() == null || a.getPatient().getEmail().isBlank()) {
                    log.warn("Cita {} no tiene email de paciente, se omite.", a.getId());
                    continue;
                }

                String to = a.getPatient().getEmail();
                String subject = "Recordatorio: próxima cita";
                String when = a.getStartDateTime().format(dtf);
                String therapist = a.getTherapist() != null ? a.getTherapist().getName() : "(sin terapeuta)";
                String room = a.getRoom() != null ? a.getRoom().getNombre() : "(sin sala)";

                String text = String.format("Estimado/a %s,\n\nEste es un recordatorio de su cita programada para el %s con %s en la sala %s.\n\nSi necesita reprogramar, por favor contacte a la clínica.\n\nSaludos,\nClínicaPsico",
                        a.getPatient().getFirstName() + " " + a.getPatient().getLastName(), when, therapist, room);

                SimpleMailMessage msg = new SimpleMailMessage();
                msg.setTo(to);
                msg.setSubject(subject);
                msg.setText(text);

                mailSender.send(msg);
                a.setReminderSent(true);
                appointmentRepo.save(a);
                log.info("Recordatorio enviado para cita {} a {}", a.getId(), to);
            } catch (MailException mex) {
                log.error("Error enviando recordatorio para cita {}: {}", a.getId(), mex.getMessage());
                // No marcar como enviado para reintentos
            } catch (Exception ex) {
                log.error("Error procesando recordatorio para cita {}: {}", a.getId(), ex.getMessage());
            }
        }
    }

    // Método público para enviar recordatorio de forma inmediata para una cita específica
    public String sendReminderForAppointment(Long appointmentId) {
        Appointment a = appointmentRepo.findById(appointmentId).orElse(null);
        if (a == null) return "Cita no encontrada";
        if (a.getPatient() == null || a.getPatient().getEmail() == null || a.getPatient().getEmail().isBlank()) {
            return "Paciente no tiene correo registrado";
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        try {
            String to = a.getPatient().getEmail();
            String subject = "Recordatorio: próxima cita";
            String when = a.getStartDateTime().format(dtf);
            String therapist = a.getTherapist() != null ? a.getTherapist().getName() : "(sin terapeuta)";
            String room = a.getRoom() != null ? a.getRoom().getNombre() : "(sin sala)";

            String text = String.format("Estimado/a %s,\n\nEste es un recordatorio de su cita programada para el %s con %s en la sala %s.\n\nSi necesita reprogramar, por favor contacte a la clínica.\n\nSaludos,\nClínicaPsico",
                    a.getPatient().getFirstName() + " " + a.getPatient().getLastName(), when, therapist, room);

            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText(text);

            mailSender.send(msg);
            a.setReminderSent(true);
            appointmentRepo.save(a);
            log.info("Recordatorio enviado para cita {} a {}", a.getId(), to);
            return "OK";
        } catch (Exception e) {
            log.error("Error enviando recordatorio inmediato: {}", e.getMessage());
            return "ERROR: " + e.getMessage();
        }
    }

    // Método para enviar un correo de prueba a una dirección
    public String sendTestEmail(String to, String body) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setSubject("[Prueba] Recordatorio desde ClínicaPsico");
            msg.setText(body != null ? body : "Este es un correo de prueba desde la aplicación.");
            mailSender.send(msg);
            return "OK";
        } catch (Exception e) {
            log.error("Error enviando correo de prueba: {}", e.getMessage());
            return "ERROR: " + e.getMessage();
        }
    }
}
