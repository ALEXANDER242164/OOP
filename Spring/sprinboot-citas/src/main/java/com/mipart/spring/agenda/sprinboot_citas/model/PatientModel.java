package com.mipart.spring.agenda.sprinboot_citas.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patients")
public class PatientModel extends BaseModel {

    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String email;
    private String phone;

    @OneToMany(mappedBy = "patientModel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AppointmentModel> appointmentModels = new ArrayList<>();

    public PatientModel() {}

    public PatientModel(String firstName, String lastName, LocalDate birthDate, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.email = email;
        this.phone = phone;
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

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

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