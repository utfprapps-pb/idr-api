package br.gov.pr.idr.application.livestock.pregnancy_diagnose.update;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.pregnancy_diagnose.PregnancyDiagnose;
import br.gov.pr.idr.domain.livestock.pregnancy_diagnose.PregnancyDiagnoseGateway;
import br.gov.pr.idr.domain.livestock.pregnancy_diagnose.PregnancyDiagnoseID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

@CommandUseCase
public class UpdatePregnancyDiagnoseUseCase extends UseCase<UpdatePregnancyDiagnoseCommand,
        UpdatePregnancyDiagnoseOutput> {

    private final PregnancyDiagnoseGateway pregnancyDiagnoseGateway;
    private final AnimalAccessPolicy accessPolicy;

    public UpdatePregnancyDiagnoseUseCase(final PregnancyDiagnoseGateway pregnancyDiagnoseGateway,
                                           final AnimalAccessPolicy accessPolicy) {
        this.pregnancyDiagnoseGateway = pregnancyDiagnoseGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public UpdatePregnancyDiagnoseOutput execute(final UpdatePregnancyDiagnoseCommand command) {
        accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(), command.username());

        final var id = PregnancyDiagnoseID.from(command.pregnancyDiagnoseId());
        final var pregnancyDiagnose = pregnancyDiagnoseGateway.findById(id)
                .filter(p -> p.getAnimalId().equals(AnimalID.from(command.animalId())))
                .orElseThrow(() -> NotFoundException.with(PregnancyDiagnose.class, id));

        final var updatedPregnancyDiagnose = pregnancyDiagnose.update(command.diagnoseDate(), command.lastIa());

        return UpdatePregnancyDiagnoseOutput.from(pregnancyDiagnoseGateway.update(updatedPregnancyDiagnose));
    }
}
