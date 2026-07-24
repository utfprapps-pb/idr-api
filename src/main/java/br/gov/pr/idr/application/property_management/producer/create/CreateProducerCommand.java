package br.gov.pr.idr.application.property_management.producer.create;

public record CreateProducerCommand(String name, String cpf) {

    public static CreateProducerCommand from(final String name, final String cpf) {
        return new CreateProducerCommand(name, cpf);
    }
}
