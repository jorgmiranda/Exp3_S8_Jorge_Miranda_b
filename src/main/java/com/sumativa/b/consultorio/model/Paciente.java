package com.sumativa.b.consultorio.model;

import java.util.Date;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity(name = "paciente")
public class Paciente extends RepresentationModel<Paciente>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paciente")
    private Long id;
    @Column(name = "rut_paciente")
    private String rut;
    @Column(name = "nombre_paciente")
    private String nombre;
    @Column(name = "correo_paciente")
    private String correo;
    @Column(name = "fecha_nacimiento_p")
    private Date fechaNacimeinto;
    @Column(name = "direccion_paciente")
    private String direccion;
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<AtencionMedica> atenciones;
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ConsultaMedica> consultas;
    
    public Paciente() {
    }

    public Paciente(Long id, String rut, String nombre, String correo, Date fechaNacimeinto, String direccion,
            List<AtencionMedica> atenciones, List<ConsultaMedica> consultas) {
        this.id = id;
        this.rut = rut;
        this.nombre = nombre;
        this.correo = correo;
        this.fechaNacimeinto = fechaNacimeinto;
        this.direccion = direccion;
        this.atenciones = atenciones;
        this.consultas = consultas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Date getFechaNacimeinto() {
        return fechaNacimeinto;
    }

    public void setFechaNacimeinto(Date fechaNacimeinto) {
        this.fechaNacimeinto = fechaNacimeinto;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<AtencionMedica> getAtenciones() {
        return atenciones;
    }

    public void setAtenciones(List<AtencionMedica> atenciones) {
        this.atenciones = atenciones;
    }

    public List<ConsultaMedica> getConsultas() {
        return consultas;
    }

    public void setConsultas(List<ConsultaMedica> consultas) {
        this.consultas = consultas;
    }

    
    


    
}
