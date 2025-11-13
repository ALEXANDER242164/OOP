package com.informaticonfing.spring.app.springboot.AppointmentService;

import com.informaticonfing.spring.app.springboot.dto.AppointmentRequest;
import com.informaticonfing.spring.app.springboot.dto.AppointmentResponse;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {

    public AppointmentResponse create(AppointmentRequest req) {
        // aquí luego metes toda la lógica que ya tenías
        return new AppointmentResponse(
                "Cita registrada correctamente (dummy)",
                1L,
                "PAC-DUMMY"
        );
    }
}
