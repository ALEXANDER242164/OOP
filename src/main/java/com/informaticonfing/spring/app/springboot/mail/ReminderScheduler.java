package com.informaticonfing.spring.app.springboot.mail;

import com.informaticonfing.spring.app.springboot.model.Appointment;
import com.informaticonfing.spring.app.springboot.repository.AppointmentRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ReminderScheduler {

    private final AppointmentRepository appointmentRepository;
    private final EmailService emailService;

    public ReminderScheduler(AppointmentRepository appointmentRepository, EmailService emailService) {
        this.appointmentRepository = appointmentRepository;
        this.emailService = emailService;
    }

    // Ejecuta cada 5 minutos y busca citas que empiezan dentro de la ventana de 12 horas +/- 5 minutos
    @Scheduled(fixedRate = 300000)
    public void sendReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startWindow = now.plusHours(12);
        LocalDateTime endWindow = startWindow.plusMinutes(5);

        List<Appointment> list = appointmentRepository.findAppointmentsForReminder(startWindow, endWindow);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (Appointment a : list) {
            if (a.getPatient() == null || a.getPatient().getEmail() == null || a.getPatient().getEmail().isBlank()) {
                continue; // no hay correo
            }

            String to = a.getPatient().getEmail();
            String subject = "Recordatorio de cita - Clínica";
            String body = String.format("Hola %s %s,\n\nLe recordamos que tiene una cita programada el %s (inicio: %s).\n\nTerapeuta: %s\nSala: %s\n\nSi necesita reprogramar o cancelar, por favor contacte a la clínica.\n\nSaludos,\nClínica",
                    a.getPatient().getFirstName() != null ? a.getPatient().getFirstName() : "",
                    a.getPatient().getLastName() != null ? a.getPatient().getLastName() : "",
                    a.getStartDateTime().toLocalDate().toString(),
                    a.getStartDateTime().format(fmt),
                    a.getTherapist() != null ? a.getTherapist().getName() : "-",
                    a.getRoom() != null ? a.getRoom().getNombre() : "-");

            try {
                emailService.sendSimpleMessage(to, subject, body);
                a.setReminderSent(true);
                appointmentRepository.save(a);
            } catch (Exception e) {
                // En caso de fallo en envío, se ignora para reintento futuro
                // (no marcamos como enviado)
                System.err.println("Error enviando recordatorio a " + to + ": " + e.getMessage());
            }
        }
    }
}
