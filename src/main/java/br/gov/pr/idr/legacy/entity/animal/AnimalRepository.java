package br.gov.pr.idr.legacy.entity.animal;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalRepository extends JpaRepository<Animal, UUID>, AnimalSpecExecutor {

    Animal findByIdentifier(String identifier);

}
