package com.sumativa.b.consultorio.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RestController;

import com.sumativa.b.consultorio.model.AtencionMedica;
import com.sumativa.b.consultorio.model.ConsultaMedica;
import com.sumativa.b.consultorio.model.Paciente;
import com.sumativa.b.consultorio.service.AtencionMedicaService;
import com.sumativa.b.consultorio.service.ConsultaMedicaService;
import com.sumativa.b.consultorio.service.PacienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
    public CollectionModel<EntityModel<Paciente>> getPacientes() {
        List<Paciente> pacientes = pacienteService.getAllPaciente();
        
        List<EntityModel<Paciente>> pacienteResources = pacientes.stream()
                .map(paciente -> EntityModel.of(paciente,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getPacienteById(paciente.getId())).withSelfRel()
                    ))
                .collect(Collectors.toList());
        WebMvcLinkBuilder linkTo = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getPacientes());
        CollectionModel<EntityModel<Paciente>> resourses = CollectionModel.of(pacienteResources, linkTo.withRel("pacientes"));

        return resourses;
    }
    
    @GetMapping("/{id}")
    public EntityModel<Paciente> getPacienteById(@PathVariable Long id) {
        Optional<Paciente> paciente = pacienteService.getPacienteById(id);
        if(paciente.isPresent()){
            return EntityModel.of(paciente.get(),
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getPacienteById(id)).withSelfRel(),
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getPacientes()).withRel("all-pacientes"));
        }else{
            log.error("No se encontro ningun Paciente con ese ID {} ", id);
            throw new NotFoundException("No se encontro ningun Paciente con ese ID: " + id);
        }
        
    }
    
    
    @GetMapping("/{id}/consultas")
    public CollectionModel<EntityModel<ConsultaMedica>> getConsultasPaciente(@PathVariable Long id) {
        Optional<Paciente> paciente = pacienteService.getPacienteById(id);
        if(paciente.isPresent()){
            List<ConsultaMedica> consultas = paciente.get().getConsultas();

            List<EntityModel<ConsultaMedica>> consultaModel = consultas.stream()
                .map(consulta -> EntityModel.of(consulta,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ConsultaMedicaController.class).getConsultaMedicaByID(consulta.getId())).withSelfRel()
                ))
                .collect(Collectors.toList());
            
            Link selfLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(this.getClass()).getConsultasPaciente(id)
            ).withSelfRel();

            Link pacientesLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(this.getClass()).getPacientes()
            ).withSelfRel();

            return CollectionModel.of(consultaModel, selfLink, pacientesLink);
        }else {
            log.error("No se encontro ningun Paciente con ese ID {} ", id);
            throw new NotFoundException("No se encontro ningun Paciente con ese ID: " + id);
        }
        
    }

    @GetMapping("/{id}/atenciones")
    public CollectionModel<EntityModel<AtencionMedica>> getAtencionesPaciente(@PathVariable Long id) {
        Optional<Paciente> paciente = pacienteService.getPacienteById(id);
        if(paciente.isPresent()){
            List<AtencionMedica> atenciones = paciente.get().getAtenciones();

            List<EntityModel<AtencionMedica>> atencionModel = atenciones.stream()
                .map(atencion -> EntityModel.of(atencion,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AtencionMedicaController.class).getAtencionMedicaByID(atencion.getId())).withSelfRel()
                ))
                .collect(Collectors.toList());
            
            Link selfLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(this.getClass()).getConsultasPaciente(id)
            ).withSelfRel();

            Link pacientesLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(this.getClass()).getPacientes()
            ).withSelfRel();

            return CollectionModel.of(atencionModel, selfLink, pacientesLink);
        }else {
            log.error("No se encontro ningun Paciente con ese ID {} ", id);
            throw new NotFoundException("No se encontro ningun Paciente con ese ID: " + id);
        }
    }

    @PostMapping
    public EntityModel<Paciente> crearPaciente(@RequestBody Paciente paciente){
         // se valida que el campo nombre no este vacio
         if(paciente.getNombre() == null || paciente.getNombre().isEmpty()){
            log.error("El Nombre Paciente esta vacio");
            throw new BadRequestException("Debe ingresar el nombre del paciente");
        }

        // se valida que el campo Rut no este vacio
        if(paciente.getRut() == null || paciente.getRut().isEmpty()){
            log.error("El Rut del Paciente esta vacio");
            throw new BadRequestException("Debe ingresar el rut del paciente");
            
        }

         // se valida que el campo dirección no este vacio
        if(paciente.getDireccion() == null || paciente.getDireccion().isEmpty()){
            log.error("La dirección del Paciente esta vacio");
            throw new BadRequestException("Debe ingresar la direccion del paciente");
        }

         // se valida que el campo fecha de nacimiento no este vacio
        if(paciente.getFechaNacimeinto() == null){
            log.error("La fecha de nacimiento del  Paciente esta vacio");
            throw new BadRequestException("Debe ingresar la fecha de nacimiento del paciente");
        }

         // se valida que el campo correo no este vacio
         if(paciente.getCorreo() == null || paciente.getCorreo().isEmpty()){
            log.error("El correo del Paciente esta vacio");
            throw new BadRequestException("Debe ingresar el correo del paciente");
        }

        Paciente pacienteCreado = pacienteService.crearPaciente(paciente);
        if(pacienteCreado == null){
            log.error("Error al crear el Paciente");
            throw new BadRequestException("Error al crear el Paciente");
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

        return EntityModel.of(pacienteCreado,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getPacienteById(pacienteCreado.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getPacientes()).withRel("all-pacientes"));
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
    public EntityModel<Paciente> actualizarPaciente(@PathVariable Long id, @RequestBody Paciente paciente){
        // Valida que el paciente exista
        Optional<Paciente> pacienteBuscado = pacienteService.getPacienteById(id);
        if(pacienteBuscado.isEmpty()){
            log.error("No se encontro ningun Paciente con ese ID {} ", id);
            throw new BadRequestException("No se encontro ningun Paciente con ese ID");
        }

        // validaciones adicionales Paciente
        if(paciente.getNombre() == null || paciente.getNombre().isEmpty()){
            log.error("El Nombre Paciente esta vacio");
            throw new BadRequestException("Debe ingresar el nombre del paciente");
        }

        // se valida que el campo Rut no este vacio
        if(paciente.getRut() == null || paciente.getRut().isEmpty()){
            log.error("El Rut del Paciente esta vacio");
            throw new BadRequestException("Debe ingresar el rut del paciente");
        }

         // se valida que el campo dirección no este vacio
        if(paciente.getDireccion() == null || paciente.getDireccion().isEmpty()){
            log.error("La dirección del Paciente esta vacio");
            throw new BadRequestException("Debe ingresar la direccion del paciente");
        }

         // se valida que el campo fecha de nacimiento no este vacio
        if(paciente.getFechaNacimeinto() == null){
            log.error("La fecha de nacimiento del  Paciente esta vacio");
            throw new BadRequestException("Debe ingresar la fecha de nacimiento del paciente");
        }

         // se valida que el campo correo no este vacio
         if(paciente.getCorreo() == null || paciente.getCorreo().isEmpty()){
            log.error("El correo del Paciente esta vacio");
            throw new BadRequestException("Debe ingresar el correo del paciente");
        }

        //Preservar Atenciones y Consultas
        Paciente pacienteActual = pacienteBuscado.get();
        pacienteActual.setRut(paciente.getRut());
        pacienteActual.setNombre(paciente.getNombre());
        pacienteActual.setFechaNacimeinto(paciente.getFechaNacimeinto());
        pacienteActual.setDireccion(paciente.getDireccion());
        pacienteActual.setCorreo(paciente.getCorreo());

        Paciente u = pacienteService.actualizarPaciente(id, pacienteActual);
        return EntityModel.of(u,
        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getPacienteById(id)).withSelfRel(),
        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getPacientes()).withRel("all-pacientes"));
    
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
