package br.gov.pr.idr.application.livestock.animal_diseases.retrieve.get;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_diseases.AnimalDiseases;
import br.gov.pr.idr.domain.livestock.animal_diseases.AnimalDiseasesGateway;
import br.gov.pr.idr.domain.livestock.animal_diseases.AnimalDiseasesID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

@QueryUseCase
public class GetAnimalDiseasesByIdUseCase extends UseCase<GetAnimalDiseasesByIdCommand,
        GetAnimalDiseasesByIdOutput> {

    private final AnimalDiseasesGateway animalDiseasesGateway;
    private final AnimalAccessPolicy accessPolicy;

    public GetAnimalDiseasesByIdUseCase(final AnimalDiseasesGateway animalDiseasesGateway,
                                         final AnimalAccessPolicy accessPolicy) {
        this.animalDiseasesGateway = animalDiseasesGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public GetAnimalDiseasesByIdOutput execute(final GetAnimalDiseasesByIdCommand command) {
        accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(), command.username());

        final var id = AnimalDiseasesID.from(command.animalDiseasesId());
        final var animalDiseases = animalDiseasesGateway.findById(id)
                .filter(p -> p.getAnimalId().equals(AnimalID.from(command.animalId())))
                .orElseThrow(() -> NotFoundException.with(AnimalDiseases.class, id));

        return GetAnimalDiseasesByIdOutput.from(animalDiseases);
    }
}
