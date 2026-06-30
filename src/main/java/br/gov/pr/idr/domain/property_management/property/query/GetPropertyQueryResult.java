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
        List<Collaborator> collaborators
) {

    public record Producer(UUID id, String name) {}
    public record City(UUID id, String name) {}
    public record Technician(UUID id, String name) {}
    public record Collaborator(UUID id, String name, String hoursPerDay) {}

    public GetPropertyQueryResult(
            UUID id, String name,
            BigDecimal nakedAveragePrice, BigDecimal leaseAveragePrice,
            Double dairyCattleFarmingArea, Double perennialPastureArea,
            Double summerPlowingArea, Double winterPlowingArea,
            BigDecimal latitude, BigDecimal longitude,
            Long version, Instant updatedAt,
            UUID producerId, String producerName,
            UUID cityId, String cityName,
            String techniciansStr, String collaboratorsStr
    ) {
        this(id, name, nakedAveragePrice, leaseAveragePrice,
                dairyCattleFarmingArea, perennialPastureArea, summerPlowingArea, winterPlowingArea,
                latitude, longitude, version, updatedAt,
                new Producer(producerId, producerName),
                new City(cityId, cityName),
                parseTechnicians(techniciansStr),
                parseCollaborators(collaboratorsStr));
    }

    private static List<Technician> parseTechnicians(final String str) {
        if (str == null || str.isBlank()) return List.of();
        return Arrays.stream(str.split(","))
                .map(s -> s.split("\\|"))
                .map(parts -> new Technician(UUID.fromString(parts[0]), parts[1]))
                .toList();
    }

    private static List<Collaborator> parseCollaborators(final String str) {
        if (str == null || str.isBlank()) return List.of();
        return Arrays.stream(str.split(","))
                .map(s -> s.split("\\|"))
                .map(parts -> new Collaborator(UUID.fromString(parts[0]), parts[1], parts[2]))
                .toList();
    }
}
