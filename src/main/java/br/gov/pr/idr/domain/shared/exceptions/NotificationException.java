package br.gov.pr.idr.domain.shared.exceptions;

import br.gov.pr.idr.domain.shared.validation.NotificationValidation;

public class NotificationException extends DomainException {

    public NotificationException(final String aMessage, final NotificationValidation  notification) {
        super(aMessage, notification.getErrors());
    }
}