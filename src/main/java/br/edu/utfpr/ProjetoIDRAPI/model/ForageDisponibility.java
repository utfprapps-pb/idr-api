package br.edu.utfpr.ProjetoIDRAPI.model;

import java.math.BigInteger;
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
public class ForageDisponibility {

	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private long id;
	 
	 private LocalDate date;
	 
	 private String forage;
	 
	 private Float entry;
	 
	 private Float residue;
	 
	 private Float kg;
	 
	 private Float picketArea;
	 
	 private Float efficiency;
	 
	 private BigInteger numCows;
	 
	 private Float kgCows;

	 @NotNull
	 @ManyToOne
	 private Property property;

}
