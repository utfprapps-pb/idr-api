package br.gov.pr.idr.infra.property_management.api;

import br.gov.pr.idr.application.iam.user.retries.find.FindUserByUsernameUseCase;
import br.gov.pr.idr.domain.property_management.sync.query.DownloadSyncQuery;
import br.gov.pr.idr.application.property_management.sync.download.DownloadSyncUseCase;
import br.gov.pr.idr.application.property_management.sync.upload.OfflineEntityCommand;
import br.gov.pr.idr.application.property_management.sync.upload.UploadSyncCommand;
import br.gov.pr.idr.application.property_management.sync.upload.UploadSyncUseCase;
import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.infra.property_management.property.models.sync.DownloadSyncResponse;
import br.gov.pr.idr.infra.property_management.property.models.sync.UploadSyncRequest;
import br.gov.pr.idr.infra.property_management.property.models.sync.UploadSyncResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/v1/sync")
@RequiredArgsConstructor
public class SyncController {

    private final DownloadSyncUseCase downloadSyncUseCase;
    private final UploadSyncUseCase uploadSyncUseCase;
    private final FindUserByUsernameUseCase findUserByUsernameUseCase;

    @GetMapping("/download")
    public ResponseEntity<DownloadSyncResponse> download(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final Instant since,
            final Authentication authentication) {
        final var technicianId = resolveUserId(authentication);
        final var output = downloadSyncUseCase.execute(new DownloadSyncQuery(technicianId, since));
        return ResponseEntity.ok(DownloadSyncResponse.from(output));
    }

    @PostMapping("/upload")
    public ResponseEntity<UploadSyncResponse> upload(
            @RequestBody @Valid final UploadSyncRequest request,
            final Authentication authentication) {
        final var technicianId = resolveUserId(authentication);
        final var command = new UploadSyncCommand(
                technicianId,
                request.entities().stream()
                        .map(e -> new OfflineEntityCommand(e.type(), e.localId(), e.data()))
                        .toList()
        );
        final var results = uploadSyncUseCase.execute(command);
        return ResponseEntity.ok(UploadSyncResponse.from(results));
    }

    private UserID resolveUserId(final Authentication authentication) {
        final var principal = authentication == null ? null : authentication.getPrincipal();
        if (principal == null) {
            throw new IllegalStateException("Authentication principal is null");
        }
        final var username = principal.toString();
        final var user = findUserByUsernameUseCase.execute(username);
        return UserID.from(user.id());
    }
}
