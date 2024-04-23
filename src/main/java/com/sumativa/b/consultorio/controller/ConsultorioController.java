package com.sumativa.b.consultorio.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.RestController;

import com.sumativa.b.consultorio.model.AtencionMedica;
import com.sumativa.b.consultorio.model.ConsultaMedica;
import com.sumativa.b.consultorio.model.Paciente;
import com.sumativa.b.consultorio.service.AtencionMedicaService;
import com.sumativa.b.consultorio.service.ConsultaMedicaService;
import com.sumativa.b.consultorio.service.PacienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/pacientes")
public class ConsultorioController {

    private static final Logger log = LoggerFactory.getLogger(ConsultorioController.class);

    @Autowired
    private PacienteService pacienteService;

    @Autowired 
    private ConsultaMedicaService consultaMedicaService;

    @Autowired
    private AtencionMedicaService atencionMedicaService;

    @GetMapping
    public List<Paciente> getPacientes() {
        return pacienteService.getAllPaciente();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Object> getPacienteById(@PathVariable Long id) {
        Optional<Paciente> paciente = pacienteService.getPacienteById(id);
        if(paciente.isEmpty()){
            log.error("No se encontro ningun Paciente con ese ID {} ", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("No se encontro ningun Paciente con ese ID"));
        }
        return ResponseEntity.ok(paciente.get());
    }
    
    
    @GetMapping("/{id}/consultas")
    public ResponseEntity<Object> getConsultasPaciente(@PathVariable Long id) {
        Optional<Paciente> paciente = pacienteService.getPacienteById(id);
        if(paciente.isEmpty()){
            log.error("No se encontro ningun Paciente con ese ID {} ", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("No se encontro ningun Paciente con ese ID"));
        }
        return ResponseEntity.ok(paciente.get().getConsultas());
    }

    @GetMapping("/{id}/atenciones")
    public ResponseEntity<Object> getAtencionesPaciente(@PathVariable Long id) {
        Optional<Paciente> paciente = pacienteService.getPacienteById(id);
        if(paciente.isEmpty()){
            log.error("No se encontro ningun Paciente con ese ID {} ", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("No se encontro ningun Paciente con ese ID"));
        }
        return ResponseEntity.ok(paciente.get().getAtenciones());
    }
    
    @GetMapping("/pacientes/contar")
    public int getCantidadPacientes() {
        List<Paciente> pacientes = pacienteService.getAllPaciente();
        return pacientes.size();
    }

    @PostMapping
    public ResponseEntity<Object> crearPaciente(@RequestBody Paciente paciente){
         // se valida que el campo nombre no este vacio
         if(paciente.getNombre() == null){
            log.error("El Nombre Paciente esta vacio");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Debe ingresar el nombre del paciente"));
        }

        // se valida que el campo Rut no este vacio
        if(paciente.getRut() == null){
            log.error("El Rut del Paciente esta vacio");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Debe ingresar el rut del paciente"));
        }

         // se valida que el campo dirección no este vacio
        if(paciente.getDireccion() == null){
            log.error("La dirección del Paciente esta vacio");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Debe ingresar la direccion del paciente"));
        }

         // se valida que el campo fecha de nacimiento no este vacio
        if(paciente.getFechaNacimeinto() == null){
            log.error("La fecha de nacimiento del  Paciente esta vacio");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Debe ingresar la fecha de nacimiento del paciente"));
        }

         // se valida que el campo correo no este vacio
         if(paciente.getCorreo() == null){
            log.error("El correo del Paciente esta vacio");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Debe ingresar el correo del paciente"));
        }

        Paciente pacienteCreado = pacienteService.crearPaciente(paciente);
        if(pacienteCreado == null){
            log.error("Error al crear el Usuario");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Error al crear el Usuario"));
        }

        // Asignar atenciones si estas son creadas con el paciente
        if(pacienteCreado.getAtenciones() != null){
            for(AtencionMedica a : pacienteCreado.getAtenciones()){
                a.setPaciente(pacienteCreado);
                atencionMedicaService.actualizarAtencionMedica(a.getId(), a);
            }   
        }

        // Asignar consultas si estas son creadas con el paciente
        if(pacienteCreado.getConsultas() != null){
            for(ConsultaMedica c : pacienteCreado.getConsultas()){
                c.setPaciente(pacienteCreado);
                consultaMedicaService.actualizarConsultaMedica(c.getId(), c);
            }   
        }

        return ResponseEntity.ok(pacienteCreado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarPaciente(@PathVariable Long id){
        Optional<Paciente> paciente = pacienteService.getPacienteById(id);
        if(paciente.isEmpty()){
            log.error("No se encontro ningun Paciente con ese ID {} ", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("No se encontro ningun Paciente con ese ID"));
        }
        pacienteService.eliminarPaciente(id);
        return ResponseEntity.ok("Paciente Eliminado");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizarPaciente(@PathVariable Long id, @RequestBody Paciente paciente){
        // Valida que el paciente exista
        Optional<Paciente> pacienteBuscado = pacienteService.getPacienteById(id);
        if(pacienteBuscado.isEmpty()){
            log.error("No se encontro ningun Paciente con ese ID {} ", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("No se encontro ningun Paciente con ese ID"));
        }

        // validaciones adicionales Paciente
        if(paciente.getNombre() == null){
            log.error("El Nombre Paciente esta vacio");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Debe ingresar el nombre del paciente"));
        }

        // se valida que el campo Rut no este vacio
        if(paciente.getRut() == null){
            log.error("El Rut del Paciente esta vacio");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Debe ingresar el rut del paciente"));
        }

         // se valida que el campo dirección no este vacio
        if(paciente.getDireccion() == null){
            log.error("La dirección del Paciente esta vacio");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Debe ingresar la direccion del paciente"));
        }

         // se valida que el campo fecha de nacimiento no este vacio
        if(paciente.getFechaNacimeinto() == null){
            log.error("La fecha de nacimiento del  Paciente esta vacio");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Debe ingresar la fecha de nacimiento del paciente"));
        }

         // se valida que el campo correo no este vacio
         if(paciente.getCorreo() == null){
            log.error("El correo del Paciente esta vacio");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Debe ingresar el correo del paciente"));
        }

        //Preservar Atenciones y Consultas
        Paciente pacienteActual = pacienteBuscado.get();
        pacienteActual.setRut(paciente.getRut());
        pacienteActual.setNombre(paciente.getNombre());
        pacienteActual.setFechaNacimeinto(paciente.getFechaNacimeinto());
        pacienteActual.setDireccion(paciente.getDireccion());
        pacienteActual.setCorreo(paciente.getCorreo());

        Paciente u = pacienteService.actualizarPaciente(id, pacienteActual);
        return ResponseEntity.ok(u);
    
    }

    

    static class ErrorResponse {
        private final String message;
    
        public ErrorResponse(String message){
            this.message = message;
        }
    
        public String getMessage(){
            return message;
        }
        
    }

}
