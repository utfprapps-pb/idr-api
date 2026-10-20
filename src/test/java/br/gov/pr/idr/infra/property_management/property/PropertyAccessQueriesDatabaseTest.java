package br.gov.pr.idr.infra.property_management.property;

import br.gov.pr.idr.infra.property_management.property.persistence.PropertyJPARepository;
import br.gov.pr.idr.infra.shared.testing.PersistenceAdapterTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Consultas de autorização por propriedade contra o seed: a propriedade 1 fica em Pato Branco, com o técnico 1. */
@PersistenceAdapterTest
@DisplayName("Consultas de acesso à propriedade — Postgres real")
class PropertyAccessQueriesDatabaseTest {

    private static final UUID PROPERTY = UUID.fromString("c0000000-0000-0000-0000-000000000001");
    private static final UUID TECHNICIAN = UUID.fromString("a0000000-0000-0000-0000-000000000011");
    private static final UUID PATO_BRANCO = UUID.fromString("58ac2f17-d95a-457f-85a9-3db07f3f50e6");
    private static final UUID PATO_BRANCO_REGION = UUID.fromString("77f65395-e7ed-4c6b-8678-4668bff690a2");
    private static final UUID APUCARANA = UUID.fromString("0e747009-9abf-4253-89ab-cd1a208d983e");
    private static final UUID APUCARANA_REGION = UUID.fromString("b109136b-2cef-432c-8deb-b1169ffc1de9");

    @Autowired PropertyJPARepository repository;

    @Test
    @DisplayName("técnico: só a propriedade a que está vinculado")
    void technicianScope() {
        assertTrue(repository.existsByIdAndTechnician(PROPERTY, TECHNICIAN));
        assertFalse(repository.existsByIdAndTechnician(PROPERTY, UUID.randomUUID()));
    }

    @Test
    @DisplayName("localização: pela cidade ou pela região da cidade, mesmo com o outro conjunto vazio")
    void locationScope() {
        assertTrue(repository.existsByIdAndLocation(PROPERTY, Set.of(), Set.of(PATO_BRANCO)));
        assertTrue(repository.existsByIdAndLocation(PROPERTY, Set.of(PATO_BRANCO_REGION), Set.of()));
        assertFalse(repository.existsByIdAndLocation(PROPERTY, Set.of(APUCARANA_REGION), Set.of(APUCARANA)));
        assertFalse(repository.existsByIdAndLocation(UUID.randomUUID(), Set.of(PATO_BRANCO_REGION),
                Set.of(PATO_BRANCO)));
    }
}
