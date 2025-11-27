package br.edu.utfpr.ProjetoIDRAPI.entity.foragedisponibility;

import java.time.LocalDate;
import java.math.BigDecimal;

import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

@Entity @Audited
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
	private Double averageCost;
	private Long usefulLife;
	private String growthCycle;
	private String observation;
	private String ownershipType;
	private Double entry;
	private Double residue;
	private Double kg;
	private Double picketArea;
	private Double efficiency;

	@Column(precision = 20, scale = 0)
	private BigDecimal numCows;

	private Double kgCows;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "property_id")
	private Property property;

}