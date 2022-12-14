package br.edu.utfpr.ProjetoIDRAPI.controller;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.edu.utfpr.ProjetoIDRAPI.service.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.utils.GenericResponse;

public abstract class CrudController<T, D, ID extends Serializable> {
	protected abstract CrudService<T, ID> getService();
	protected abstract ModelMapper getModelMapper();
	private final Class<T> typeClass;
	private final Class<D> typeDtoClass;
	
	public CrudController(Class<T> typeClass, Class<D> typeDtoClass) {
		this.typeClass = typeClass;
		this.typeDtoClass = typeDtoClass;
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public GenericResponse createRegister(@RequestBody @Valid T entity) {
		getService().save(entity);
		return new GenericResponse("Registro inserido com sucesso");
	}
	
	@PutMapping
    public GenericResponse updateRegister(@RequestBody @Valid T entity) {
		getService().save(entity);
        return new GenericResponse("Registro atualizado com sucesso");
    }
	
	@GetMapping
    public ResponseEntity<List<D>> listAll(){
    	return ResponseEntity.ok(getService().findAll().stream()
    			.map(this::convertToDto)
    			.collect(Collectors.toList()));
    }
	
	@GetMapping("{id}")
    public ResponseEntity<D> findOne(@PathVariable ID id){
    	T entity = getService().findOne(id);
    	
    	if(entity != null) {
    		return ResponseEntity.ok(convertToDto(getService().findOne(id)));
    	} else {
    		return ResponseEntity.noContent().build();
    	}
    }
	
	@DeleteMapping("{id}")
	public GenericResponse deleteRegister(@PathVariable ID id){
		getService().delete(id);
		return new GenericResponse("Registro excluido com sucesso");
	}
	
	private D convertToDto(T entity) {
    	return getModelMapper().map(entity, this.typeDtoClass);
    }
}
