package br.edu.utfpr.ProjetoIDRAPI.cowFormulation.modules.protein.fractions;

import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.model.AnimalContext;
import br.edu.utfpr.ProjetoIDRAPI.entity.animal.Animal;
import br.edu.utfpr.ProjetoIDRAPI.entity.animal.dto.AnimalDto;
import br.edu.utfpr.ProjetoIDRAPI.entity.animal.AnimalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RumenDegradableProteinService {

    private final AnimalService animalService;
    private final RumenDegradableProteinCalculator rumenDegradableProteinCalculator;

    /**
     * Calcula a Proteína Degradável no Rúmen (PDR) total (C111),
     * considerando o status da vaca.
     */
    public BigDecimal calculatePDR(AnimalDto animalDto) {

        // 1. Buscar Animal no banco
        Animal animal = animalService.findByIdentifier(animalDto.getIdentifier());

        if (animal == null) {
            throw new IllegalArgumentException("Animal não encontrado: " + animalDto.getIdentifier());
        }

        // 2. Montar AnimalContext
        AnimalContext context = AnimalContext.builder()
                .animalOrigin(animal)
                .stage(animalDto.getStage())
                .milkYield(animalDto.getMilkProductionReal())
                .milkFatPct(animalDto.getFatContent())
                .milkProteinPct(animalDto.getProteinContent())
                .ambientTemperature(null) // se não usar agora
                .build();

        // 3. Chamar Calculator
        return rumenDegradableProteinCalculator.calculateTotalPDR(context);
    }
}
