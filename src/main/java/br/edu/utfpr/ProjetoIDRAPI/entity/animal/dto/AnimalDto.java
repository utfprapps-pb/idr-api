package br.edu.utfpr.ProjetoIDRAPI.entity.animal.dto;

import br.edu.utfpr.ProjetoIDRAPI.entity.breed.dto.BreedDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.property.dto.PropertyDto;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.dto.DietIngredient;
import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.enums.ProductionStage;
import lombok.Data;
import java.math.BigDecimal;

@SuppressWarnings("unused")
@Data
public class AnimalDto {

    private Long id;

    private PropertyDto property;

    private String type;

    // identifier é o nome do animal
    private String identifier;

    private BreedDto breed;

    private String bornDate;

    private Float bornWeight;

    private Float previousWeight;

    private Float currentWeight;

    private Float ecc;

    // Status da Vaca (Seca/Lactação/Pré-Parto)
    private ProductionStage stage;

    // Peso Estimado da Vaca no Próximo Balanceamento 
    private BigDecimal projectedBodyWeight;

    // Ingestão de Matéria Seca
    private BigDecimal IMS;

    // Produção de Leite Real
    private BigDecimal milkProductionReal;

    // Produção de Leite Projetada Para Pico
    private BigDecimal milkProductionProjected;

    // Teor de Gordura
    private BigDecimal fatContent;

    // Teor de Proteína
    private BigDecimal proteinContent;

    // Peso Vivo
    private BigDecimal liveWeight;

    // Recomendação de Perda de Peso
    private BigDecimal weightLossPerDay;

    // Recomendação de Ganho de Peso
    private BigDecimal weightGainPerDay;

    // Dias em Lactação
    private Integer daysInLactation;

    // Data do Balanceamento
    private Integer balanceDate;

    // Data da Próxima Visita
    private Integer nextVisitDate;

    // Data do Parto Atual
    private Integer parturitionDate;

}
