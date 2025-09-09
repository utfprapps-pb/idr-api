package br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility;

import java.math.BigInteger;
import java.time.LocalDate;

import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
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

	@Column(precision = 20, scale = 0)
	private java.math.BigDecimal numCows;

	private Float kgCows;

	 @NotNull
	 @ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name = "property_id")
	 private Property property;

}
