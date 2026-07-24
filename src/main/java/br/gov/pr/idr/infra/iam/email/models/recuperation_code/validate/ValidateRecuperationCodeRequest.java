package br.gov.pr.idr.infra.iam.email.models.recuperation_code.validate;

public record ValidateRecuperationCodeRequest(String email, String code) {
}
