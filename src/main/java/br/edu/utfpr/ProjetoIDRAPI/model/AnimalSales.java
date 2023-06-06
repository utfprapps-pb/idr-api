package br.edu.utfpr.ProjetoIDRAPI.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.edu.utfpr.ProjetoIDRAPI.enums.Destination;
import br.edu.utfpr.ProjetoIDRAPI.enums.Reason;
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
