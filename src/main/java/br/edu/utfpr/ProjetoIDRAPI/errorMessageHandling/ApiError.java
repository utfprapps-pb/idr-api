package br.edu.utfpr.ProjetoIDRAPI.errorMessageHandling;

import java.util.Date;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiError {
	private long timestamp = new Date().getTime();
	private int status;
	private String message;
	private String url;
	private Map<String, String> errors;
	
	public ApiError(int status, String message, String url, Map<String, String> errors) {
		this.status = status;
		this.message = message;
		this.url = url;
		this.errors = errors;
	}
}
