package br.edu.utfpr.ProjetoIDRAPI.errorMessageHandling;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
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
