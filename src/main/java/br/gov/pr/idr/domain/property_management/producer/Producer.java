package br.gov.pr.idr.domain.property_management.producer;

import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.shared.tactical.AggregateRoot;
import br.gov.pr.idr.domain.shared.tactical.validation.DomainError;
import br.gov.pr.idr.domain.shared.tactical.validation.ValidationHandler;

import java.time.Instant;

public class Producer extends AggregateRoot<ProducerID> {

    private String name;
    private CPF cpf;
    private final Long version;
    private final Instant updatedAt;

    protected Producer(final ProducerID id, final String name, final CPF cpf,
                       final Long version, final Instant updatedAt) {
        super(id);
        this.name = name;
        this.cpf = cpf;
        this.version = version;
        this.updatedAt = updatedAt;
    }

    public static Producer create(final String name, final CPF cpf) {
        final var producer = new Producer(ProducerID.unique(), name, cpf, null, null);
        producer.selfValidate();
        return producer;
    }

    public static Producer with(final ProducerID id, final String name, final CPF cpf) {
        return new Producer(id, name, cpf, null, null);
    }

    public static Producer with(final ProducerID id, final String name, final CPF cpf,
                                final Long version, final Instant updatedAt) {
        return new Producer(id, name, cpf, version, updatedAt);
    }

    public Producer update(final String name, final CPF cpf) {
        this.name = name;
        this.cpf = cpf;
        selfValidate();
        return this;
    }

    @Override
    public void validate(final ValidationHandler handler) {
        if (this.name == null || this.name.isBlank()) {
            handler.append(DomainError.from("Nome do produtor não pode ser vazio"));
        }
    }

    public String getName() { return name; }

    public CPF getCpf() { return cpf; }

    public Long getVersion() { return version; }

    public Instant getUpdatedAt() { return updatedAt; }
}
