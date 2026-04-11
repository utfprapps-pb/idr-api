package br.edu.utfpr.ProjetoIDRAPI.entity.cultivationpest;

import java.time.LocalDate;

import br.edu.utfpr.ProjetoIDRAPI.entity.feed.Feed;
import br.edu.utfpr.ProjetoIDRAPI.entity.pest.Pest;
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
public class CultivationPest {

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
	private Feed feed;

	@NotNull
    @ManyToOne
    private Pest pest;

}
