package br.gov.pr.idr.application.livestock.animal_diseases.update;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_diseases.AnimalDiseases;
import br.gov.pr.idr.domain.livestock.animal_diseases.AnimalDiseasesGateway;
import br.gov.pr.idr.domain.livestock.animal_diseases.AnimalDiseasesID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

@CommandUseCase
public class UpdateAnimalDiseasesUseCase extends UseCase<UpdateAnimalDiseasesCommand,
        UpdateAnimalDiseasesOutput> {

    private final AnimalDiseasesGateway animalDiseasesGateway;
    private final AnimalAccessPolicy accessPolicy;

    public UpdateAnimalDiseasesUseCase(final AnimalDiseasesGateway animalDiseasesGateway,
                                        final AnimalAccessPolicy accessPolicy) {
        this.animalDiseasesGateway = animalDiseasesGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public UpdateAnimalDiseasesOutput execute(final UpdateAnimalDiseasesCommand command) {
        accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(), command.scope());

        final var id = AnimalDiseasesID.from(command.animalDiseasesId());
        final var animalDiseases = animalDiseasesGateway.findByIdAndAnimalId(id, AnimalID.from(command.animalId()))
                .orElseThrow(() -> NotFoundException.with(AnimalDiseases.class, id));

        final var updatedAnimalDiseases = animalDiseases.update(command.diagnosis(), command.diagnosisDate());

        return UpdateAnimalDiseasesOutput.from(animalDiseasesGateway.update(updatedAnimalDiseases));
    }
}
