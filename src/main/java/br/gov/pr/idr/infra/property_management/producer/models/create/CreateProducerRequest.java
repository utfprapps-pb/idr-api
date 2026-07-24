package br.gov.pr.idr.infra.property_management.producer.models.create;

import jakarta.validation.constraints.NotBlank;

public record CreateProducerRequest(
        @NotBlank(message = "Nome não pode ser vazio") String name,
        @NotBlank(message = "CPF não pode ser vazio") String cpf
) {}
