package br.gov.pr.idr.legacy.entity.insemination.dto;

import br.gov.pr.idr.legacy.entity.animal.Animal;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InseminationDto {

    private long id;

    private LocalDate iaDate;

    private String bull;

    private Animal animal;

}
