package br.gov.pr.idr.domain.property_management.property.query;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public record GetPropertyQueryResult(
        UUID id,
        String name,
        BigDecimal nakedAveragePrice,
        BigDecimal leaseAveragePrice,
        Double dairyCattleFarmingArea,
        Double perennialPastureArea,
        Double summerPlowingArea,
        Double winterPlowingArea,
        BigDecimal latitude,
        BigDecimal longitude,
        Long version,
        Instant updatedAt,
        Producer producer,
        City city,
        List<Technician> technicians,
        List<Collaborator> collaborators,
        List<Attachment> attachments
) {

    public record Producer(UUID id, String name) {}
    public record City(UUID id, String name) {}
    public record Technician(UUID id, String name) {}
    public record Collaborator(UUID id, String name, String hoursPerDay) {}
    public record Attachment(UUID id, String fileName, String contentType, Long sizeBytes) {}

    /**
     * Separadores usados nas agregações LISTAGG do {@code PropertyJPARepository}.
     * Caracteres de controle ASCII (Record/Unit Separator) em vez de ','/'|' para não colidir
     * com campos de texto livre (ex.: nome de arquivo de anexo contendo vírgula).
     */
    private static final String RECORD_SEPARATOR = "\u001E";
    private static final String FIELD_SEPARATOR = "\u001F";

    public GetPropertyQueryResult(
            UUID id, String name,
            BigDecimal nakedAveragePrice, BigDecimal leaseAveragePrice,
            Double dairyCattleFarmingArea, Double perennialPastureArea,
            Double summerPlowingArea, Double winterPlowingArea,
            BigDecimal latitude, BigDecimal longitude,
            Long version, Instant updatedAt,
            UUID producerId, String producerName,
            UUID cityId, String cityName,
            String techniciansStr, String collaboratorsStr, String attachmentsStr
    ) {
        this(id, name, nakedAveragePrice, leaseAveragePrice,
                dairyCattleFarmingArea, perennialPastureArea, summerPlowingArea, winterPlowingArea,
                latitude, longitude, version, updatedAt,
                new Producer(producerId, producerName),
                new City(cityId, cityName),
                parseTechnicians(techniciansStr),
                parseCollaborators(collaboratorsStr),
                parseAttachments(attachmentsStr));
    }

    private static List<Technician> parseTechnicians(final String str) {
        if (str == null || str.isBlank()) return List.of();
        return Arrays.stream(str.split(RECORD_SEPARATOR))
                .map(s -> s.split(FIELD_SEPARATOR))
                .map(parts -> new Technician(UUID.fromString(parts[0]), parts[1]))
                .toList();
    }

    private static List<Collaborator> parseCollaborators(final String str) {
        if (str == null || str.isBlank()) return List.of();
        return Arrays.stream(str.split(RECORD_SEPARATOR))
                .map(s -> s.split(FIELD_SEPARATOR))
                .map(parts -> new Collaborator(UUID.fromString(parts[0]), parts[1], parts[2]))
                .toList();
    }

    private static List<Attachment> parseAttachments(final String str) {
        if (str == null || str.isBlank()) return List.of();
        return Arrays.stream(str.split(RECORD_SEPARATOR))
                .map(s -> s.split(FIELD_SEPARATOR))
                .map(parts -> new Attachment(UUID.fromString(parts[0]), parts[1], parts[2], Long.valueOf(parts[3])))
                .toList();
    }
}
