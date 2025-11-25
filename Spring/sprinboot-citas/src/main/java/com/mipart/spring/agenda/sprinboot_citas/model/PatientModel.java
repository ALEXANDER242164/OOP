package com.mipart.spring.agenda.sprinboot_citas.model;

//lo que tenemos es esta clase es el modelado, basicamente el modelo que estara mapeado en la base de datos.
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patients")
public class PatientModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String email;
    private String phone;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    private List<AppointmentModel> appointmentModels = new ArrayList<>();


    public PatientModel() {}

    // Getters / Setters
    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }
    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public List<AppointmentModel> getAppointmentModels() { return appointmentModels; }
    public void addAppointment(AppointmentModel appt) {
        appointmentModels.add(appt);
        appt.setPatient(this);
    }
    public void removeAppointment(AppointmentModel appt) {
        appointmentModels.remove(appt);
        appt.setPatient(null);
    }
}