package br.edu.utfpr.ProjetoIDRAPI;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class ProjetoIdrApiApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ProjetoIdrApiApplication.class, args);
	}
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
