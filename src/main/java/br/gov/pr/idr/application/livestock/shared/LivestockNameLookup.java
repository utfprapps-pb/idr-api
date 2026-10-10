package br.gov.pr.idr.application.livestock.shared;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.breed.BreedID;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Read-side port: display names of livestock references (breed name, animal identifier) for query outputs.
 * Kept out of the domain gateways, which stay about persisting aggregates.
 *
 * <p>Contract: each call is a single lookup for the whole collection; an empty collection returns an empty map
 * without any I/O; unknown ids are absent from the map.
 */
public interface LivestockNameLookup {

    Map<BreedID, String> breedNames(final Collection<BreedID> ids);

    Map<AnimalID, String> animalIdentifiers(final Collection<AnimalID> ids);

    default String breedName(final BreedID id) {
        return breedNames(Set.of(id)).get(id);
    }

    default String animalIdentifier(final AnimalID id) {
        return animalIdentifiers(Set.of(id)).get(id);
    }
}
