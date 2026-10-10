package br.gov.pr.idr.application.livestock.animal_childbirth.retrieve.get;

import br.gov.pr.idr.application.livestock.animal_childbirth.ChildbirthReferencePolicy;
import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalGateway;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirth;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirthGateway;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirthID;
import br.gov.pr.idr.domain.livestock.breed.BreedGateway;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

import java.util.List;

@QueryUseCase
public class GetAnimalChildbirthByIdUseCase extends UseCase<GetAnimalChildbirthByIdCommand,
        GetAnimalChildbirthByIdOutput> {

    private final AnimalChildbirthGateway animalChildbirthGateway;
    private final BreedGateway breedGateway;
    private final AnimalGateway animalGateway;
    private final AnimalAccessPolicy accessPolicy;

    public GetAnimalChildbirthByIdUseCase(final AnimalChildbirthGateway animalChildbirthGateway,
                                          final BreedGateway breedGateway, final AnimalGateway animalGateway,
                                          final AnimalAccessPolicy accessPolicy) {
        this.animalChildbirthGateway = animalChildbirthGateway;
        this.breedGateway = breedGateway;
        this.animalGateway = animalGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public GetAnimalChildbirthByIdOutput execute(final GetAnimalChildbirthByIdCommand command) {
        accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(), command.username());
        final var id = AnimalChildbirthID.from(command.animalChildbirthId());
        final var animalChildbirth = animalChildbirthGateway.findById(id)
                .filter(p -> p.getAnimalId().equals(AnimalID.from(command.animalId())))
                .orElseThrow(() -> NotFoundException.with(AnimalChildbirth.class, id));

        final var breedNames = ChildbirthReferencePolicy.breedNames(breedGateway, List.of(animalChildbirth));
        final var calfIdentifiers = ChildbirthReferencePolicy.calfIdentifiers(animalGateway, List.of(animalChildbirth));
        return GetAnimalChildbirthByIdOutput.from(animalChildbirth,
                breedNames.get(animalChildbirth.getBreedId()),
                animalChildbirth.getCalfId().map(calfIdentifiers::get).orElse(null));
    }
}
