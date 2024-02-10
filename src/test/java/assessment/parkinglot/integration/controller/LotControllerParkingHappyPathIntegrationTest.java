package assessment.parkinglot.integration.controller;

import assessment.parkinglot.controller.LotController;
import assessment.parkinglot.controller.model.ParkingRequest;
import assessment.parkinglot.errors.DataDuplication;
import assessment.parkinglot.services.PersistenceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


public class LotControllerParkingHappyPathIntegrationTest
        extends BasicControllerIntegrationTest<LotController> {
    final private ObjectMapper om = new ObjectMapper();
    final private PersistenceService persistenceService;

    @Autowired
    public LotControllerParkingHappyPathIntegrationTest(
            WebApplicationContext webApplicationContext
            , PersistenceService persistenceService
    ) {
        super(webApplicationContext, LotController.class);
        this.persistenceService = persistenceService;
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2})
    void success_when_park_a_vehicle_on_an_allowed_free_spot(int spots) throws Exception, DataDuplication {
        String endpoint = LotController.endpoint.parkVehicle;
        persistenceService.persistMapping("car","regular",1);
        persistenceService.persistParkingSpots("regular", spots);

        ParkingRequest parkingRequest = new ParkingRequest("car", "A123");
        var respose = super.getMockMvc().perform(
                        MockMvcRequestBuilders.post(endpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(om.writeValueAsString(parkingRequest))
                ).andDo(
                        print()
                ).andExpect(
                        MockMvcResultMatchers.status().isOk()
                ).andExpect(
                        MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.spotType").value("regular")
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.parkingId").value(parkingRequest.vehicleId())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.slots.size()").value("1")
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.slots[0]").value("1")
                )
                .andReturn();
    }

    @ParameterizedTest
    @ValueSource(ints = {3,4})
    void success_when_park_a_vehicle_on_multiple_spots(int spots) throws Exception, DataDuplication {
        String endpoint = LotController.endpoint.parkVehicle;
        persistenceService.persistMapping("van","regular",3);
        persistenceService.persistParkingSpots("regular", spots);

        ParkingRequest parkingRequest = new ParkingRequest("van", "A123");
        var respose = super.getMockMvc().perform(
                        MockMvcRequestBuilders.post(endpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(om.writeValueAsString(parkingRequest))
                ).andDo(
                        print()
                ).andExpect(
                        MockMvcResultMatchers.status().isOk()
                ).andExpect(
                        MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.spotType").value("regular")
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.parkingId").value(parkingRequest.vehicleId())
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.slots.size()").value("3")
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.slots[0]").value("1")
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.slots[1]").value("2")
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.slots[2]").value("3")
                )
                .andReturn();
    }

}
