package br.gov.pr.idr.application.inventory_resource.medication.retrieve.get;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.inventory_resource.medication.Medication;
import br.gov.pr.idr.domain.inventory_resource.medication.MedicationGateway;
import br.gov.pr.idr.domain.inventory_resource.medication.MedicationID;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

@QueryUseCase
public class GetMedicationByIdUseCase extends UseCase<GetMedicationByIdCommand, GetMedicationByIdOutput> {

    private final MedicationGateway medicationGateway;
    private final AnimalAccessPolicy accessPolicy;

    public GetMedicationByIdUseCase(final MedicationGateway medicationGateway,
                                     final AnimalAccessPolicy accessPolicy) {
        this.medicationGateway = medicationGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public GetMedicationByIdOutput execute(final GetMedicationByIdCommand command) {
        accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(), command.username());

        final var id = MedicationID.from(command.medicationId());
        final var medication = medicationGateway.findById(id)
                .filter(m -> m.getAnimalId().equals(AnimalID.from(command.animalId())))
                .orElseThrow(() -> NotFoundException.with(Medication.class, id));

        return GetMedicationByIdOutput.from(medication);
    }
}
