package br.gov.pr.idr.infra.livestock.animal;

import br.gov.pr.idr.domain.livestock.animal.Animal;
import br.gov.pr.idr.domain.livestock.animal.AnimalGateway;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.property_management.property.PropertyID;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.livestock.animal.persistence.AnimalJPAEntity;
import br.gov.pr.idr.infra.livestock.animal.persistence.AnimalJPARepository;
import br.gov.pr.idr.infra.livestock.animal.persistence.AnimalJPARepository.AnimalIdentifierView;
import br.gov.pr.idr.infra.shared.support.PageRequestFactory;
import br.gov.pr.idr.infra.shared.support.PaginationFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class AnimalPostgresGateway implements AnimalGateway {

    private final AnimalJPARepository repository;

    @Override
    public Animal save(final Animal animal) {
        return repository.save(AnimalJPAEntity.from(animal))
                .toAggregate();
    }

    @Override
    public Optional<Animal> findById(final AnimalID id) {
        return repository.findById(id.id())
                .map(AnimalJPAEntity::toAggregate);
    }

    @Override
    public Map<AnimalID, String> findIdentifiersByIds(final Collection<AnimalID> ids) {
        if (ids.isEmpty()) {
            return Map.of();
        }
        return repository.findByIdIn(ids.stream().map(AnimalID::id).toList()).stream()
                .collect(Collectors.toMap(view -> AnimalID.from(view.getId()),
                        AnimalIdentifierView::getIdentifier));
    }

    @Override
    public Animal update(final Animal animal) {
        return this.save(animal);
    }

    @Override
    public void deleteById(final AnimalID id) {
        repository.findById(id.id()).ifPresent(entity -> {
            entity.markDeleted();
            repository.save(entity);
        });
    }

    @Override
    public Pagination<Animal> search(final PropertyID propertyId, final SearchQuery query) {
        final var terms = PageRequestFactory.terms(query.terms());
        final var pageRequest = PageRequestFactory.from(query, "identifier");
        final var page = repository.findAllByPropertyIdAndIdentifierContainingIgnoreCase(propertyId.id(), terms,
                pageRequest);
        return PaginationFactory.from(page, AnimalJPAEntity::toAggregate);
    }

    @Override
    public Optional<Animal> findByPropertyIdAndIdentifier(final PropertyID propertyId, final String identifier) {
        return repository.findByPropertyIdAndIdentifier(propertyId.id(), identifier)
                .map(AnimalJPAEntity::toAggregate);
    }
}
