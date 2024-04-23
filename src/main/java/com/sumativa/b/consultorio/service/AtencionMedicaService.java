package com.sumativa.b.consultorio.service;

import java.util.List;
import java.util.Optional;

import com.sumativa.b.consultorio.model.AtencionMedica;

public interface AtencionMedicaService {
    List<AtencionMedica> getAllAtencionMedica();
    Optional<AtencionMedica> getAtencionMedicaById(Long id);
    AtencionMedica crearAtencionMedica(AtencionMedica atencionMedica);
    AtencionMedica actualizarAtencionMedica(Long id, AtencionMedica atencionMedica);
    void eliminarAtencionMedica(Long id);
}
