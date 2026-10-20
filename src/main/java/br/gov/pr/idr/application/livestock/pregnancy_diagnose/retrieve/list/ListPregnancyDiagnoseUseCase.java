package br.gov.pr.idr.application.livestock.pregnancy_diagnose.retrieve.list;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.shared.stereotype.QueryUseCase;
import br.gov.pr.idr.application.shared.stereotype.UseCase;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.pregnancy_diagnose.PregnancyDiagnoseGateway;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;

@QueryUseCase
public class ListPregnancyDiagnoseUseCase extends UseCase<ListPregnancyDiagnoseCommand,
        Pagination<ListPregnancyDiagnoseOutput>> {

    private final PregnancyDiagnoseGateway pregnancyDiagnoseGateway;
    private final AnimalAccessPolicy accessPolicy;

    public ListPregnancyDiagnoseUseCase(final PregnancyDiagnoseGateway pregnancyDiagnoseGateway,
                                         final AnimalAccessPolicy accessPolicy) {
        this.pregnancyDiagnoseGateway = pregnancyDiagnoseGateway;
        this.accessPolicy = accessPolicy;
    }

    @Override
    public Pagination<ListPregnancyDiagnoseOutput> execute(final ListPregnancyDiagnoseCommand command) {
        accessPolicy.ensureAccessToAnimal(command.propertyId(), command.animalId(), command.scope());

        return pregnancyDiagnoseGateway.search(AnimalID.from(command.animalId()), command.query())
                .map(ListPregnancyDiagnoseOutput::from);
    }
}
