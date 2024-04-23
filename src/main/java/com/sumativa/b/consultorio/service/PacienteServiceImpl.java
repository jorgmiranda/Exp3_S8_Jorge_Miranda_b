package com.sumativa.b.consultorio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumativa.b.consultorio.model.Paciente;
import com.sumativa.b.consultorio.repository.PacienteRepository;

@Service
public class PacienteServiceImpl implements PacienteService{
    @Autowired
    private PacienteRepository pacienteRepository;

    @Override
    public List<Paciente> getAllPaciente() {
        return pacienteRepository.findAll();
    }

    @Override
    public Optional<Paciente> getPacienteById(Long id) {
        return pacienteRepository.findById(id);
    }

    @Override
    public Paciente crearPaciente(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    @Override
    public Paciente actualizarPaciente(Long id, Paciente paciente) {
        if(pacienteRepository.existsById(id)){
            paciente.setId(id);
            return pacienteRepository.save(paciente);
        }else{
            return null;
        }
    }

    @Override
    public void eliminarPaciente(Long id) {
        pacienteRepository.deleteById(id);
    }
    
}
