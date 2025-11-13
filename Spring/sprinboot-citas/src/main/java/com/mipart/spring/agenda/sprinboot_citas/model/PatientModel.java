package com.mipart.spring.agenda.sprinboot_citas.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patients") // aqui lo que tenemos especificado es que en la base datos la tabla con el nombre patient va tener los 
//siguieintes atributos o culmnas para guardadr la siguiente informacion o tatributo de los patient 
public class PatientModel extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String email;
    private int phone;
    private RoomMdel room; // esta es el atributo de tipo Roommodel el cual es basicamente la clase RoomMdel
    private LocalDateTime createdAt; //se le ha agregado createdAt para que sea un atributo el cual sea la fecha y hora en la cual se creo el paciente  
    private LocalDateTime upDateTime; //parecido con este es la fecha y hora en la que se actualizo por ultima vez el paciente

    @OneToMany(mappedBy = "patientModel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AppointmentModel> appointmentModels = new ArrayList<>();

    public PatientModel() {}

    public PatientModel(String firstName, String lastName, LocalDate birthDate, String email, int phone, RoomMdel room, LocalDateTime creatdAt,LocalDateTime upDateTime, Long id ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.email = email;
        this.phone = phone;
        this.room = room; 
        this.createdAt = creatdAt;
        this.upDateTime = upDateTime;
    }

    // Getters / Setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getPhone() { return phone; }
    public void setPhone(int phone) { this.phone = phone; }

    public RoomMdel getRoom() {return room;}
    public void setRoom(RoomMdel room) {this.room = room;}

    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}

    public LocalDateTime getUpDateTime() { return upDateTime;}
    public void setUpDateTime(LocalDateTime upDateTime) {this.upDateTime = upDateTime;}

    public List<AppointmentModel> getAppointmentModels() { return appointmentModels; }
    public void addAppointmentModel(AppointmentModel appointmentModel) {
        appointmentModels.add(appointmentModel);
        appointmentModel.setPatient(this);
    }
    public void removeAppointmentModel(AppointmentModel appointmentModel) {
        appointmentModels.remove(appointmentModel);
        appointmentModel.setPatient(null);
    }
}