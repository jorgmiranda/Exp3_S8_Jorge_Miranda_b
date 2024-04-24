package com.sumativa.b.consultorio.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sumativa.b.consultorio.model.Paciente;
import com.sumativa.b.consultorio.repository.PacienteRepository;

@ExtendWith(MockitoExtension.class)
public class PacienteServiceTest {
    @InjectMocks
    private PacienteServiceImpl pacienteServiceImpl;

    @Mock
    private PacienteRepository pacienteRepositoryMock;

    @SuppressWarnings("deprecation")
    @Test
    public void crearPacienteTest(){
        //Arrange
        Paciente paciente = new Paciente();
        paciente.setRut("9999999-9");
        paciente.setNombre("Miguelito Manolo");
        paciente.setCorreo("miguelito.manolo@prueba.cl");
        paciente.setDireccion("Calle prueba 123, el examen");
        paciente.setFechaNacimeinto(new Date(1997,3,12));

        when(pacienteRepositoryMock.save(any())).thenReturn(paciente);

        //ACT
        Paciente resultado = pacienteServiceImpl.crearPaciente(paciente);

        //Assert
        assertEquals("9999999-9", resultado.getRut());
        assertEquals("Miguelito Manolo", resultado.getNombre());
        assertEquals("miguelito.manolo@prueba.cl", resultado.getCorreo());
        assertEquals("Calle prueba 123, el examen", resultado.getDireccion());
        assertEquals(new Date(1997,3,12), resultado.getFechaNacimeinto());
    }
    
}
