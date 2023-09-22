package br.edu.utfpr.ProjetoIDRAPI.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.edu.utfpr.ProjetoIDRAPI.enums.Destination;
import br.edu.utfpr.ProjetoIDRAPI.enums.Reason;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
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
