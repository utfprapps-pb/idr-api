package br.gov.pr.idr.infra.iam.email.models.reset_password;

public record ResetPasswordResponse(String message) {

    public static ResetPasswordResponse ok() {
        return new ResetPasswordResponse("Senha redefinida com sucesso!");
    }
}
