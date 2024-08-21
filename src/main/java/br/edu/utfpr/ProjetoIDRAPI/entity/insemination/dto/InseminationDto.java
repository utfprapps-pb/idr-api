package br.edu.utfpr.ProjetoIDRAPI.entity.insemination.dto;

import br.edu.utfpr.ProjetoIDRAPI.entity.animal.Animal;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InseminationDto {

    private long id;

    private LocalDate iaDate;

    private String bull;

    private Animal animal;

}
