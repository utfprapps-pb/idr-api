package br.gov.pr.idr.application.livestock.pregnancy_diagnose.delete;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.CommandUseCase;
import br.gov.pr.idr.application.shared.stereotype.VoidUseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.pregnancy_diagnose.PregnancyDiagnose;
import br.gov.pr.idr.domain.livestock.pregnancy_diagnose.PregnancyDiagnoseGateway;
import br.gov.pr.idr.domain.livestock.pregnancy_diagnose.PregnancyDiagnoseID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

@CommandUseCase
public class DeletePregnancyDiagnoseUseCase extends VoidUseCase<DeletePregnancyDiagnoseCommand> {

    private final PregnancyDiagnoseGateway pregnancyDiagnoseGateway;
    private final AnimalAccessPolicy accessPolicy;

    public DeletePregnancyDiagnoseUseCase(final PregnancyDiagnoseGateway pregnancyDiagnoseGateway,
                                           final AnimalAccessPolicy accessPolicy) {
        this.pregnancyDiagnoseGateway = pregnancyDiagnoseGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public void execute(final DeletePregnancyDiagnoseCommand command) {
        accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(), command.scope());

        final var id = PregnancyDiagnoseID.from(command.pregnancyDiagnoseId());
        final var pregnancyDiagnose = pregnancyDiagnoseGateway.findByIdAndAnimalId(id, AnimalID.from(command.animalId()))
                .orElseThrow(() -> NotFoundException.with(PregnancyDiagnose.class, id));

        pregnancyDiagnoseGateway.deleteById(pregnancyDiagnose.getId());
    }
}
