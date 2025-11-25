// package com.mipart.spring.agenda.sprinboot_citas.model.DTOs;

// import java.time.LocalDate;
// import java.time.LocalDateTime;

// import com.mipart.spring.agenda.sprinboot_citas.model.RoomMdel;

// import jakarta.validation.constraints.*;

// public class patientDTO {
//     private Long id;

//     @NotBlank(message = "Nombre es obligatorio")
//     private String firstname;

//     @NotBlank(message = "Apellido es obligatorio")
//     private String lastname;

//     @NotBlank(message = "debe tener fecha de nacimiento")
//     private LocalDate birthDate;

//     @NotBlank(message = "debe tener numero")
//     private int phone;

//     @NotBlank(message = "debe asignada uan habitacion")
//     private RoomMdel room;

//     @NotBlank(message = "debe tener fecha de creacion")
//     private LocalDateTime createdAt;

//     @NotBlank(message = "debe tener fecha de nacimiento")
//     private LocalDateTime upDateTime;

//     @Email(message = "Email inválido")
//     private String email;

//     public String getFirstname() {
//         return firstname;
//     }
//     public void setFirstname(String firstname) {
//         this.firstname = firstname;
//     }
//     public String getLastname() {
//         return lastname;
//     }
//     public void setLastname(String lastname) {
//         this.lastname = lastname;
//     }
//     public LocalDate getBirthDate() {
//         return birthDate;
//     }
//     public void setBirthDate(LocalDate birthDate) {
//         this.birthDate = birthDate;
//     }
//     public int getPhone() {
//         return phone;
//     }
//     public void setPhone(int phone) {
//         this.phone = phone;
//     }
//     public RoomMdel getRoom() {
//         return room;
//     }
//     public void setRoom(RoomMdel room) {
//         this.room = room;
//     }
//     public LocalDateTime getCreatedAt() {
//         return createdAt;
//     }
//     public void setCreatedAt(LocalDateTime createdAt) {
//         this.createdAt = createdAt;
//     }
//     public LocalDateTime getUpDateTime() {
//         return upDateTime;
//     }
//     public void setUpDateTime(LocalDateTime upDateTime) {
//         this.upDateTime = upDateTime;
//     }
//     private String telefono;

    
//     public Long getId() { return id; }
//     public void setId(Long id) { this.id = id; }

//     public String getNombre() { return firstname; }
//     public void setNombre(String firstname) { this.firstname = firstname; }

//     public String getApellido() { return lastname; }
//     public void setApellido(String lastname) { this.lastname = lastname; }

//     public String getEmail() { return email; }
//     public void setEmail(String email) { this.email = email; }

//     public String getTelefono() { return telefono; }
//     public void setTelefono(String telefono) { this.telefono = telefono; }
// }
