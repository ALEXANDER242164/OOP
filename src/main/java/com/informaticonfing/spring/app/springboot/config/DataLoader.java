package com.informaticonfing.spring.app.springboot.config;

import com.informaticonfing.spring.app.springboot.model.Patient;
import com.informaticonfing.spring.app.springboot.model.Room;
import com.informaticonfing.spring.app.springboot.model.Therapist;
import com.informaticonfing.spring.app.springboot.repository.PatientRepository;
import com.informaticonfing.spring.app.springboot.repository.RoomRepository;
import com.informaticonfing.spring.app.springboot.repository.TherapistRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final TherapistRepository therapistRepo;
    private final PatientRepository patientRepo;
    private final RoomRepository roomRepo;

    public DataLoader(TherapistRepository therapistRepo, PatientRepository patientRepo, RoomRepository roomRepo) {
        this.therapistRepo = therapistRepo;
        this.patientRepo = patientRepo;
        this.roomRepo = roomRepo;
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
    }
}
