package br.gov.pr.idr.legacy.entity.pregnancydiagnose.dto;

import br.gov.pr.idr.legacy.entity.animal.Animal;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PregnancyDiagnoseDto {

    private long id;

    private LocalDate diagnoseDate;

    private LocalDate lastIA;

    private Animal animal;

}
