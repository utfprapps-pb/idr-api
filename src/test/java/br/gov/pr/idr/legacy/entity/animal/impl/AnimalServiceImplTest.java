package br.gov.pr.idr.legacy.entity.animal.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import br.gov.pr.idr.legacy.entity.animal.Animal;
import br.gov.pr.idr.legacy.entity.animal.AnimalRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("AnimalServiceImpl")
class AnimalServiceImplTest {

    @Mock
    AnimalRepository animalRepository;

    @InjectMocks
    AnimalServiceImpl service;

    @Test
    @DisplayName("update preserva o propertyId existente mesmo enviando outro no lugar")
    void updatePreservesExistingPropertyIdEvenWhenBodySendsAnother() {
        UUID id = UUID.randomUUID();
        UUID originalPropertyId = UUID.randomUUID();
        UUID otherPropertyId = UUID.randomUUID();

        Animal existing = new Animal();
        existing.setId(id);
        existing.setPropertyId(originalPropertyId);
        existing.setIdentifier("Original");

        Animal incoming = new Animal();
        incoming.setPropertyId(otherPropertyId);
        incoming.setIdentifier("Atualizado");

        when(animalRepository.findById(id)).thenReturn(Optional.of(existing));

        Animal result = service.update(id, incoming);

        assertEquals(originalPropertyId, result.getPropertyId());
        assertEquals("Atualizado", result.getIdentifier());
    }

    @Test
    @DisplayName("update lança IllegalArgumentException quando o animal não existe")
    void updateThrowsWhenAnimalDoesNotExist() {
        UUID id = UUID.randomUUID();
        Animal incoming = new Animal();
        when(animalRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.update(id, incoming));
    }
}
