package br.edu.utfpr.ProjetoIDRAPI.dto;

import br.edu.utfpr.ProjetoIDRAPI.model.Animal;
import br.edu.utfpr.ProjetoIDRAPI.model.Product;
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
