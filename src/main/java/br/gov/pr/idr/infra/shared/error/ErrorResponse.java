package br.gov.pr.idr.infra.shared.error;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(LocalDateTime timestamp, int status, String message, List<FieldError> errors) {
}