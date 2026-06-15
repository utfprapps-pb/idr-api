package br.gov.pr.idr.domain.shared.exceptions;

import br.gov.pr.idr.domain.shared.validation.NotificationValidation;
import br.gov.pr.idr.domain.shared.validation.ValidationHandler;

public class NotificationException extends DomainException {

    public NotificationException(final String aMessage, final NotificationValidation notification) {
        super(aMessage, notification.getErrors());
    }

    public NotificationException(final ValidationHandler notification) {
        super("Erro ao realizar a operação: ", notification.getErrors());
    }
}