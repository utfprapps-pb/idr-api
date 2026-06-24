package br.gov.pr.idr.domain.iam.email.send;

public interface SendEmailGateway {

    void send(String toEmail, String userName, String code);

    void sendPasswordResetConfirmation(String toEmail, String userName);

    void sendWelcome(String toEmail, String userName);
}
