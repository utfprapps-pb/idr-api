package br.gov.pr.idr.legacy.entity.animal;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalRepository extends JpaRepository<Animal, Long>, AnimalSpecExecutor {

    Animal findByIdentifier(String identifier);

}
