package br.gov.pr.idr.application.livestock.animal_childbirth.retrieve.get;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.livestock.shared.LivestockNameLookup;
import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirth;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirthGateway;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirthID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;


@QueryUseCase
public class GetAnimalChildbirthByIdUseCase extends UseCase<GetAnimalChildbirthByIdCommand,
        GetAnimalChildbirthByIdOutput> {

    private final AnimalChildbirthGateway animalChildbirthGateway;
    private final LivestockNameLookup nameLookup;
    private final AnimalAccessPolicy accessPolicy;

    public GetAnimalChildbirthByIdUseCase(final AnimalChildbirthGateway animalChildbirthGateway,
                                          final LivestockNameLookup nameLookup,
                                          final AnimalAccessPolicy accessPolicy) {
        this.animalChildbirthGateway = animalChildbirthGateway;
        this.nameLookup = nameLookup;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public GetAnimalChildbirthByIdOutput execute(final GetAnimalChildbirthByIdCommand command) {
        accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(), command.username());
        final var id = AnimalChildbirthID.from(command.animalChildbirthId());
        final var animalChildbirth = animalChildbirthGateway.findByIdAndAnimalId(id, AnimalID.from(command.animalId()))
                .orElseThrow(() -> NotFoundException.with(AnimalChildbirth.class, id));

        return GetAnimalChildbirthByIdOutput.from(animalChildbirth,
                nameLookup.breedName(animalChildbirth.getBreedId()),
                animalChildbirth.getCalfId().map(nameLookup::animalIdentifier).orElse(null));
    }
}
