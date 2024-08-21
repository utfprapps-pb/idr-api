package br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility;

import java.math.BigInteger;
import java.time.LocalDate;

import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
