package br.gov.pr.idr.application.livestock.animal_diseases.create;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal_diseases.AnimalDiseases;
import br.gov.pr.idr.domain.livestock.animal_diseases.AnimalDiseasesGateway;

@CommandUseCase
public class CreateAnimalDiseasesUseCase extends UseCase<CreateAnimalDiseasesCommand,
        CreateAnimalDiseasesOutput> {

    private final AnimalDiseasesGateway animalDiseasesGateway;
    private final AnimalAccessPolicy accessPolicy;

    public CreateAnimalDiseasesUseCase(final AnimalDiseasesGateway animalDiseasesGateway,
                                        final AnimalAccessPolicy accessPolicy) {
        this.animalDiseasesGateway = animalDiseasesGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public CreateAnimalDiseasesOutput execute(final CreateAnimalDiseasesCommand command) {
        final var animal = accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(),
                command.username());

        final var animalDiseases = AnimalDiseases.create(animal.getId(), command.diagnosis(),
                command.diagnosisDate());

        return CreateAnimalDiseasesOutput.from(animalDiseasesGateway.save(animalDiseases));
    }
}
