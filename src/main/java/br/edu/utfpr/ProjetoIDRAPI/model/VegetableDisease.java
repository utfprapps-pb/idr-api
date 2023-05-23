package br.edu.utfpr.ProjetoIDRAPI.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VegetableDisease {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	private String infestationType;
	
	private LocalDate date;
	
	@NotNull
	@JoinColumn(name = "Property_id")
    @ManyToOne
	private Property idProperty;
	
	@NotNull
	@JoinColumn(name = "Culture_id")
    @ManyToOne
	private Culture idCulture;
	
	@NotNull
	@JoinColumn(name = "Disease_id")
    @ManyToOne
    private Disease idDisease;
}