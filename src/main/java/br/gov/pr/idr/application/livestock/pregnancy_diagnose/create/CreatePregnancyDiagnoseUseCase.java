package br.gov.pr.idr.application.livestock.pregnancy_diagnose.create;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.pregnancy_diagnose.PregnancyDiagnose;
import br.gov.pr.idr.domain.livestock.pregnancy_diagnose.PregnancyDiagnoseGateway;

@CommandUseCase
public class CreatePregnancyDiagnoseUseCase extends UseCase<CreatePregnancyDiagnoseCommand,
        CreatePregnancyDiagnoseOutput> {

    private final PregnancyDiagnoseGateway pregnancyDiagnoseGateway;
    private final AnimalAccessPolicy accessPolicy;

    public CreatePregnancyDiagnoseUseCase(final PregnancyDiagnoseGateway pregnancyDiagnoseGateway,
                                           final AnimalAccessPolicy accessPolicy) {
        this.pregnancyDiagnoseGateway = pregnancyDiagnoseGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public CreatePregnancyDiagnoseOutput execute(final CreatePregnancyDiagnoseCommand command) {
        final var animal = accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(),
                command.username());

        final var pregnancyDiagnose = PregnancyDiagnose.create(animal.getId(), command.diagnoseDate(),
                command.lastIa());

        return CreatePregnancyDiagnoseOutput.from(pregnancyDiagnoseGateway.save(pregnancyDiagnose));
    }
}
