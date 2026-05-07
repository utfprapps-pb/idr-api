package br.gov.pr.idr.legacy.entity.generalcultivation.dto;

import lombok.Data;

@Data
public class GeneralCultivationDto {
 private Long id;
 private String name;
 private String type;
 private Double dryMatter;
 private Double crudeProtein;
 private Double totalDigestibleNutrients;
 private Double calcium;
 private Double phosphorus;
 private Double nonFibrousCarbohydrates;
 private Double etherExtract;
 private Double rumenDegradableProtein;
}
