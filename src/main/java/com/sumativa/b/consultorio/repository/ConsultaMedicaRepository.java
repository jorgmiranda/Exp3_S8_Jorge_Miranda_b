package com.sumativa.b.consultorio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sumativa.b.consultorio.model.ConsultaMedica;

public interface ConsultaMedicaRepository extends JpaRepository<ConsultaMedica, Long>{
    
}
