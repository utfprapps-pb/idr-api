package br.gov.pr.idr.infra.shared.error;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(Instant timestamp, int status, String message, List<FieldError> errors) {
}