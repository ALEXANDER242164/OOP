package com.mipart.spring.agenda.sprinboot_citas.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rooms")
public class RoomMdel extends BaseModel {

    private String name;
    private Integer capacity;
    private String location;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AppointmentModel> appointmentModels = new ArrayList<>();

    public RoomMdel() {}

    public RoomMdel(String name, Integer capacity, String location) {
        this.name = name;
        this.capacity = capacity;
        this.location = location;
    }

    // Getters / Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public List<AppointmentModel> getAppointmentModels() { return appointmentModels; }
    public void addAppointmentModel(AppointmentModel appointmentModel) {
        appointmentModels.add(appointmentModel);
        appointmentModel.setRoom(this);
    }
    public void removeAppointmentModel(AppointmentModel appointmentModel) {
        appointmentModels.remove(appointmentModel);
        appointmentModel.setRoom(null);
    }
}

