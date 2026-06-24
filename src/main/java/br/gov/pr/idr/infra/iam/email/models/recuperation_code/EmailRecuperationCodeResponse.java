package br.gov.pr.idr.infra.iam.email.models.recuperation_code;

public record EmailRecuperationCodeResponse(String message) {

    public static EmailRecuperationCodeResponse ok() {
        return new EmailRecuperationCodeResponse("Email enviado com sucesso!");
    }
}
