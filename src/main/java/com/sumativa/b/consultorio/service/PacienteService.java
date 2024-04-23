package com.sumativa.b.consultorio.service;

import java.util.List;
import java.util.Optional;

import com.sumativa.b.consultorio.model.Paciente;

public interface PacienteService {
    List<Paciente> getAllPaciente();
    Optional<Paciente> getPacienteById(Long id);
    Paciente crearPaciente(Paciente paciente);
    Paciente actualizarPaciente(Long id, Paciente paciente);
    void eliminarPaciente(Long id);
}
