package assessment.parkinglot.integration.service;

import assessment.parkinglot.controller.model.AvailableSpotsConfiguration;
import assessment.parkinglot.controller.model.SpotVehicleMappingConfiguration;
import assessment.parkinglot.errors.DataDuplication;
import assessment.parkinglot.errors.DataNotFound;
import assessment.parkinglot.integration.BasicIntegrationTest;
import assessment.parkinglot.services.ConfigurationService;
import assessment.parkinglot.config.components.PersistenceCleanerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConfigurationServiceErrorIntegrationTest
        extends BasicIntegrationTest {

    private final ConfigurationService configurationService;

    @Autowired
    public ConfigurationServiceErrorIntegrationTest(
            WebApplicationContext webApplicationContext, ConfigurationService configurationService
            , PersistenceCleanerService persistenceCleanerService
    ) {
        super(webApplicationContext, persistenceCleanerService);
        this.configurationService = configurationService;
    }

    @Test
    void dataDuplication_whenMappingsHasDuplications() {
        List<SpotVehicleMappingConfiguration> mappings=List.of(
            new SpotVehicleMappingConfiguration("car", "regular",1)
            , new SpotVehicleMappingConfiguration("car", "regular",3)
        );
        List<AvailableSpotsConfiguration> availableSpots=List.of(
               new AvailableSpotsConfiguration("regular", 1)
        );

        assertThrows(DataDuplication.class, () ->
                configurationService.loadConfiguration(mappings, availableSpots)
        );
    }

    @Test
    void dataDuplication_whenAvailableSpotsHasDuplications() {
        List<SpotVehicleMappingConfiguration> mappings = List.of(
            new SpotVehicleMappingConfiguration("car", "regular", 1)
        );
        List<AvailableSpotsConfiguration> availableSpots = List.of(
            new AvailableSpotsConfiguration("regular", 1)
            ,new AvailableSpotsConfiguration("regular", 2)
        );

        assertThrows(DataDuplication.class, () ->
                configurationService.loadConfiguration(mappings, availableSpots)
        );
    }

    @Test
    void dataNotFound_whenConfigurationHasNoMappings(){
        List<SpotVehicleMappingConfiguration> mappings=List.of();
        List<AvailableSpotsConfiguration> availableSpots=List.of(
                new AvailableSpotsConfiguration("regular", 1)
        );
        assertThrows(DataNotFound.class, () ->
                configurationService.loadConfiguration(null, availableSpots)
        );
        assertThrows(DataNotFound.class, () ->
                configurationService.loadConfiguration(mappings, availableSpots)
        );
    }

    @Test
    void dataNotFound_whenConfigurationHasNoAvailableSpots(){
        List<SpotVehicleMappingConfiguration> mappings=List.of(
            new SpotVehicleMappingConfiguration("car", "regular",1)
        );
        List<AvailableSpotsConfiguration> availableSpots=List.of(
        );
        assertThrows(DataNotFound.class, () ->
                configurationService.loadConfiguration(mappings, null)
        );
        assertThrows(DataNotFound.class, () ->
                configurationService.loadConfiguration(mappings, availableSpots)
        );
    }

}
