package br.gov.pr.idr.legacy.entity.vegetabledisease;


import java.time.LocalDate;

import br.gov.pr.idr.legacy.entity.culture.Culture;
import br.gov.pr.idr.legacy.entity.disease.Disease;
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
public class VegetableDisease {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	private String infestationType;
	
	private LocalDate date;
	
//	@NotNull
//	@JoinColumn(name = "Property_id")
//    @ManyToOne
//	private Property property;
	
	@NotNull
	@JoinColumn(name = "Culture_id")
    @ManyToOne
	private Culture culture;
	
	@NotNull
	@JoinColumn(name = "Disease_id")
    @ManyToOne
    private Disease disease;

}
