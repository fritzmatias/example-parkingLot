package assessment.parkinglot.integration;

import assessment.parkinglot.ParkingLotServiceApplication;
import assessment.parkinglot.config.AppTestConfig;
import assessment.parkinglot.config.components.PersistenceCleanerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ContextConfiguration(classes = { ParkingLotServiceApplication.class, AppTestConfig.class })
@ActiveProfiles({"test"})
public abstract class BasicIntegrationTest {

    final private WebApplicationContext webApplicationContext;
    final private PersistenceCleanerService persistenceCleanerService;
    private MockMvc mockMvc;

    public BasicIntegrationTest(
            WebApplicationContext webApplicationContext
            , PersistenceCleanerService persistenceCleanerService
    ) {
        this.webApplicationContext = webApplicationContext;
        this.persistenceCleanerService = persistenceCleanerService;
    }

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        persistenceCleanerService.cleanup("PUBLIC");
    }

    @Test
    void contextLoads() {
    }

    public MockMvc getMockMvc() {
        return mockMvc;
    }

    public WebApplicationContext getWebApplicationContext() {
        return webApplicationContext;
    }
}
