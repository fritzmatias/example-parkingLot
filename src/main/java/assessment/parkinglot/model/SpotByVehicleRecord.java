package assessment.parkinglot.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record SpotByVehicleRecord(
        @NotBlank
        String vehicleType,
        @NotBlank
        String spotType,
        @Min(1)
        int slots
) {
}
