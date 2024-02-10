package assessment.parkinglot.controller.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;

@ConfigurationPropertiesBinding
public record AvailableSpotsConfiguration (
    @NotBlank
    String type,
    @Min(1)
    int spots
){
}
