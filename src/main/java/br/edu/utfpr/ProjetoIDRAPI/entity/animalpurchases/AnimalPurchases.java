package br.edu.utfpr.ProjetoIDRAPI.entity.animalpurchases;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.edu.utfpr.ProjetoIDRAPI.entity.animal.Animal;
import jakarta.persistence.Entity;
import org.hibernate.envers.Audited;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class AnimalPurchases {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private LocalDate datePurchase;
	
	private LocalDate birthDate;
	
	private BigDecimal value;

	@ManyToOne
	private Animal animal;

}
