package com.informaticonfing.spring.app.springboot.config;

import com.informaticonfing.spring.app.springboot.model.Therapist;
import com.informaticonfing.spring.app.springboot.repository.TherapistRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final TherapistRepository therapistRepo;

    public DataLoader(TherapistRepository therapistRepo) {
        this.therapistRepo = therapistRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        // Si no hay terapeutas, creamos algunos de prueba
        if (therapistRepo.count() == 0) {
            therapistRepo.save(new Therapist("Dr. Juan Pérez"));
            therapistRepo.save(new Therapist("Lic. Ana Gómez"));
            therapistRepo.save(new Therapist("Dra. Sofia Martinez"));
            System.out.println("✅ Terapeutas de prueba cargados en la base de datos.");
        }
    }
}
