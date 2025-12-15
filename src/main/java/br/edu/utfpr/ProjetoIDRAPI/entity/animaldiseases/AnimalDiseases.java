package br.edu.utfpr.ProjetoIDRAPI.entity.animaldiseases;

import java.time.LocalDate;

import br.edu.utfpr.ProjetoIDRAPI.entity.animal.Animal;
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
public class AnimalDiseases {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String diagnosis;
	
	private LocalDate diagnosisDate;
	
	@JoinColumn(name = "Animal_id")
	@ManyToOne
	private Animal animal;
}
