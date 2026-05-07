package br.gov.pr.idr.legacy.entity.animalsales;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.gov.pr.idr.legacy.entity.animal.Animal;
import br.gov.pr.idr.legacy.enums.Destination;
import br.gov.pr.idr.legacy.enums.Reason;
import jakarta.persistence.Entity;
import org.hibernate.envers.Audited;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity @Audited
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimalSales {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private LocalDate dateSale;
	
	private Reason reason;
	
	private BigDecimal value;
	
	private Destination destination;
	
	@JoinColumn(name = "Animal_id")
	@ManyToOne
	private Animal animal;
}
