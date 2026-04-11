package br.edu.utfpr.ProjetoIDRAPI.entity.cultivationdisease;


import java.time.LocalDate;

import br.edu.utfpr.ProjetoIDRAPI.entity.feed.Feed;
import br.edu.utfpr.ProjetoIDRAPI.entity.disease.Disease;
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
public class CultivationDisease {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	private String infestationType;
	
	private LocalDate date;
	
	@NotNull
	@JoinColumn(name = "property_id")
    @ManyToOne
	private Property property;
	
	@NotNull
	@JoinColumn(name = "feed_id")
    @ManyToOne
	private Feed feed;
	
	@NotNull
	@JoinColumn(name = "disease_id")
    @ManyToOne
    private Disease disease;

}
