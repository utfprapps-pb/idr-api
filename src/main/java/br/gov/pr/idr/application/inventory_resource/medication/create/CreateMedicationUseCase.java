package br.gov.pr.idr.application.inventory_resource.medication.create;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.inventory_resource.medication.Medication;
import br.gov.pr.idr.domain.inventory_resource.medication.MedicationGateway;
import br.gov.pr.idr.domain.inventory_resource.product.ProductGateway;
import br.gov.pr.idr.domain.inventory_resource.product.ProductID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotificationException;
import br.gov.pr.idr.domain.shared.tactical.validation.NotificationValidation;

@CommandUseCase
public class CreateMedicationUseCase extends UseCase<CreateMedicationCommand, CreateMedicationOutput> {

    private final MedicationGateway medicationGateway;
    private final ProductGateway productGateway;
    private final AnimalAccessPolicy accessPolicy;

    public CreateMedicationUseCase(final MedicationGateway medicationGateway, final ProductGateway productGateway,
                                    final AnimalAccessPolicy accessPolicy) {
        this.medicationGateway = medicationGateway;
        this.productGateway = productGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public CreateMedicationOutput execute(final CreateMedicationCommand command) {
        final var animal = accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(),
                command.username());

        final var productId = ProductID.from(command.productId());
        final var medication = Medication.create(animal.getId(), productId, command.applicationDate(),
                command.appliedDose(), command.applicationWay());

        if (!productGateway.existsById(productId)) {
            throw new NotificationException("Produto com o id %s não encontrado".formatted(productId.id()),
                    NotificationValidation.create());
        }

        return CreateMedicationOutput.from(medicationGateway.save(medication));
    }
}
