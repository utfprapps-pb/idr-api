package br.edu.utfpr.ProjetoIDRAPI.entity.disease;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.envers.Audited;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity @Audited
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "disease")
public class Disease {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotBlank
	private String name;
}
