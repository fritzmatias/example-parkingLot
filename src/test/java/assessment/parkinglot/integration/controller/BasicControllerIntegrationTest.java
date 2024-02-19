package assessment.parkinglot.integration.controller;

import assessment.parkinglot.integration.BasicIntegrationTest;
import assessment.parkinglot.config.components.PersistenceCleanerService;
import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;

import java.beans.Introspector;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class BasicControllerIntegrationTest<T> extends BasicIntegrationTest {
    final private Class<T> expectedController;

    public BasicControllerIntegrationTest(
            WebApplicationContext webApplicationContext
            , PersistenceCleanerService persistenceCleanerService
            , Class<T> expectedController
    ) {
        super(
                webApplicationContext
                , persistenceCleanerService
        );
        this.expectedController = expectedController;
    }


    @Test
    public void givenIntegrationTests_whenServletContext_thenItProvidesController() {
        ServletContext servletContext = super.getWebApplicationContext().getServletContext();

        assertNotNull(servletContext);
        assertInstanceOf(MockServletContext.class, servletContext);
        assertNotNull(super.getWebApplicationContext().getBean(
                Introspector.decapitalize( this.expectedController.getSimpleName() )
        ));
    }

}
