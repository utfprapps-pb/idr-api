package br.gov.pr.idr.infra.livestock.shared;

import br.gov.pr.idr.application.livestock.shared.LivestockNameLookup;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.infra.livestock.animal.persistence.AnimalJPARepository;
import br.gov.pr.idr.infra.livestock.animal.persistence.AnimalJPARepository.AnimalIdentifierView;
import br.gov.pr.idr.infra.livestock.breed.persistence.BreedJPARepository;
import br.gov.pr.idr.infra.livestock.breed.persistence.BreedJPARepository.BreedNameView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/** Atende {@link LivestockNameLookup} com projeções de duas colunas, sem carregar agregados. */
@RequiredArgsConstructor
@Component
public class PostgresLivestockNameLookup implements LivestockNameLookup {

    private final BreedJPARepository breedRepository;
    private final AnimalJPARepository animalRepository;

    @Override
    public Map<BreedID, String> breedNames(final Collection<BreedID> ids) {
        if (ids.isEmpty()) {
            return Map.of();
        }
        return breedRepository.findByIdIn(ids.stream().map(BreedID::id).toList()).stream()
                .collect(Collectors.toMap(view -> BreedID.from(view.getId()), BreedNameView::getBreedName));
    }

    @Override
    public Map<AnimalID, String> animalIdentifiers(final Collection<AnimalID> ids) {
        if (ids.isEmpty()) {
            return Map.of();
        }
        return animalRepository.findByIdIn(ids.stream().map(AnimalID::id).toList()).stream()
                .collect(Collectors.toMap(view -> AnimalID.from(view.getId()), AnimalIdentifierView::getIdentifier));
    }
}
