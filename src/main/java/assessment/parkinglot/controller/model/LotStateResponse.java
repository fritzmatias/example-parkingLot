package assessment.parkinglot.controller.model;

import java.time.OffsetDateTime;
import java.util.Set;

public record LotStateResponse(
        OffsetDateTime asOf,
        Set<SpotTypeStatusResponse> vehicleAvailability
) {
}
