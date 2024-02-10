package assessment.parkinglot.controller.model;

import assessment.parkinglot.model.SpotRecord;

import java.time.OffsetDateTime;
import java.util.Set;

public record LotSpotStateResponse(
        OffsetDateTime asOf,
        Set<SpotRecord> spots
) {
}
