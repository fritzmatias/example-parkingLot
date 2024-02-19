package assessment.parkinglot.integration.controller;

import assessment.parkinglot.controller.LotController;
import assessment.parkinglot.controller.model.ParkingRequest;
import assessment.parkinglot.errors.DataDuplication;
import assessment.parkinglot.config.components.PersistenceCleanerService;
import assessment.parkinglot.peristence.repositories.SpotRepository;
import assessment.parkinglot.peristence.repositories.SpotTypeRepository;
import assessment.parkinglot.services.PersistenceService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;


public class LotControllerParkingPostMethodErrorsIntegrationTest
        extends BaseControllerPostMethodErrorsIntegrationTest<LotController> {

    private final String endpointUrl = LotController.endpoint.parkVehicle;
    private final PersistenceService persistenceService;
    private final SpotRepository spotRepository;
    private final SpotTypeRepository spotTypeRepository;

    @Autowired
    public LotControllerParkingPostMethodErrorsIntegrationTest(
            WebApplicationContext webApplicationContext
            , PersistenceCleanerService persistenceCleanerService
            , PersistenceService persistenceService, SpotRepository spotRepository, SpotTypeRepository spotTypeRepository, EntityManager entityManager
    ) {
        super(webApplicationContext, persistenceCleanerService , LotController.class);
        this.persistenceService = persistenceService;
        this.spotRepository = spotRepository;
        this.spotTypeRepository = spotTypeRepository;
    }

    @Test
    @Override
    public void badRequest_givenPostRequest_when_endpoint_validation_fails() throws Exception {
        ParkingRequest emptyRequest=new ParkingRequest("", "");
        super.badRequest_givenPostRequest_when_endpoint_validation_fails(
                endpointUrl
                , mapper.writeValueAsString(emptyRequest)
                , 2
        );
    }

    @Test
    @Override
    public void notFound_givenPostReques_when_action_is_not_possible() throws Exception {
        ParkingRequest validRequest=new ParkingRequest("car", "a1234");
        super.notFound_givenPostReques_when_action_is_not_possible(
                endpointUrl
                ,mapper.writeValueAsString(validRequest)
        );
    }

    @Test
    public void notFound_givenPostRequest_when_action_is_not_possible_on_multi_slot()
            throws Exception, DataDuplication {
        this.persistenceService.persistMapping("van","regular", 3);
        this.persistenceService.persistParkingSpots("regular", 2);

        ParkingRequest validRequest=new ParkingRequest("van", "a1234");
        super.notFound_givenPostReques_when_action_is_not_possible(
                endpointUrl
                ,mapper.writeValueAsString(validRequest)
        );
    }

    @Test
    public void DataDuplication_givenTwicePostRequest_when_action_is_not_possible_on_multi_slot()
            throws Exception, DataDuplication {
        String regular = "regular";
        String vehicleType = "car";
        String vehicleId = "a1234";
        mimicParkingCar(vehicleType, regular, vehicleId);

        // 2d request
        ParkingRequest validRequest=new ParkingRequest(vehicleType, vehicleId);
        super.conflict_givenPostReques_when_action_is_duplicated(
                endpointUrl
                ,mapper.writeValueAsString(validRequest)
        );
    }

    public void mimicParkingCar(String vehicleType, String regular, String vehicleId) throws DataDuplication, Exception {
        this.persistenceService.persistMapping(vehicleType, regular, 3);
        this.persistenceService.persistParkingSpots(regular, 2);
        this.spotTypeRepository.findFirstViewByName(regular)
                .orElseThrow();

        var spots=this.spotRepository.findByVehicleIdIsNullAndType_Name(regular);
        if(spots.isEmpty()){
            throw new Exception("Should not be empty");
        }

        this.spotRepository.saveAndFlush(spots.getFirst().park(vehicleId));
        if(!this.spotRepository.existsByVehicleIdIs(vehicleId)){
            throw new Exception("Should not be empty after persist");
        }

    }
}
