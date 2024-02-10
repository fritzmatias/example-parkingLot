package assessment.parkinglot.controller.model;

public record SpotTypeStatusResponse(
        String type,
        long remainingSpots
) {
}
