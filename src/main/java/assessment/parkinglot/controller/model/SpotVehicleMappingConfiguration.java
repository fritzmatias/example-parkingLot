package assessment.parkinglot.controller.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;

@ConfigurationPropertiesBinding
public record SpotVehicleMappingConfiguration(
        @NotBlank
        String vehicleType,
        @NotBlank
        String spotType,
        @Min(1)
        int requiredSlots
){
}
