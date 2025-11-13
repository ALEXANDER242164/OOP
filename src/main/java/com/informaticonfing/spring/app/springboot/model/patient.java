package com.informaticonfing.spring.app.springboot.model;
import jakarta.persistence.*;

@Entity
public class patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(unique = true)
    private String folio;

    public patient() {}

    public patient(String nombre) {
        this.nombre = nombre;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getFolio() { return folio; }
    public void setFolio(String folio) { this.folio = folio; }
}
