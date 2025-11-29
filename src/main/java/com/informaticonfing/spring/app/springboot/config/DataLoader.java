package com.informaticonfing.spring.app.springboot.config;

import com.informaticonfing.spring.app.springboot.model.Patient;
import com.informaticonfing.spring.app.springboot.model.Room;
import com.informaticonfing.spring.app.springboot.model.Therapist;
import com.informaticonfing.spring.app.springboot.repository.PatientRepository;
import com.informaticonfing.spring.app.springboot.repository.RoomRepository;
import com.informaticonfing.spring.app.springboot.repository.TherapistRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Random;
import com.informaticonfing.spring.app.springboot.model.Appointment;
import com.informaticonfing.spring.app.springboot.model.AppointmentStatus;
import com.informaticonfing.spring.app.springboot.repository.AppointmentRepository;

@Component
public class DataLoader implements CommandLineRunner {

    private final TherapistRepository therapistRepo;
    private final PatientRepository patientRepo;
    private final RoomRepository roomRepo;
    private final AppointmentRepository appointmentRepo;

    public DataLoader(TherapistRepository therapistRepo, PatientRepository patientRepo, RoomRepository roomRepo, AppointmentRepository appointmentRepo) {
        this.therapistRepo = therapistRepo;
        this.patientRepo = patientRepo;
        this.roomRepo = roomRepo;
        this.appointmentRepo = appointmentRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        // Cargar Terapeutas
        if (therapistRepo.count() == 0) {
            therapistRepo.save(new Therapist("Dr. Juan Pérez"));
            therapistRepo.save(new Therapist("Lic. Ana Gómez"));
            therapistRepo.save(new Therapist("Dra. Sofia Martinez"));
            System.out.println("✅ Terapeutas de prueba cargados.");
        }

        // Cargar Pacientes
        if (patientRepo.count() == 0) {
            Patient p = new Patient();
            p.setFirstName("Paciente");
            p.setLastName("Prueba");
            p.setEmail("paciente@test.com");
            p.setPhone("555-0000");
            patientRepo.save(p);
            System.out.println("✅ Paciente de prueba cargado (ID 1).");
        }

        // Cargar Salas
        if (roomRepo.count() == 0) {
            Room r = new Room();
            r.setNombre("Consultorio 1");
            roomRepo.save(r);
            System.out.println("✅ Sala de prueba cargada (ID 1).");
        }

        // Rellenar folios faltantes para pacientes existentes (6 dígitos aleatorios, únicos)
        List<Patient> patients = patientRepo.findAll();
        Random rnd = new Random();
        for (Patient p : patients) {
            if (p.getFolio() == null || p.getFolio().isBlank()) {
                String folio;
                int attempts = 0;
                do {
                    folio = String.format("%06d", rnd.nextInt(1_000_000));
                    attempts++;
                    if (attempts > 200) {
                        // Fallback: use padded ID
                        folio = String.format("%06d", p.getId());
                        break;
                    }
                } while (patientRepo.existsByFolio(folio));
                p.setFolio(folio);
                patientRepo.save(p);
                System.out.println("✅ Folio generado para paciente ID " + p.getId() + ": " + folio);
            }
        }

        // Rellenar estado de citas existentes a 'pendiente' si no tienen
        List<Appointment> appointments = appointmentRepo.findAll();
        for (Appointment ap : appointments) {
            if (ap.getAppointmentStatus() == null) {
                ap.setAppointmentStatus(AppointmentStatus.PENDIENTE);
                appointmentRepo.save(ap);
                System.out.println("✅ Estado asignado 'pendiente' a cita ID " + ap.getId());
            }
        }
    }
}
