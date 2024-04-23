package com.sumativa.b.consultorio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sumativa.b.consultorio.model.AtencionMedica;

public interface AtencionMedicaRepository extends JpaRepository<AtencionMedica, Long>{
    
}
