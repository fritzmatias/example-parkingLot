package assessment.parkinglot.controller.model;

import java.util.Set;

public record ParkingResponse(
    String spotType,
    String parkingId,
    Set<Integer> slots
) {
}
