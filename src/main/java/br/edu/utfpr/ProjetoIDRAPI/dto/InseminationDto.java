package br.edu.utfpr.ProjetoIDRAPI.dto;

import br.edu.utfpr.ProjetoIDRAPI.model.Animal;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InseminationDto {

    private long id;

    private LocalDate iaDate;

    private String bull;

    private Animal animal;

}
