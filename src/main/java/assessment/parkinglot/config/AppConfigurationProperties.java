package assessment.parkinglot.config;

import assessment.parkinglot.controller.model.AvailableSpotsConfiguration;
import assessment.parkinglot.controller.model.SpotVehicleMappingConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.List;

@ConfigurationProperties(prefix="lot")
@ConfigurationPropertiesBinding
public record AppConfigurationProperties(

    @NestedConfigurationProperty
    List<AvailableSpotsConfiguration> parkingSpots,

    @NestedConfigurationProperty
    List<SpotVehicleMappingConfiguration> mappings
){
}