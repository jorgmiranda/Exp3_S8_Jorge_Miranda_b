package com.sumativa.b.consultorio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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
import org.springframework.web.bind.annotation.RestController;

import com.sumativa.b.consultorio.model.ConsultaMedica;
import com.sumativa.b.consultorio.model.ConsultaMedicaDTO;
import com.sumativa.b.consultorio.model.Paciente;
import com.sumativa.b.consultorio.service.ConsultaMedicaService;
import com.sumativa.b.consultorio.service.PacienteService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/consulta")
public class ConsultaMedicaController {
    private static final Logger log = LoggerFactory.getLogger(ConsultaMedicaController.class);

    @Autowired
    private ConsultaMedicaService consultaMedicaService;

    @Autowired
    private PacienteService pacienteService;

    @GetMapping
    public CollectionModel<EntityModel<ConsultaMedica>> getAllConsultaMedica(){
        List<ConsultaMedica> consultas = consultaMedicaService.getAllConsultaMedica();
        
        List<EntityModel<ConsultaMedica>> consultaResources = consultas.stream()
                .map(consulta -> EntityModel.of(consulta,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getConsultaMedicaByID(consulta.getId())).withSelfRel()
                    ))
                .collect(Collectors.toList());
        WebMvcLinkBuilder linkTo = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllConsultaMedica());
        CollectionModel<EntityModel<ConsultaMedica>> resourses = CollectionModel.of(consultaResources, linkTo.withRel("consultas"));

        return resourses;
    }

    @GetMapping("/{id}")
    public EntityModel<ConsultaMedica> getConsultaMedicaByID(@PathVariable Long id) {
        Optional<ConsultaMedica> consulta = consultaMedicaService.getConsultaMedicaById(id);
        if(consulta.isPresent()){
            return EntityModel.of(consulta.get(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getConsultaMedicaByID(id)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllConsultaMedica()).withRel("all-consultasMedicas"));
        }else {
            log.error("No se encontro ninguna consulta medica con ese ID {} ", id);
            throw new NotFoundException("No se encontro ninguna consulta medica con ese ID: " + id);
        }
    }

    @PostMapping
    public EntityModel<ConsultaMedica> crearConsultaMedica(@RequestBody ConsultaMedicaDTO consultaMedica){
        // se valida que el campo id paciente no este vacio
        if(consultaMedica.getIdPaciente() == null){
            log.error("El ID Paciente esta vacio");
            throw new BadRequestException("Debe ingresar el ID del paciente antes de crear una Consulta medica");
        }

        // Se busca el paciente segun el id entregado
        Optional<Paciente> buscarPaciente = pacienteService.getPacienteById(consultaMedica.getIdPaciente());
        if(buscarPaciente.isEmpty()){
            log.error("No se encontro un Paciente con el ID {} entregado", consultaMedica.getIdPaciente());
            throw new NotFoundException("No se encontro ningun Paciente con ese ID");
        }

        // Validaciones adicionales
        if(consultaMedica.getFecha() == null){
            log.error("La fecha de atención es obligatoria");
            throw new BadRequestException("La fecha de atención es obligatoria");
        }

        if(consultaMedica.getMotivoConsulta() == null || consultaMedica.getMotivoConsulta().isEmpty()){
            log.error("El motivo de la consulta es obligatorio");
            throw new BadRequestException("El motivo de la consulta es obligatorio");
        }

        if(consultaMedica.getTratamiento() == null || consultaMedica.getTratamiento().isEmpty()){
            log.error("El tratamiento es obligatorio");
            throw new BadRequestException("El tratamiento es obligatorio");
        }

        if(consultaMedica.getDiagnostico() == null || consultaMedica.getDiagnostico().isEmpty()){
            log.error("El diagnostico es obligatorio");
            throw new BadRequestException("El diagnostico es obligatorio");
        }

        ConsultaMedica consultaCreada = new ConsultaMedica();
        consultaCreada.setFecha(consultaMedica.getFecha());
        consultaCreada.setDiagnostico(consultaMedica.getDiagnostico());
        consultaCreada.setMotivoConsulta(consultaMedica.getMotivoConsulta());
        consultaCreada.setTratamiento(consultaMedica.getTratamiento());
        consultaCreada.setPaciente(buscarPaciente.get());

        ConsultaMedica c = consultaMedicaService.crearConsultaMedica(consultaCreada);
        if(c == null){
            log.error("Error al crear la Consulta medica");
            throw new BadRequestException("Error al crear la Consulta medica");
        }
        return EntityModel.of(c,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getConsultaMedicaByID(c.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllConsultaMedica()).withRel("all-consultasMedicas"));

    }

    @PutMapping("/{id}")
    public EntityModel<ConsultaMedica> actualizarConsultaMedica(@PathVariable Long id, @RequestBody ConsultaMedicaDTO consultaMedica){
        // se valida que el campo id paciente no este vacio
        if(consultaMedica.getIdPaciente() == null){
            log.error("El ID Paciente esta vacio");
            throw new BadRequestException("Debe ingresar el ID del paciente antes de crear una Consulta medica");
        }

        // Se busca el paciente segun el id entregado
        Optional<Paciente> buscarPaciente = pacienteService.getPacienteById(consultaMedica.getIdPaciente());
        if(buscarPaciente.isEmpty()){
            log.error("No se encontro un Paciente con el ID {} entregado", consultaMedica.getIdPaciente());
            throw new NotFoundException("No se encontro ningun Paciente con ese ID");
           
        }
        //Se busca la consulta media segun el id ingresado
        Optional<ConsultaMedica> consulta = consultaMedicaService.getConsultaMedicaById(id);
        if(consulta.isEmpty()){
            log.error("No se encontro ninguna consulta medica con ese ID {} ", id);
            throw new NotFoundException("No se encontro ninguna consulta medica con ese ID");
        }

        // Validaciones adicionales
        if(consultaMedica.getFecha() == null){
            log.error("La fecha de atención es obligatoria");
            throw new BadRequestException("La fecha de atención es obligatoria");
        }

        if(consultaMedica.getMotivoConsulta() == null || consultaMedica.getMotivoConsulta().isEmpty()){
            log.error("El motivo de la consulta es obligatorio");
            throw new BadRequestException("El motivo de la consulta es obligatorio");
        }

        if(consultaMedica.getTratamiento() == null || consultaMedica.getTratamiento().isEmpty()){
            log.error("El tratamiento es obligatorio");
            throw new BadRequestException("El tratamiento es obligatorio");
        }

        if(consultaMedica.getDiagnostico() == null || consultaMedica.getDiagnostico().isEmpty()){
            log.error("El diagnostico es obligatorio");
            throw new BadRequestException("El diagnostico es obligatorio");
        }

        ConsultaMedica consultaEditada = new ConsultaMedica();
        consultaEditada.setFecha(consultaMedica.getFecha());
        consultaEditada.setDiagnostico(consultaMedica.getDiagnostico());
        consultaEditada.setMotivoConsulta(consultaMedica.getMotivoConsulta());
        consultaEditada.setTratamiento(consultaMedica.getTratamiento());
        consultaEditada.setPaciente(buscarPaciente.get());

        ConsultaMedica c = consultaMedicaService.actualizarConsultaMedica(id, consultaEditada);

        return EntityModel.of(c,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getConsultaMedicaByID(id)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllConsultaMedica()).withRel("all-consultasMedicas"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarConsultaMedica(@PathVariable Long id){
        Optional<ConsultaMedica> consulta = consultaMedicaService.getConsultaMedicaById(id);
        if(consulta.isEmpty()){
            log.error("No se encontro ninguna consulta medica con ese ID {} ", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("No se encontro ninguna consulta medica con ese ID"));
        }
        consultaMedicaService.eliminarConsultaMedica(id);
        return ResponseEntity.ok("Consulta medica eliminada");
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
