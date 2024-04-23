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

import com.sumativa.b.consultorio.model.AtencionMedica;
import com.sumativa.b.consultorio.model.AtencionMedicaDTO;
import com.sumativa.b.consultorio.model.Paciente;
import com.sumativa.b.consultorio.service.AtencionMedicaService;
import com.sumativa.b.consultorio.service.PacienteService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/atencion")
public class AtencionMedicaController {
    private static final Logger log = LoggerFactory.getLogger(AtencionMedicaController.class);

    @Autowired
    private AtencionMedicaService atencionMedicaService;

    @Autowired
    private PacienteService pacienteService;

    @GetMapping
    public CollectionModel<EntityModel<AtencionMedica>> getAllAtencionMedica(){
        List<AtencionMedica> atenciones = atencionMedicaService.getAllAtencionMedica();

        List<EntityModel<AtencionMedica>> atencionResources = atenciones.stream()
                .map(atencion -> EntityModel.of(atencion,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAtencionMedicaByID(atencion.getId())).withSelfRel()
                    ))
                .collect(Collectors.toList());
        WebMvcLinkBuilder linkTo = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllAtencionMedica());
        CollectionModel<EntityModel<AtencionMedica>> resourses = CollectionModel.of(atencionResources, linkTo.withRel("atencionesMedicas"));

        return resourses;
    }

    @GetMapping("/{id}")
    public EntityModel<AtencionMedica> getAtencionMedicaByID(@PathVariable Long id) {
        Optional<AtencionMedica> atencion = atencionMedicaService.getAtencionMedicaById(id);
        if(atencion.isPresent()){
            return EntityModel.of(atencion.get(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAtencionMedicaByID(id)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllAtencionMedica()).withRel("all-atencionesMedicas"));
        }else {
            log.error("No se encontro ninguna atención medica con ese ID {} ", id);
            throw new NotFoundException("No se encontro ninguna atención medica con ese ID: " + id);
        }
    }
    

    @PostMapping
    public EntityModel<AtencionMedica> crearAtencionMedica(@RequestBody AtencionMedicaDTO atencionMedica){
        // se valida que el campo id paciente no este vacio
        if(atencionMedica.getIdPaciente() == null){
            log.error("El ID Paciente esta vacio");
            throw new BadRequestException("Debe ingresar el ID del paciente antes de crear una atencion medica ");
        }
        // Se busca el paciente segun el id entregado
        Optional<Paciente> buscarPaciente = pacienteService.getPacienteById(atencionMedica.getIdPaciente());
        if(buscarPaciente.isEmpty()){
            log.error("No se encontro un Paciente con el ID {} entregado", atencionMedica.getIdPaciente());
            throw new NotFoundException("No se encontro ningun Usuario con ese ID");
           
           
        }
        // Validaciones adicionales
        if(atencionMedica.getFecha() == null){
            log.error("La fecha de atención es obligatoria");
            throw new BadRequestException("La fecha de atención es obligatoria ");
        }

        if(atencionMedica.getTipo() == null || atencionMedica.getTipo().isEmpty()){
            log.error("El tipo de atención es obligatorio");
            throw new BadRequestException("El tipo de atención es obligatorio ");
        }

        AtencionMedica atencionCreada = new AtencionMedica();
        atencionCreada.setFecha(atencionMedica.getFecha());
        atencionCreada.setTipo(atencionMedica.getTipo());
        atencionCreada.setDescripcion(atencionMedica.getDescripcion());
        atencionCreada.setPaciente(buscarPaciente.get());

        AtencionMedica a = atencionMedicaService.crearAtencionMedica(atencionCreada);
        if(a == null){
            log.error("Error al crear la atención medica");
            throw new BadRequestException("Error al crear la atención medica");
        }

        return EntityModel.of(a,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAtencionMedicaByID(a.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllAtencionMedica()).withRel("all-atencionesMedicas"));

    }

    @PutMapping("/{id}")
    public EntityModel<AtencionMedica> actualizarAtencionMedica(@PathVariable Long id, @RequestBody AtencionMedicaDTO atencionMedica){
        // se valida que el campo id paciente no este vacio
        if(atencionMedica.getIdPaciente() == null){
            log.error("El ID Paciente esta vacio");
            throw new BadRequestException("Debe ingresar el ID del paciente antes de crear una atencion medica");
        }

        // Se busca el paciente segun el id entregado
        Optional<Paciente> buscarPaciente = pacienteService.getPacienteById(atencionMedica.getIdPaciente());
        if(buscarPaciente.isEmpty()){
            log.error("No se encontro un Paciente con el ID {} entregado", atencionMedica.getIdPaciente());
            throw new NotFoundException("No se encontro ningun Paciente con ese ID: " + atencionMedica.getIdPaciente());
           
        }

        // Se busca la AtencionMedica segun el id entregado
        Optional<AtencionMedica> atencion = atencionMedicaService.getAtencionMedicaById(id);
        if(atencion.isEmpty()){
            log.error("No se encontro ninguna atención medica con ese ID {} ", id);
            throw new NotFoundException("No se encontro ninguna atención medica con ese ID: " + id);
        }

        // Validaciones adicionales
        if(atencionMedica.getFecha() == null){
            log.error("La fecha de atención es obligatoria");
            throw new BadRequestException("La fecha de atención es obligatoria ");
        }

        if(atencionMedica.getTipo() == null || atencionMedica.getTipo().isEmpty()){
            log.error("El tipo de atención es obligatorio");
            throw new BadRequestException("El tipo de atención es obligatorio");
        }

        AtencionMedica atencionEditada  = new AtencionMedica();
        atencionEditada.setFecha(atencionMedica.getFecha());
        atencionEditada.setTipo(atencionMedica.getTipo());
        atencionEditada.setDescripcion(atencionMedica.getDescripcion());
        atencionEditada.setPaciente(buscarPaciente.get());
        
        AtencionMedica a = atencionMedicaService.actualizarAtencionMedica(id, atencionEditada);

        return EntityModel.of(a,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAtencionMedicaByID(a.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllAtencionMedica()).withRel("all-atencionesMedicas"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarAtencionMedica(@PathVariable Long id){
         // Se busca la AtencionMedica segun el id entregado
         Optional<AtencionMedica> atencion = atencionMedicaService.getAtencionMedicaById(id);
         if(atencion.isEmpty()){
             log.error("No se encontro ninguna atención medica con ese ID {} ", id);
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("No se encontro ninguna atención medica con ese ID"));
         }
         atencionMedicaService.eliminarAtencionMedica(id);
         return ResponseEntity.ok("Atencion Medica Eliminado");
 
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
