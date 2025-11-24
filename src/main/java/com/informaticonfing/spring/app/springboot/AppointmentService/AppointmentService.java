package com.informaticonfing.spring.app.springboot.AppointmentService;

import com.informaticonfing.spring.app.springboot.dto.AppointmentRequest;
import com.informaticonfing.spring.app.springboot.dto.AppointmentResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AppointmentService {

    // Lista en memoria para guardar las citas mientras la app está encendida
    private final List<AppointmentResponse> appointments = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public AppointmentResponse create(AppointmentRequest req) {
        // Convertimos el Request a Response (simulando guardado)
        AppointmentResponse newAppointment = new AppointmentResponse(
                "Cita creada para el paciente ID: " + req.getPatientId(),
                idCounter.getAndIncrement(),
                "Paciente ID: " + req.getPatientId() // Simulamos el nombre usando el ID
        );

        // Guardamos en la lista en memoria
        appointments.add(newAppointment);

        return newAppointment;
    }

    public List<AppointmentResponse> findAll() {
        // Devolvemos la lista real que tiene las citas guardadas
        return appointments;
    }
}
