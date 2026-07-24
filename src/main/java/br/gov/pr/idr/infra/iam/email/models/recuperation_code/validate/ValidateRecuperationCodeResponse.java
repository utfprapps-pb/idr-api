package br.gov.pr.idr.infra.iam.email.models.recuperation_code.validate;

public record ValidateRecuperationCodeResponse(String message) {

    public static ValidateRecuperationCodeResponse ok() {
        return new ValidateRecuperationCodeResponse("Código validado com sucesso!");
    }
}
