package com.sumativa.b.consultorio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sumativa.b.consultorio.model.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long>{
    
}
