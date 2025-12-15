package br.edu.utfpr.ProjetoIDRAPI.entity.pregnancydiagnose.dto;

import br.edu.utfpr.ProjetoIDRAPI.entity.animal.Animal;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PregnancyDiagnoseDto {

    private long id;

    private LocalDate diagnoseDate;

    private LocalDate lastIA;

    private Animal animal;

}
