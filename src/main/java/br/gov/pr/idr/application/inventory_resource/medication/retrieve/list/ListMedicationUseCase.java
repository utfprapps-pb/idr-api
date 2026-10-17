package br.gov.pr.idr.application.inventory_resource.medication.retrieve.list;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.inventory_resource.medication.MedicationGateway;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;

@QueryUseCase
public class ListMedicationUseCase extends UseCase<ListMedicationCommand, Pagination<ListMedicationOutput>> {

    private final MedicationGateway medicationGateway;
    private final AnimalAccessPolicy accessPolicy;

    public ListMedicationUseCase(final MedicationGateway medicationGateway,
                                  final AnimalAccessPolicy accessPolicy) {
        this.medicationGateway = medicationGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public Pagination<ListMedicationOutput> execute(final ListMedicationCommand command) {
        accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(), command.username());

        return medicationGateway.search(AnimalID.from(command.animalId()), command.query())
                .map(ListMedicationOutput::from);
    }
}
