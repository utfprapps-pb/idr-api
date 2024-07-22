package br.edu.utfpr.ProjetoIDRAPI.entity.vegetableDisease;


import java.time.LocalDate;

import br.edu.utfpr.ProjetoIDRAPI.entity.culture.Culture;
import br.edu.utfpr.ProjetoIDRAPI.entity.disease.Disease;
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
	private Property property;
	
	@NotNull
	@JoinColumn(name = "Culture_id")
    @ManyToOne
	private Culture culture;
	
	@NotNull
	@JoinColumn(name = "Disease_id")
    @ManyToOne
    private Disease disease;

}
