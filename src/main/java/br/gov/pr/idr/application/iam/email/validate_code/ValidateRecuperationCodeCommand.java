package br.gov.pr.idr.application.iam.email.validate_code;

public record ValidateRecuperationCodeCommand(String email, String code) {

    public static ValidateRecuperationCodeCommand from(final String email, final String code) {
        return new ValidateRecuperationCodeCommand(email, code);
    }
}
