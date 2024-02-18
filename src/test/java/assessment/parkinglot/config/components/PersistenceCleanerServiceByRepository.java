package assessment.parkinglot.config.components;

import assessment.parkinglot.peristence.repositories.SpotRepository;
import assessment.parkinglot.peristence.repositories.SpotTypeRepository;
import assessment.parkinglot.peristence.repositories.ValidSpotTypeByVehicleTypeRepository;
import assessment.parkinglot.peristence.repositories.VehicleTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersistenceCleanerServiceByRepository implements PersistenceCleanerService {
    private final SpotRepository spotRepository;
    private final SpotTypeRepository spotTypeRepository;
    private final ValidSpotTypeByVehicleTypeRepository validSpotTypeByVehicleTypeRepository;
    private final VehicleTypeRepository vehicleTypeRepository;
    private final Logger logger= LoggerFactory.getLogger(PersistenceCleanerServiceByRepository.class);

    public PersistenceCleanerServiceByRepository(
            SpotRepository spotRepository
            , SpotTypeRepository spotTypeRepository
            , ValidSpotTypeByVehicleTypeRepository validSpotTypeByVehicleTypeRepository
            , VehicleTypeRepository vehicleTypeRepository
    ) {
        this.spotRepository = spotRepository;
        this.spotTypeRepository = spotTypeRepository;
        this.validSpotTypeByVehicleTypeRepository = validSpotTypeByVehicleTypeRepository;
        this.vehicleTypeRepository = vehicleTypeRepository;
    }

    public void cleanup(String schemaName) {
        this.disableConstraints();
        this.truncateTables(schemaName);
        this.resetSequences(schemaName);
        this.enableConstraints();
    }

    private void disableConstraints() {
    }
    private void enableConstraints() {
    }

    private void truncateTables(String schemaName) {
        this.spotRepository.deleteAll();
        this.validSpotTypeByVehicleTypeRepository.deleteAll();
        this.spotTypeRepository.deleteAll();
        this.vehicleTypeRepository.deleteAll();
    }
    private void resetSequences(String schemaName) {
    }

}
