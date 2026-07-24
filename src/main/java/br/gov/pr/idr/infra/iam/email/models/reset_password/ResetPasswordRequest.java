package br.gov.pr.idr.infra.iam.email.models.reset_password;

public record ResetPasswordRequest(String email, String code, String password, String confirmPassword) {
}
