package br.gov.pr.idr.infra.livestock.animal_diseases;

import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_diseases.AnimalDiseases;
import br.gov.pr.idr.domain.livestock.animal_diseases.AnimalDiseasesGateway;
import br.gov.pr.idr.domain.livestock.animal_diseases.AnimalDiseasesID;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.livestock.animal_diseases.persistence.AnimalDiseasesJPAEntity;
import br.gov.pr.idr.infra.livestock.animal_diseases.persistence.AnimalDiseasesJPARepository;
import br.gov.pr.idr.infra.shared.support.PageRequestFactory;
import br.gov.pr.idr.infra.shared.support.PaginationFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class AnimalDiseasesPostgresGateway implements AnimalDiseasesGateway {

    private final AnimalDiseasesJPARepository repository;

    @Override
    public AnimalDiseases save(final AnimalDiseases animalDiseases) {
        return repository.save(AnimalDiseasesJPAEntity.from(animalDiseases))
                .toAggregate();
    }

    @Override
    public Optional<AnimalDiseases> findByIdAndAnimalId(final AnimalDiseasesID id, final AnimalID animalId) {
        return repository.findByIdAndAnimalId(id.id(), animalId.id())
                .map(AnimalDiseasesJPAEntity::toAggregate);
    }

    @Override
    public AnimalDiseases update(final AnimalDiseases animalDiseases) {
        return this.save(animalDiseases);
    }

    @Override
    public void deleteById(final AnimalDiseasesID id) {
        repository.findById(id.id()).ifPresent(entity -> {
            entity.markDeleted();
            repository.save(entity);
        });
    }

    @Override
    public Pagination<AnimalDiseases> search(final AnimalID animalId, final SearchQuery query) {
        final var pageRequest = PageRequestFactory.from(query, "diagnosisDate");
        final var page = repository.findAllByAnimalId(animalId.id(), pageRequest);
        return PaginationFactory.from(page, AnimalDiseasesJPAEntity::toAggregate);
    }
}
