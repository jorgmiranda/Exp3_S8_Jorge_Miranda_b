package com.sumativa.b.consultorio.model;

import java.util.Date;

public class ConsultaMedicaDTO {
    private Date fecha;
    private String motivoConsulta;
    private String diagnostico;
    private String tratamiento;
    private Long idPaciente;
    
    public ConsultaMedicaDTO() {
    }

    public ConsultaMedicaDTO(Date fecha, String motivoConsulta, String diagnostico, String tratamiento,
            Long idPaciente) {
        this.fecha = fecha;
        this.motivoConsulta = motivoConsulta;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
        this.idPaciente = idPaciente;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public Long getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Long idPaciente) {
        this.idPaciente = idPaciente;
    }

    
}
