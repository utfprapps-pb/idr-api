package br.gov.pr.idr.application.livestock.animal_childbirth.retrieve.list;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.livestock.shared.LivestockNameLookup;
import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirth;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirthGateway;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;

import java.util.stream.Collectors;

@QueryUseCase
public class ListAnimalChildbirthUseCase extends UseCase<ListAnimalChildbirthCommand,
        Pagination<ListAnimalChildbirthOutput>> {

    private final AnimalChildbirthGateway animalChildbirthGateway;
    private final LivestockNameLookup nameLookup;
    private final AnimalAccessPolicy accessPolicy;

    public ListAnimalChildbirthUseCase(final AnimalChildbirthGateway animalChildbirthGateway,
                                       final LivestockNameLookup nameLookup,
                                       final AnimalAccessPolicy accessPolicy) {
        this.animalChildbirthGateway = animalChildbirthGateway;
        this.nameLookup = nameLookup;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public Pagination<ListAnimalChildbirthOutput> execute(final ListAnimalChildbirthCommand command) {
        accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(), command.username());
        final var page = animalChildbirthGateway.search(AnimalID.from(command.animalId()), command.query());

        final var breedNames = nameLookup.breedNames(
                page.items().stream().map(AnimalChildbirth::getBreedId).collect(Collectors.toSet()));
        final var calfIdentifiers = nameLookup.animalIdentifiers(
                page.items().stream().flatMap(childbirth -> childbirth.getCalfId().stream()).collect(Collectors.toSet()));
        return page.map(childbirth -> ListAnimalChildbirthOutput.from(childbirth,
                breedNames.get(childbirth.getBreedId()),
                childbirth.getCalfId().map(calfIdentifiers::get).orElse(null)));
    }
}
