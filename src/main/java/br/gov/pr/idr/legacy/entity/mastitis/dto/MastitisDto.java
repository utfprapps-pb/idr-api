package br.gov.pr.idr.legacy.entity.mastitis.dto;

import br.gov.pr.idr.legacy.entity.animal.Animal;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MastitisDto {

    private long id;

    private LocalDate diagnoseDate;

    private String mastitisType;

    private String cmtResult;

    private String ad;

    private String ae;

    private String pd;

    private String pe;

    private Animal animal;

}
