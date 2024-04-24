package com.sumativa.b.consultorio.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.sumativa.b.consultorio.model.AtencionMedica;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AtencionRepositoryTest {
    @Autowired
    private AtencionMedicaRepository atencionMedicaRepository;

    @SuppressWarnings("deprecation")
    @Test
    public void crearAtencionMedicaTest(){
        //Arrange
        AtencionMedica atencion = new AtencionMedica();
        atencion.setTipo("Ambulatoria");
        atencion.setFecha(new Date(2012,3,12));
        atencion.setDescripcion("Es un ejemplo");

        //Act
        AtencionMedica resultado = atencionMedicaRepository.save(atencion);

        //Assert
        assertNotNull(resultado.getId());
        assertEquals("Ambulatoria", resultado.getTipo());
        assertEquals(new Date(2012,3,12), resultado.getFecha());
        assertEquals("Es un ejemplo", resultado.getDescripcion());
    
    }
    
}
