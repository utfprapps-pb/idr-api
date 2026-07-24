package br.gov.pr.idr.domain.property_management.sync.query;

import br.gov.pr.idr.domain.iam.user.UserID;

import java.time.Instant;

public record DownloadSyncQuery(UserID technicianId, Instant since) {}
