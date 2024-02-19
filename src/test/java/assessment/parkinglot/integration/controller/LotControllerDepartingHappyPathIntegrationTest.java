package assessment.parkinglot.integration.controller;

import assessment.parkinglot.controller.LotController;
import assessment.parkinglot.errors.DataDuplication;
import assessment.parkinglot.errors.DataNotFound;
import assessment.parkinglot.controller.model.DepartingRequest;
import assessment.parkinglot.config.components.PersistenceCleanerService;
import assessment.parkinglot.peristence.repositories.SpotRepository;
import assessment.parkinglot.services.PersistenceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


public class LotControllerDepartingHappyPathIntegrationTest
        extends BasicControllerIntegrationTest<LotController> {
    final private ObjectMapper om = new ObjectMapper();
    final private PersistenceService persistenceService;
    final private SpotRepository spotRepository;

    @Autowired
    public LotControllerDepartingHappyPathIntegrationTest(
            WebApplicationContext webApplicationContext
            , PersistenceCleanerService persistenceCleanerService
            , PersistenceService persistenceService
            , SpotRepository spotRepository
    ) {
        super(webApplicationContext, persistenceCleanerService, LotController.class);
        this.persistenceService = persistenceService;
        this.spotRepository = spotRepository;
    }

    @BeforeEach
    void initData() throws DataNotFound, DataDuplication {
        persistenceService.persistMapping(
                "car"
                ,"regular"
                ,1
        );
        persistenceService.persistParkingSpots("regular",5);
        var spot=spotRepository.findAll().stream().findFirst().orElseThrow();
        spot.park("parkingId");
        spotRepository.saveAndFlush(spot);
    }

    @Test
    void success_when_vehicle_depart_then_frees_up_spot() throws Exception {
        String endpoint = LotController.endpoint.departVehicle;
        DepartingRequest departingRequest = new DepartingRequest("parkingId");
        super.getMockMvc().perform(
                MockMvcRequestBuilders.post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(departingRequest))
        ).andDo(
                print()
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.asOf").isNotEmpty()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.parkingId").value(departingRequest.parkingId())
       ).andExpect(
                MockMvcResultMatchers.jsonPath("$.slots[0]").isNotEmpty()
        )
        ;
    }

}
