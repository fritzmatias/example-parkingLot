package assessment.parkinglot.model;

import java.time.OffsetDateTime;

public record SpotRecord(
        int id,
        String type,
        String vehicleId,
        OffsetDateTime lastUpdate
) {
}
