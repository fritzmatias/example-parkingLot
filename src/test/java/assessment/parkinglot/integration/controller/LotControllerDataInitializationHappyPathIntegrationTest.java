package assessment.parkinglot.integration.controller;

import assessment.parkinglot.controller.LotController;
import assessment.parkinglot.errors.DataDuplication;
import assessment.parkinglot.config.components.PersistenceCleanerService;
import assessment.parkinglot.peristence.repositories.VehicleTypeRepository;
import assessment.parkinglot.services.PersistenceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;


public class LotControllerDataInitializationHappyPathIntegrationTest
        extends BasicControllerIntegrationTest<LotController> {
    final private PersistenceService persistenceService;
    final private VehicleTypeRepository vehicleTypeRepository;

    @Autowired
    public LotControllerDataInitializationHappyPathIntegrationTest(
            WebApplicationContext webApplicationContext
            , PersistenceCleanerService persistenceCleanerService
            , PersistenceService persistenceService
            , VehicleTypeRepository vehicleTypeRepository
    ) {
        super(webApplicationContext, persistenceCleanerService, LotController.class);
        this.persistenceService = persistenceService;
        this.vehicleTypeRepository = vehicleTypeRepository;
    }

    @Test
    void success_when_init_db_data() throws Exception, DataDuplication {
        persistenceService.persistMapping("car", "regular", 1);
        persistenceService.persistMapping("car", "compact", 2);
        persistenceService.persistParkingSpots("regular", 3);

        var vehicleTypes = this.vehicleTypeRepository.findByName("car").orElseThrow();
        assertThat(vehicleTypes).isNotNull();
        assertThat(vehicleTypes.getSpots()).isNotNull();
        assertThat(vehicleTypes.getSpots().size()).isGreaterThan(0);

        var mappings = this.persistenceService.getMappings("car");
        assertThat(mappings.size()).isGreaterThan(0);
        var mapping = mappings.stream().findFirst().get();
        assertThat(mapping.vehicleType()).isNotNull();
        assertThat(mapping.vehicleType()).isEqualTo("car");
    }

}

