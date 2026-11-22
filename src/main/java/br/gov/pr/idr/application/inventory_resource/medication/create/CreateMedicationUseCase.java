package br.gov.pr.idr.application.inventory_resource.medication.create;

import br.gov.pr.idr.application.inventory_resource.medication.shared.MedicationProductPolicy;
import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.inventory_resource.active_principle.ActivePrincipleGateway;
import br.gov.pr.idr.domain.inventory_resource.medication.Medication;
import br.gov.pr.idr.domain.inventory_resource.medication.MedicationGateway;
import br.gov.pr.idr.domain.inventory_resource.product.ProductGateway;

@CommandUseCase
public class CreateMedicationUseCase extends UseCase<CreateMedicationCommand, CreateMedicationOutput> {

    private final MedicationGateway medicationGateway;
    private final ProductGateway productGateway;
    private final ActivePrincipleGateway activePrincipleGateway;
    private final AnimalAccessPolicy accessPolicy;

    public CreateMedicationUseCase(final MedicationGateway medicationGateway, final ProductGateway productGateway,
                                    final ActivePrincipleGateway activePrincipleGateway,
                                    final AnimalAccessPolicy accessPolicy) {
        this.medicationGateway = medicationGateway;
        this.productGateway = productGateway;
        this.activePrincipleGateway = activePrincipleGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public CreateMedicationOutput execute(final CreateMedicationCommand command) {
        final var animal = accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(),
                command.scope());

        final var product = MedicationProductPolicy.resolve(productGateway, activePrincipleGateway,
                command.productId(), command.productName(), command.activePrinciple());
        final var medication = Medication.create(animal.getId(), product.productId(), product.productName(),
                product.activePrinciple(), command.applicationDate(), command.appliedDose(),
                command.applicationWay());

        return CreateMedicationOutput.from(medicationGateway.save(medication));
    }
}
