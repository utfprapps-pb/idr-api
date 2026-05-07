package br.gov.pr.idr.legacy.entity.medication.dto;

import br.gov.pr.idr.legacy.entity.animal.Animal;
import br.gov.pr.idr.legacy.entity.product.Product;
import lombok.Data;

@Data
public class MedicationDto {

    private long id;

    private String applicationDate;

    private String appliedDose;

    private String applicationWay;

    private Product product;

    private Animal animal;

}
