package br.edu.utfpr.ProjetoIDRAPI.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping; 
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.edu.utfpr.ProjetoIDRAPI.ErrorMessageHandling.ApiError;
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
	public ResponseEntity createRegister(@RequestBody @Valid T entity) {
		getService().save(entity);
	    return ResponseEntity.ok(new GenericResponse("Registro inserido com sucesso"));
	}
	
	@PutMapping("{id}")
    public GenericResponse updateRegister(@RequestBody @Valid T entity, @PathVariable ID id) {
		try {
			T ent = getService().findOne(id);
	    	
	    	if(ent != null) {
	    		getService().save(entity);
	    		return new GenericResponse("Registro atualizado com sucesso");
	    	}else {
	    		return new GenericResponse("Registro inexistente");
	    	}
		} catch (Exception e) {
			return new GenericResponse("Erro ao atualizar o registro!");
		}
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
	public ResponseEntity deleteRegister(@PathVariable ID id){
		try {
			getService().delete(id);
	    	return ResponseEntity.ok(new GenericResponse("Registro excluido com sucesso"));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new GenericResponse("Não foi possível excluir o registro!"));
		}
	}
	
	private D convertToDto(T entity) {
    	return getModelMapper().map(entity, this.typeDtoClass);
    }
	
	@ExceptionHandler({ConstraintViolationException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiError handlerSQLException(ConstraintViolationException exception, HttpServletRequest request) {
		return new ApiError(HttpStatus.BAD_REQUEST.value(), "Erro ao realizar o registro no banco de dados", request.getServletPath(), null);
	}
	
	@ExceptionHandler({DataIntegrityViolationException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiError handlerSQLException(DataIntegrityViolationException exception, HttpServletRequest request) {
		return new ApiError(HttpStatus.BAD_REQUEST.value(), "Erro na integridade dos dados", request.getServletPath(), null);
	}
	
	@ExceptionHandler({MethodArgumentNotValidException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiError handlerValidationException(MethodArgumentNotValidException exception, HttpServletRequest request) {
		BindingResult result = exception.getBindingResult();
		
		Map<String, String> validationErrors = new HashMap<>();
		
		for(FieldError fieldError: result.getFieldErrors()) {
			validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
		}
		
		return new ApiError(HttpStatus.BAD_REQUEST.value(), "Erro ao validar as informações!", request.getServletPath(), validationErrors);
	}
}
