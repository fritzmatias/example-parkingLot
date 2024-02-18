package assessment.parkinglot.integration.controller;

import assessment.parkinglot.config.components.PersistenceCleanerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


public abstract class BaseControllerPostMethodErrorsIntegrationTest<T>
        extends BasicControllerIntegrationTest<T> {

    final protected ObjectMapper mapper = new ObjectMapper();
    public BaseControllerPostMethodErrorsIntegrationTest(
            WebApplicationContext webApplicationContext
            , PersistenceCleanerService persistenceCleanerService
            , Class<T> controller
    ) {
        super(webApplicationContext, persistenceCleanerService, controller);
    }

    public abstract void badRequest_givenPostRequest_when_endpoint_validation_fails() throws Exception ;

    protected void badRequest_givenPostRequest_when_endpoint_validation_fails(
            String endpointUrl
            , String postData
            , int expectedViolations
    ) throws Exception {
        var respose=super.getMockMvc().perform(
                MockMvcRequestBuilders.post(endpointUrl).contentType(MediaType.APPLICATION_JSON)
                        .content(postData)
        ).andDo(
            print()
        ).andExpect(
            MockMvcResultMatchers.status().isBadRequest()
        ).andExpect(
            MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.error").isNotEmpty()
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.violations.size()").value(expectedViolations)
        )
        .andReturn();
    }

    public abstract void notFound_givenPostReques_when_action_is_not_possible() throws Exception ;

    protected void notFound_givenPostReques_when_action_is_not_possible(String endpointUrl, String postData) throws Exception {

        var respose=super.getMockMvc().perform(
                        MockMvcRequestBuilders.post(endpointUrl).contentType(MediaType.APPLICATION_JSON)
                                .content(postData)
        ).andDo(
            print()
        ).andExpect(
            MockMvcResultMatchers.status().isNotFound()
        ).andExpect(
            MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.error").isNotEmpty()
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.violations").doesNotExist()
        )
        .andReturn();
    }

    protected void conflict_givenPostReques_when_action_is_duplicated(String endpointUrl, String postData)
            throws Exception {

        var respose=super.getMockMvc().perform(
                        MockMvcRequestBuilders.post(endpointUrl).contentType(MediaType.APPLICATION_JSON)
                                .content(postData)
                ).andDo(
                        print()
                ).andExpect(
                        MockMvcResultMatchers.status().isConflict()
                ).andExpect(
                        MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.error").isNotEmpty()
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.violations").doesNotExist()
                )
                .andReturn();
    }

}
