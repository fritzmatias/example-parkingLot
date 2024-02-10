package assessment.parkinglot.controller.model;

import jakarta.validation.constraints.NotBlank;

public record DepartingRequest(
        @NotBlank
        String parkingId
) {
}
