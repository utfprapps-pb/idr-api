package br.gov.pr.idr.application.livestock.pregnancy_diagnose.retrieve.get;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.pregnancy_diagnose.PregnancyDiagnose;
import br.gov.pr.idr.domain.livestock.pregnancy_diagnose.PregnancyDiagnoseGateway;
import br.gov.pr.idr.domain.livestock.pregnancy_diagnose.PregnancyDiagnoseID;
import br.gov.pr.idr.domain.shared.tactical.exceptions.NotFoundException;

@QueryUseCase
public class GetPregnancyDiagnoseByIdUseCase extends UseCase<GetPregnancyDiagnoseByIdCommand,
        GetPregnancyDiagnoseByIdOutput> {

    private final PregnancyDiagnoseGateway pregnancyDiagnoseGateway;
    private final AnimalAccessPolicy accessPolicy;

    public GetPregnancyDiagnoseByIdUseCase(final PregnancyDiagnoseGateway pregnancyDiagnoseGateway,
                                            final AnimalAccessPolicy accessPolicy) {
        this.pregnancyDiagnoseGateway = pregnancyDiagnoseGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public GetPregnancyDiagnoseByIdOutput execute(final GetPregnancyDiagnoseByIdCommand command) {
        accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(), command.scope());

        final var id = PregnancyDiagnoseID.from(command.pregnancyDiagnoseId());
        final var pregnancyDiagnose = pregnancyDiagnoseGateway.findByIdAndAnimalId(id, AnimalID.from(command.animalId()))
                .orElseThrow(() -> NotFoundException.with(PregnancyDiagnose.class, id));

        return GetPregnancyDiagnoseByIdOutput.from(pregnancyDiagnose);
    }
}
