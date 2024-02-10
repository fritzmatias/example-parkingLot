package assessment.parkinglot.integration.controller;

import assessment.parkinglot.controller.LotController;
import assessment.parkinglot.errors.DataDuplication;
import assessment.parkinglot.errors.DataNotFound;
import assessment.parkinglot.services.PersistenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class LotControllerVerifyingErrorIntegrationTest
        extends BasicControllerIntegrationTest<LotController> {
    final private PersistenceService persistenceService;

    @Autowired
    public LotControllerVerifyingErrorIntegrationTest(
            WebApplicationContext webApplicationContext
            , PersistenceService persistenceService
    ) {
        super(webApplicationContext, LotController.class);
        this.persistenceService = persistenceService;
    }

    @Test
    void notFound_when_checks_remaining_spots_by_vehicleType() throws Exception, DataDuplication {
        persistenceService.persistMapping("car", "regular",1);
        persistenceService.persistMapping("car", "compact",1);
        persistenceService.persistMapping("van", "regular",3);
        persistenceService.persistMapping("motorcycle", "motorcycle",1);

        persistenceService.persistParkingSpots("compact", 3);
        persistenceService.persistParkingSpots("regular", 4);
        persistenceService.persistParkingSpots("motorcycle", 3);

        String endpoint= LotController.endpoint.remainingSpots;
        super.getMockMvc().perform(
                MockMvcRequestBuilders.get(endpoint)
                    .queryParam("vt","NotValidVehicle")
                    .contentType(MediaType.APPLICATION_JSON)
        ).andDo(
                print()
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.error").isNotEmpty()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.violations").doesNotExist()
        )
        ;
    }

    @Test
    void notFound_when_checks_remaining_spots_whithout_data() throws Exception {
        String endpoint= LotController.endpoint.remainingSpots;
        super.getMockMvc().perform(
                MockMvcRequestBuilders.get(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(
                print()
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.error").isNotEmpty()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.violations").doesNotExist()
        )
        ;
    }
}
