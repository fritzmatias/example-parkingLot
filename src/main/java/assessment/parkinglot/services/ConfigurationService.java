package assessment.parkinglot.services;

import assessment.parkinglot.controller.model.AvailableSpotsConfiguration;
import assessment.parkinglot.controller.model.SpotVehicleMappingConfiguration;
import assessment.parkinglot.errors.DataDuplication;
import assessment.parkinglot.errors.DataNotFound;
import assessment.parkinglot.errors.ThrowerFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ConfigurationService {
    private final PersistenceService persistenceService;
    private final Logger log = LoggerFactory.getLogger(ConfigurationService.class);
    private final ObjectMapper objectMapper;

    @Autowired
    public ConfigurationService(
            PersistenceService persistenceService
    ) {
        this.persistenceService = persistenceService;
        objectMapper = new ObjectMapper();
    }

    public void loadConfiguration(
            List<SpotVehicleMappingConfiguration> mappings
            , List<AvailableSpotsConfiguration> availableSpotsConfigurations
    ) throws DataNotFound, DataDuplication {
        if(
            mappings == null || mappings.isEmpty()
            || availableSpotsConfigurations == null || availableSpotsConfigurations.isEmpty()
        ){
            ThrowerFactory.throwDataNotFound(
                    "Configuration not found", "Empty data.");
        }

        var persistedMappings = loadConfigurationMappings(
                mappings
        );
        validateConfiguration(mappings,persistedMappings);

        var persistedParkingSpots= loadConfigurationParkingSpots(
                availableSpotsConfigurations
        );
        validateConfiguration(availableSpotsConfigurations,persistedParkingSpots);
    }

    private void validateConfiguration(Collection<?> expected, Set<?> actual)
            throws DataDuplication {
        if(expected.size() != actual.size()){
            ThrowerFactory.throwDataDuplication(
                    "Configuration could have duplicated rules","");
        }
    }

    private Set<?> loadConfigurationMappings(List<SpotVehicleMappingConfiguration> mappings) {
        return mappings.stream()
                .map(mapping -> {
                            try {
                                log.info("Creating mapping of: "+ objectMapper.writeValueAsString(mapping));
                                return persistenceService.persistMapping(
                                        mapping.vehicleType()
                                        , mapping.spotType()
                                        , mapping.requiredSlots()
                                    );
                            } catch (DataDuplication |JsonProcessingException e) {
                                return Optional.empty();
                            }
                        }
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    private Set<?> loadConfigurationParkingSpots(Collection<AvailableSpotsConfiguration> parkingSpots) {
        return parkingSpots.stream()
                .map(mapping -> {
                        try {
                            log.info("Creating parking spot of: "+ objectMapper.writeValueAsString(mapping));
                            return persistenceService.persistParkingSpots(
                                    mapping.type()
                                    , mapping.spots()
                                );
                        } catch (DataNotFound | DataDuplication |JsonProcessingException e) {
                            return Optional.empty();
                        }
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

}
