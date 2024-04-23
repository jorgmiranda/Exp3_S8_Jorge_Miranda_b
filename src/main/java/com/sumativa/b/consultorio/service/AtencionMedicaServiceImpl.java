package com.sumativa.b.consultorio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumativa.b.consultorio.model.AtencionMedica;
import com.sumativa.b.consultorio.repository.AtencionMedicaRepository;



@Service
public class AtencionMedicaServiceImpl implements AtencionMedicaService{
    @Autowired
    private AtencionMedicaRepository atencionMedicaRepositoy;

    @Override
    public List<AtencionMedica> getAllAtencionMedica() {
        return atencionMedicaRepositoy.findAll();
    }

    @Override
    public Optional<AtencionMedica> getAtencionMedicaById(Long id) {
        return atencionMedicaRepositoy.findById(id);
    }

    @Override
    public AtencionMedica crearAtencionMedica(AtencionMedica atencionMedica) {
        return atencionMedicaRepositoy.save(atencionMedica);
    }

    @Override
    public AtencionMedica actualizarAtencionMedica(Long id, AtencionMedica atencionMedica) {
        if(atencionMedicaRepositoy.existsById(id)){
            atencionMedica.setId(id);
            return atencionMedicaRepositoy.save(atencionMedica);
        }else{
            return null;
        }
    }

    @Override
    public void eliminarAtencionMedica(Long id) {
        atencionMedicaRepositoy.deleteById(id);
    }

    
}
