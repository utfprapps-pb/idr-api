package br.edu.utfpr.ProjetoIDRAPI.cowFormulation.modules.protein.fractions;

import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.model.AnimalContext;
import br.edu.utfpr.ProjetoIDRAPI.entity.animal.Animal;
import br.edu.utfpr.ProjetoIDRAPI.entity.animal.AnimalService;
import br.edu.utfpr.ProjetoIDRAPI.entity.animal.dto.AnimalDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RumenUndegradableProteinService {

    private final AnimalService animalService;
    private final RumenUndegradableProteinCalculator rumenUndegradableProteinCalculator;

    public BigDecimal calculatePUR(AnimalDto animalDto) {

        Animal animal = animalService.findByIdentifier(animalDto.getIdentifier());

        if (animal == null) {
            throw new IllegalArgumentException("Animal não encontrado: " + animalDto.getIdentifier());
        }

        AnimalContext context = AnimalContext.builder()
                .animalOrigin(animal)
                .stage(animalDto.getStage())
                .milkYield(animalDto.getMilkProductionReal())
                .milkFatPct(animalDto.getFatContent())
                .milkProteinPct(animalDto.getProteinContent())
                .ambientTemperature(null) // se não usar agora
                .build();

        return rumenUndegradableProteinCalculator.calculateTotalPUR(context);
    }
}
