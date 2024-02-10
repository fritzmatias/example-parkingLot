package assessment.parkinglot.integration.controller;

import assessment.parkinglot.controller.LotController;
import assessment.parkinglot.controller.model.DepartingRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;


public class LotControllerDepartingPostMethodErrorsIntegrationTest
        extends BaseControllerPostMethodErrorsIntegrationTest<LotController> {
    private final String endpointUrl = LotController.endpoint.departVehicle;

    @Autowired
    public LotControllerDepartingPostMethodErrorsIntegrationTest(
            WebApplicationContext webApplicationContext
            , LotController controller
    ) {
        super(webApplicationContext, LotController.class);
    }

    @Test
    @Override
    public void badRequest_givenPostRequest_when_endpoint_validation_fails() throws Exception {
        DepartingRequest emptyRequest=new DepartingRequest( "");
        super.badRequest_givenPostRequest_when_endpoint_validation_fails(
                endpointUrl
                ,mapper.writeValueAsString(emptyRequest)
                , 1
        );
    }

    @Test
    @Override
    public void notFound_givenPostReques_when_action_is_not_possible() throws Exception {
        DepartingRequest validRequest=new DepartingRequest( "parkingId");
        super.notFound_givenPostReques_when_action_is_not_possible(
                endpointUrl
                ,mapper.writeValueAsString(validRequest)
        );
    }
}
