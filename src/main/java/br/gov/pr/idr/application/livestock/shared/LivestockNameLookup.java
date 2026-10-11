package br.gov.pr.idr.application.livestock.shared;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.breed.BreedID;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Porta de leitura: nomes de exibição das referências de livestock (nome da raça, identificador do animal) para
 * as saídas de consulta. Fica fora dos gateways de domínio, que tratam só da persistência dos agregados.
 *
 * <p>Contrato: cada chamada é uma única busca para a coleção inteira; coleção vazia devolve mapa vazio sem I/O;
 * ids desconhecidos ficam fora do mapa.
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
