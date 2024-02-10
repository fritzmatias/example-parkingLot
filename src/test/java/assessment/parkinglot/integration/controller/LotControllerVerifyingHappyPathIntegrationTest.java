package assessment.parkinglot.integration.controller;

import assessment.parkinglot.controller.LotController;
import assessment.parkinglot.errors.DataDuplication;
import assessment.parkinglot.errors.DataNotFound;
import assessment.parkinglot.controller.model.SpotTypeStatusResponse;
import assessment.parkinglot.services.PersistenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class LotControllerVerifyingHappyPathIntegrationTest
        extends BasicControllerIntegrationTest<LotController> {
    final private PersistenceService persistenceService;

    @Autowired
    public LotControllerVerifyingHappyPathIntegrationTest(
            WebApplicationContext webApplicationContext
            , PersistenceService persistenceService
    ) {
        super(webApplicationContext, LotController.class);
        this.persistenceService = persistenceService;
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
    }

    @Test
    void success_when_checks_lot_status() throws Exception {
        String endpoint= LotController.endpoint.remainingSpots;
        var vanLotState=new SpotTypeStatusResponse("van", 1 /* 4/3 */);
        var carLotState=new SpotTypeStatusResponse("car", 4+3);
        var motorcycleLotState=new SpotTypeStatusResponse("motorcycle", 3);

        super.getMockMvc().perform(
                MockMvcRequestBuilders.get(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(
                print()
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.asOf").exists()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.vehicleAvailability.length()").value( 3 )
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.vehicleAvailability[0].type").value( motorcycleLotState.type())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.vehicleAvailability[0].remainingSpots")
                        .value( motorcycleLotState.remainingSpots())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.vehicleAvailability[1].type").value( vanLotState.type())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.vehicleAvailability[1].remainingSpots")
                        .value( vanLotState.remainingSpots())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.vehicleAvailability[2].type").value( carLotState.type())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.vehicleAvailability[2].remainingSpots")
                        .value( carLotState.remainingSpots())
        )
        ;
    }

    @Test
    void success_when_checks_remaining_spots_by_vehicleType() throws Exception {
        String endpoint= LotController.endpoint.remainingSpots;
        var carLotState=new SpotTypeStatusResponse("van", 1);
        super.getMockMvc().perform(
                MockMvcRequestBuilders.get(endpoint)
                    .queryParam("vt","van")
                    .contentType(MediaType.APPLICATION_JSON)
        ).andDo(
                print()
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.asOf").exists()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.vehicleAvailability.length()").value( 1)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.vehicleAvailability[0].type").value( carLotState.type() )
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.vehicleAvailability[0].remainingSpots")
                        .value( carLotState.remainingSpots())
        )
        ;
    }
}
