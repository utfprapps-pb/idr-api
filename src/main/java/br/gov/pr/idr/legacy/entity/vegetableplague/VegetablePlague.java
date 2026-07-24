package br.gov.pr.idr.legacy.entity.vegetableplague;

import java.time.LocalDate;

import br.gov.pr.idr.legacy.entity.culture.Culture;
import br.gov.pr.idr.legacy.entity.plague.Plague;
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
public class VegetablePlague {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	private String infestationType;
	
	private LocalDate date;

//	@NotNull
//    @ManyToOne
//	private Property property;

	@NotNull
    @ManyToOne
	private Culture culture;

	@NotNull
    @ManyToOne
    private Plague plague;

}
