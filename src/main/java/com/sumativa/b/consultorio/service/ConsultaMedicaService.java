package com.sumativa.b.consultorio.service;

import java.util.List;
import java.util.Optional;

import com.sumativa.b.consultorio.model.ConsultaMedica;

public interface ConsultaMedicaService {
    List<ConsultaMedica> getAllConsultaMedica();
    Optional<ConsultaMedica> getConsultaMedicaById(Long id);
    ConsultaMedica crearConsultaMedica(ConsultaMedica consultaMedica);
    ConsultaMedica actualizarConsultaMedica(Long id, ConsultaMedica consultaMedica);
    void eliminarConsultaMedica(Long id);
}
