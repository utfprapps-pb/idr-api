package br.gov.pr.idr.application.iam.email.recuperation_code;

public record SendEmailRecuperationCodeCommand(String email) {

    public static SendEmailRecuperationCodeCommand from(final String email) {
        return new SendEmailRecuperationCodeCommand(email);
    }
}
