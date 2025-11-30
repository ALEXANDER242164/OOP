package com.informaticonfing.spring.app.springboot.controllers;

import com.informaticonfing.spring.app.springboot.model.Patient;
import com.informaticonfing.spring.app.springboot.model.Therapist;
import com.informaticonfing.spring.app.springboot.model.Room;
import com.informaticonfing.spring.app.springboot.repository.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Tag(name = "Catalogs", description = "Catálogos usados para agendar citas: pacientes, terapeutas, salas y tipos de sesión.")
@RestController
@RequestMapping("/api/catalogs")
@CrossOrigin("*")
public class CatalogController {

        private final PatientRepository patientRepo;
        private final TherapistRepository therapistRepo;
        private final RoomRepository roomRepo;

        public CatalogController(PatientRepository patientRepo,
                        TherapistRepository therapistRepo,
                        RoomRepository roomRepo) {
                this.patientRepo = patientRepo;
                this.therapistRepo = therapistRepo;
                this.roomRepo = roomRepo;
        }

        @Operation(summary = "Obtener tipos de sesión", description = "Devuelve el catálogo de tipos de sesión disponibles.")
        @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
        @GetMapping("/session-types")
        public List<String> getSessionTypes() {
                return Arrays.asList(
                                "CITA_DE_TERAPIA",
                                "EVALUACION_INICIAL"
                );
        }

        @Operation(summary = "Listar pacientes", description = "Devuelve todos los pacientes registrados en el sistema.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Listado de pacientes obtenido correctamente")
        })
        @GetMapping("/patients")
        public List<Patient> getPatients() {
                return patientRepo.findAll();
        }

        @Operation(summary = "Listar terapeutas", description = "Retorna el catálogo de terapeutas disponibles.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Listado de terapeutas obtenido correctamente")
        })
        @GetMapping("/therapists")
        public List<Therapist> getTherapists() {
                return therapistRepo.findAll();
        }

        @Operation(summary = "Listar salas", description = "Devuelve el catálogo de salas disponibles.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Listado de salas obtenido correctamente")
        })
        @GetMapping("/rooms")
        public List<Room> getRooms() {
                return roomRepo.findAll();
        }
}
