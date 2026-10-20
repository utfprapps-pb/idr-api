package br.gov.pr.idr.infra.livestock.pregnancy_diagnose;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.pregnancy_diagnose.PregnancyDiagnose;
import br.gov.pr.idr.domain.livestock.pregnancy_diagnose.PregnancyDiagnoseGateway;
import br.gov.pr.idr.domain.livestock.pregnancy_diagnose.PregnancyDiagnoseID;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.livestock.pregnancy_diagnose.persistence.PregnancyDiagnoseJPAEntity;
import br.gov.pr.idr.infra.livestock.pregnancy_diagnose.persistence.PregnancyDiagnoseJPARepository;
import br.gov.pr.idr.infra.shared.support.PageRequestFactory;
import br.gov.pr.idr.infra.shared.support.PaginationFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class PregnancyDiagnosePostgresGateway implements PregnancyDiagnoseGateway {

    private final PregnancyDiagnoseJPARepository repository;

    @Override
    public PregnancyDiagnose save(final PregnancyDiagnose pregnancyDiagnose) {
        return repository.save(PregnancyDiagnoseJPAEntity.from(pregnancyDiagnose))
                .toAggregate();
    }

    @Override
    public Optional<PregnancyDiagnose> findByIdAndAnimalId(final PregnancyDiagnoseID id, final AnimalID animalId) {
        return repository.findByIdAndAnimalId(id.id(), animalId.id())
                .map(PregnancyDiagnoseJPAEntity::toAggregate);
    }

    @Override
    public PregnancyDiagnose update(final PregnancyDiagnose pregnancyDiagnose) {
        return this.save(pregnancyDiagnose);
    }

    @Override
    public void deleteById(final PregnancyDiagnoseID id) {
        repository.findById(id.id()).ifPresent(entity -> {
            entity.markDeleted();
            repository.save(entity);
        });
    }

    @Override
    public Pagination<PregnancyDiagnose> search(final AnimalID animalId, final SearchQuery query) {
        final var pageRequest = PageRequestFactory.from(query, "diagnoseDate");
        final var page = repository.findAllByAnimalId(animalId.id(), pageRequest);
        return PaginationFactory.from(page, PregnancyDiagnoseJPAEntity::toAggregate);
    }
}
