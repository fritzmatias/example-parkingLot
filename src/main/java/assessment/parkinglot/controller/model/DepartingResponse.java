package assessment.parkinglot.controller.model;

import java.time.OffsetDateTime;
import java.util.Set;

public record DepartingResponse(
    OffsetDateTime asOf,
    String parkingId,
    Set<String> slots
){

}
