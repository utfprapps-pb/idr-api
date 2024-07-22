package br.edu.utfpr.ProjetoIDRAPI.entity.vegetablePlague;

import java.time.LocalDate;

import br.edu.utfpr.ProjetoIDRAPI.entity.culture.Culture;
import br.edu.utfpr.ProjetoIDRAPI.entity.plague.Plague;
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
public class VegetablePlague {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	private String infestationType;
	
	private LocalDate date;

	@NotNull
    @ManyToOne
	private Property property;

	@NotNull
    @ManyToOne
	private Culture culture;

	@NotNull
    @ManyToOne
    private Plague plague;

}
