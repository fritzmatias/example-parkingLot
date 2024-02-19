package assessment.parkinglot.config;

import assessment.parkinglot.config.components.PersistenceCleanerService;
import assessment.parkinglot.config.components.PersistenceCleanerServiceByRepository;
import assessment.parkinglot.config.components.PersistenceCleanerServiceByTruncation;
import assessment.parkinglot.peristence.repositories.SpotRepository;
import assessment.parkinglot.peristence.repositories.SpotTypeRepository;
import assessment.parkinglot.peristence.repositories.ValidSpotTypeByVehicleTypeRepository;
import assessment.parkinglot.peristence.repositories.VehicleTypeRepository;
import jakarta.persistence.EntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class AppTestConfig {

    @Bean
    public PersistenceCleanerService getPersistenceCleaner(
            EntityManager entityManager
            , SpotRepository spotRepository
            , SpotTypeRepository spotTypeRepository
            , ValidSpotTypeByVehicleTypeRepository validSpotTypeByVehicleTypeRepository
            , VehicleTypeRepository vehicleTypeRepository
    ){
        // Simple persistence cleaner switcher to compare performance
        var truncaton=false;

        if(truncaton) {
            return this.getPersistenceCleanerByTruncation(entityManager);
        }else {
            return this.getPersistenceCleanerServiceByRepository(spotRepository, spotTypeRepository, validSpotTypeByVehicleTypeRepository, vehicleTypeRepository);
        }
    }

    private PersistenceCleanerServiceByRepository getPersistenceCleanerServiceByRepository(SpotRepository spotRepository, SpotTypeRepository spotTypeRepository, ValidSpotTypeByVehicleTypeRepository validSpotTypeByVehicleTypeRepository, VehicleTypeRepository vehicleTypeRepository) {
        return new PersistenceCleanerServiceByRepository(
                spotRepository
                , spotTypeRepository
                , validSpotTypeByVehicleTypeRepository
                , vehicleTypeRepository
        );
    }

    private PersistenceCleanerServiceByTruncation getPersistenceCleanerByTruncation(EntityManager entityManager) {
        return new PersistenceCleanerServiceByTruncation(entityManager);
    }
}
