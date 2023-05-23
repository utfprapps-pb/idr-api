package br.edu.utfpr.ProjetoIDRAPI.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Plague {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	private String plagueName;
}
