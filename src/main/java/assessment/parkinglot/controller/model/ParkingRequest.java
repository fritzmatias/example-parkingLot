package assessment.parkinglot.controller.model;


import jakarta.validation.constraints.NotBlank;

public record ParkingRequest (
        @NotBlank
        String type,
        @NotBlank
        String vehicleId
) {
}
