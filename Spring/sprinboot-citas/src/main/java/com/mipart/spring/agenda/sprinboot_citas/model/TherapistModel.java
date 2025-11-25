// package com.mipart.spring.agenda.sprinboot_citas.model;

// import jakarta.persistence.*;
// import java.util.ArrayList;
// import java.util.List;

// @Entity
// @Table(name = "therapistModels")
// public class TherapistModel extends BaseModel {

//     private String name;
//     private String email;
//     private String phone;

//     @ElementCollection
//     @CollectionTable(name = "therapistModel_specialties", joinColumns = @JoinColumn(name = "therapistModel_id"))
//     @Column(name = "specialty")
//     private List<String> specialties = new ArrayList<>();

//     @OneToMany(mappedBy = "therapistModel", cascade = CascadeType.ALL, orphanRemoval = true)
//     private List<AppointmentModel> appointmentModels = new ArrayList<>();

//     public TherapistModel() {}

//     public TherapistModel(String name, String email, String phone) {
//         this.name = name;
//         this.email = email;
//         this.phone = phone;
//     }

//     // Getters / Setters
//     public String getName() { return name; }
//     public void setName(String name) { this.name = name; }

//     public String getEmail() { return email; }
//     public void setEmail(String email) { this.email = email; }

//     public String getPhone() { return phone; }
//     public void setPhone(String phone) { this.phone = phone; }

//     public List<String> getSpecialties() { return specialties; }
//     public void setSpecialties(List<String> specialties) { this.specialties = specialties; }

//     public List<AppointmentModel> getAppointmentModels() { return appointmentModels; }
//     public void addAppointmentModel(AppointmentModel appointmentModel) {
//         appointmentModels.add(appointmentModel);
//         appointmentModel.setTherapist(this);
//     }
//     public void removeAppointmentModel(AppointmentModel appointmentModel) {
//         appointmentModels.remove(appointmentModel);
//         appointmentModel.setTherapist(null);
//     }
// }