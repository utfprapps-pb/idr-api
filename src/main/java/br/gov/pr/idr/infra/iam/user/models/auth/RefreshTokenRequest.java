package br.gov.pr.idr.infra.iam.user.models.auth;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(@NotBlank String token) {}
