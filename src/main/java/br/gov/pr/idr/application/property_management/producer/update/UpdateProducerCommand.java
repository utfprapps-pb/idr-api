package br.gov.pr.idr.application.property_management.producer.update;

import java.util.UUID;

public record UpdateProducerCommand(UUID id, String name, String cpf) {

    public static UpdateProducerCommand from(final UUID id, final String name, final String cpf) {
        return new UpdateProducerCommand(id, name, cpf);
    }
}
