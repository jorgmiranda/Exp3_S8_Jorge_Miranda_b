package com.sumativa.b.consultorio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumativa.b.consultorio.model.ConsultaMedica;
import com.sumativa.b.consultorio.repository.ConsultaMedicaRepository;

@Service
public class ConsultaMedicaServiceImpl implements ConsultaMedicaService{
    @Autowired
    private ConsultaMedicaRepository consultaMedicaRepository;

    @Override
    public List<ConsultaMedica> getAllConsultaMedica() {
        return consultaMedicaRepository.findAll();
    }

    @Override
    public Optional<ConsultaMedica> getConsultaMedicaById(Long id) {
        return consultaMedicaRepository.findById(id);
    }

    @Override
    public ConsultaMedica crearConsultaMedica(ConsultaMedica consultaMedica) {
        return consultaMedicaRepository.save(consultaMedica);
    }

    @Override
    public ConsultaMedica actualizarConsultaMedica(Long id, ConsultaMedica consultaMedica) {
        if(consultaMedicaRepository.existsById(id)){
            consultaMedica.setId(id);
            return consultaMedicaRepository.save(consultaMedica);
        }else{
            return null;
        }
    }

    @Override
    public void eliminarConsultaMedica(Long id) {
        consultaMedicaRepository.deleteById(id);
    }

    
}
