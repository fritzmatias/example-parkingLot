package assessment.parkinglot.integration.controller;

import assessment.parkinglot.controller.LotController;
import assessment.parkinglot.errors.DataDuplication;
import assessment.parkinglot.errors.DataNotFound;
import assessment.parkinglot.config.components.PersistenceCleanerService;
import assessment.parkinglot.peristence.repositories.SpotRepository;
import assessment.parkinglot.services.PersistenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class LotControllerTakenSpotsErrorsIntegrationTest
        extends BasicControllerIntegrationTest<LotController> {
    final private PersistenceService persistenceService;
    final private SpotRepository spotRepository;
    private final String endpoint = LotController.endpoint.takenParkingSpots;

    @Autowired
    public LotControllerTakenSpotsErrorsIntegrationTest(
            WebApplicationContext webApplicationContext
            , PersistenceCleanerService persistenceCleanerService
            , PersistenceService persistenceService, SpotRepository spotRepository
    ) {
        super(webApplicationContext, persistenceCleanerService, LotController.class);
        this.persistenceService = persistenceService;
        this.spotRepository = spotRepository;
    }

    @BeforeEach
    void initData() throws DataNotFound, DataDuplication {
        persistenceService.persistMapping("car", "regular",1);
        persistenceService.persistMapping("car", "compact",1);
        persistenceService.persistMapping("van", "regular",3);
        persistenceService.persistMapping("motorcycle", "motorcycle",1);

        persistenceService.persistParkingSpots("compact", 3);
        persistenceService.persistParkingSpots("regular", 4);
        persistenceService.persistParkingSpots("motorcycle", 3);

        spotRepository.findByType_Name("compact").stream()
                .limit(1).map(spot->spot.park("a1234"));
        spotRepository.findByType_Name("regular").stream()
                .limit(1).map(spot->spot.park("b1234"));
    }

    @Test
    void badRequest_when_vehicleType_is_not_set() throws Exception {
        super.getMockMvc().perform(
                MockMvcRequestBuilders.get(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(
                print()
        ).andExpect(
                MockMvcResultMatchers.status().isBadRequest()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.error").isNotEmpty()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.violations").exists()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.violations[0].property").value("vt")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.violations[0].message").isNotEmpty()
        )
        ;
    }
}

