package br.edu.utfpr.ProjetoIDRAPI.dto;

import br.edu.utfpr.ProjetoIDRAPI.model.Animal;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PregnancyDiagnoseDto {

    private long id;

    private LocalDate diagnoseDate;

    private LocalDate lastIA;

    private Animal animal;

}
