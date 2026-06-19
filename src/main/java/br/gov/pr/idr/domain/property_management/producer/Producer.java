package br.gov.pr.idr.domain.property_management.producer;

import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.shared.AggregateRoot;
import br.gov.pr.idr.domain.shared.validation.DomainError;
import br.gov.pr.idr.domain.shared.validation.ValidationHandler;

public class Producer extends AggregateRoot<ProducerID> {

    private String name;
    private CPF cpf;

    protected Producer(final ProducerID id, final String name, final CPF cpf) {
        super(id);
        this.name = name;
        this.cpf = cpf;
        selfValidate();
    }

    public static Producer create(final String name, final CPF cpf) {
        return new Producer(ProducerID.unique(), name, cpf);
    }

    public static Producer with(final ProducerID id, final String name, final CPF cpf) {
        return new Producer(id, name, cpf);
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
}
