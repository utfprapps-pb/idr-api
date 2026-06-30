package br.gov.pr.idr.infra.property_management.api;

import br.gov.pr.idr.application.iam.user.retries.find.FindUserByUsernameUseCase;
import br.gov.pr.idr.application.property_management.sync.download.DownloadSyncUseCase;
import br.gov.pr.idr.application.property_management.sync.upload.UploadSyncCommand;
import br.gov.pr.idr.application.property_management.sync.upload.UploadSyncUseCase;
import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.infra.property_management.property.models.sync.DownloadSyncResponse;
import br.gov.pr.idr.infra.property_management.property.models.sync.UploadSyncRequest;
import br.gov.pr.idr.infra.property_management.property.models.sync.UploadSyncResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/sync")
@RequiredArgsConstructor
public class SyncController {

    private final DownloadSyncUseCase downloadSyncUseCase;
    private final UploadSyncUseCase uploadSyncUseCase;
    private final FindUserByUsernameUseCase findUserByUsernameUseCase;

    @GetMapping("/download")
    @PreAuthorize("hasAuthority('TECNICO')")
    public ResponseEntity<DownloadSyncResponse> download(final Authentication authentication) {
        final var technicianId = resolveUserId(authentication);
        final var output = downloadSyncUseCase.execute(technicianId);
        return ResponseEntity.ok(DownloadSyncResponse.from(output));
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('TECNICO')")
    public ResponseEntity<UploadSyncResponse> upload(
            @RequestBody @Valid final UploadSyncRequest request,
            final Authentication authentication) {
        final var command = new UploadSyncCommand(
                request.entities().stream()
                        .map(e -> new UploadSyncCommand.OfflineEntityCommand(e.type(), e.localId(), e.data()))
                        .toList()
        );
        final var results = uploadSyncUseCase.execute(command);
        return ResponseEntity.ok(UploadSyncResponse.from(results));
    }

    private UserID resolveUserId(final Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalStateException("Authentication principal is null");
        }
        final var username = authentication.getPrincipal().toString();
        final var user = findUserByUsernameUseCase.execute(username);
        return UserID.from(user.id());
    }
}
