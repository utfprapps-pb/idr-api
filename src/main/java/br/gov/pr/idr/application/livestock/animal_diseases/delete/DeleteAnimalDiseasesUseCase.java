package br.gov.pr.idr.application.livestock.animal_diseases.delete;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.VoidUseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_diseases.AnimalDiseases;
import br.gov.pr.idr.domain.livestock.animal_diseases.AnimalDiseasesGateway;
import br.gov.pr.idr.domain.livestock.animal_diseases.AnimalDiseasesID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

@CommandUseCase
public class DeleteAnimalDiseasesUseCase extends VoidUseCase<DeleteAnimalDiseasesCommand> {

    private final AnimalDiseasesGateway animalDiseasesGateway;
    private final AnimalAccessPolicy accessPolicy;

    public DeleteAnimalDiseasesUseCase(final AnimalDiseasesGateway animalDiseasesGateway,
                                        final AnimalAccessPolicy accessPolicy) {
        this.animalDiseasesGateway = animalDiseasesGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public void execute(final DeleteAnimalDiseasesCommand command) {
        accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(), command.username());

        final var id = AnimalDiseasesID.from(command.animalDiseasesId());
        final var animalDiseases = animalDiseasesGateway.findById(id)
                .filter(p -> p.getAnimalId().equals(AnimalID.from(command.animalId())))
                .orElseThrow(() -> NotFoundException.with(AnimalDiseases.class, id));

        animalDiseasesGateway.deleteById(animalDiseases.getId());
    }
}
