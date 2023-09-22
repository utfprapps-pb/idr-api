package br.edu.utfpr.ProjetoIDRAPI.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VegetablePlague {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	private String infestationType;
	
	private LocalDate date;

	@NotNull
    @ManyToOne
	private Property property;

	@NotNull
    @ManyToOne
	private Culture culture;

	@NotNull
    @ManyToOne
    private Plague plague;

}
