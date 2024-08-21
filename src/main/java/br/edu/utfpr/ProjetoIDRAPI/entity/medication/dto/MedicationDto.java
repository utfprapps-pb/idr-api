package br.edu.utfpr.ProjetoIDRAPI.entity.medication.dto;

import br.edu.utfpr.ProjetoIDRAPI.entity.animal.Animal;
import br.edu.utfpr.ProjetoIDRAPI.entity.product.Product;
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
