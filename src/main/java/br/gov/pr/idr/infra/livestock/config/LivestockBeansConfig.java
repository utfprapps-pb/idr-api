package br.gov.pr.idr.infra.livestock.config;

import br.gov.pr.idr.application.livestock.shared.AnimalAccessPolicy;
import br.gov.pr.idr.application.livestock.shared.LivestockReferencePolicy;
import br.gov.pr.idr.domain.iam.user.UserGateway;
import br.gov.pr.idr.domain.livestock.animal.AnimalGateway;
import br.gov.pr.idr.domain.livestock.breed.BreedGateway;
import br.gov.pr.idr.domain.property_management.property.PropertyGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LivestockBeansConfig {

    @Bean
    public AnimalAccessPolicy animalAccessPolicy(final PropertyGateway propertyGateway,
                                                   final UserGateway userGateway,
                                                   final AnimalGateway animalGateway) {
        return new AnimalAccessPolicy(propertyGateway, userGateway, animalGateway);
    }

    @Bean
    public LivestockReferencePolicy livestockReferencePolicy(final BreedGateway breedGateway,
                                                             final AnimalGateway animalGateway) {
        return new LivestockReferencePolicy(breedGateway, animalGateway);
    }
}
