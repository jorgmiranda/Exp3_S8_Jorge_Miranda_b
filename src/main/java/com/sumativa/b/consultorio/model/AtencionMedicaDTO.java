package com.sumativa.b.consultorio.model;

import java.util.Date;

public class AtencionMedicaDTO {

    private Date fecha;
    private String tipo; 
    private String descripcion;
    private Long idPaciente;
    
    public AtencionMedicaDTO() {
    }

    public AtencionMedicaDTO(Date fecha, String tipo, String descripcion, Long idPaciente) {
        this.fecha = fecha;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.idPaciente = idPaciente;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Long idPaciente) {
        this.idPaciente = idPaciente;
    }

    
}
