package br.gov.pr.idr.application.iam.email.reset_password;

public record ResetPasswordCommand(String email, String code, String password, String confirmPassword) {

    public static ResetPasswordCommand from(
            final String email,
            final String code,
            final String password,
            final String confirmPassword) {
        return new ResetPasswordCommand(email, code, password, confirmPassword);
    }
}
