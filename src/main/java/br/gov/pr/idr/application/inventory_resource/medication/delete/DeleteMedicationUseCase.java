package br.gov.pr.idr.application.inventory_resource.medication.delete;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.VoidUseCase;
import br.gov.pr.idr.domain.inventory_resource.medication.Medication;
import br.gov.pr.idr.domain.inventory_resource.medication.MedicationGateway;
import br.gov.pr.idr.domain.inventory_resource.medication.MedicationID;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

@CommandUseCase
public class DeleteMedicationUseCase extends VoidUseCase<DeleteMedicationCommand> {

    private final MedicationGateway medicationGateway;
    private final AnimalAccessPolicy accessPolicy;

    public DeleteMedicationUseCase(final MedicationGateway medicationGateway,
                                    final AnimalAccessPolicy accessPolicy) {
        this.medicationGateway = medicationGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public void execute(final DeleteMedicationCommand command) {
        accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(), command.username());

        final var id = MedicationID.from(command.medicationId());
        final var medication = medicationGateway.findById(id)
                .filter(m -> m.getAnimalId().equals(AnimalID.from(command.animalId())))
                .orElseThrow(() -> NotFoundException.with(Medication.class, id));

        medicationGateway.deleteById(medication.getId());
    }
}
