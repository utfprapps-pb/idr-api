package br.gov.pr.idr.infra.inventory_resource.medication;

import br.gov.pr.idr.domain.inventory_resource.medication.Medication;
import br.gov.pr.idr.domain.inventory_resource.medication.MedicationGateway;
import br.gov.pr.idr.domain.inventory_resource.medication.MedicationID;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.inventory_resource.medication.persistence.MedicationJPAEntity;
import br.gov.pr.idr.infra.inventory_resource.medication.persistence.MedicationJPARepository;
import br.gov.pr.idr.infra.shared.support.PageRequestFactory;
import br.gov.pr.idr.infra.shared.support.PaginationFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class MedicationPostgresGateway implements MedicationGateway {

    private final MedicationJPARepository repository;

    @Override
    public Medication save(final Medication medication) {
        return repository.save(MedicationJPAEntity.from(medication))
                .toAggregate();
    }

    @Override
    public Optional<Medication> findById(final MedicationID id) {
        return repository.findById(id.id())
                .map(MedicationJPAEntity::toAggregate);
    }

    @Override
    public Medication update(final Medication medication) {
        return this.save(medication);
    }

    @Override
    public void deleteById(final MedicationID id) {
        repository.findById(id.id()).ifPresent(entity -> {
            entity.markDeleted();
            repository.save(entity);
        });
    }

    @Override
    public Pagination<Medication> search(final AnimalID animalId, final SearchQuery query) {
        final var pageRequest = PageRequestFactory.from(query, "applicationDate");
        final var page = repository.findAllByAnimalId(animalId.id(), pageRequest);
        return PaginationFactory.from(page, MedicationJPAEntity::toAggregate);
    }
}
