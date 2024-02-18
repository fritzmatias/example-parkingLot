package assessment.parkinglot.integration.service;

import assessment.parkinglot.errors.DataDuplication;
import assessment.parkinglot.errors.DataNotFound;
import assessment.parkinglot.integration.BasicIntegrationTest;
import assessment.parkinglot.config.components.PersistenceCleanerService;
import assessment.parkinglot.services.PersistenceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class PersistenceServiceErrorIntegrationTest
        extends BasicIntegrationTest {
    final private PersistenceService persistenceService;

    @Autowired
    public PersistenceServiceErrorIntegrationTest(
            WebApplicationContext webApplicationContext
            , PersistenceCleanerService persistenceCleanerService
            , PersistenceService persistenceService
    ) {
        super(webApplicationContext, persistenceCleanerService);
        this.persistenceService = persistenceService;
    }

    @Test
    void dataDuplication_whenPersistMapping() throws DataNotFound, DataDuplication {
        persistenceService.persistMapping("car", "regular", 1);
        assertThrows(DataDuplication.class, () ->
                persistenceService.persistMapping("car", "regular", 2)
        );

        persistenceService.persistMapping("car", "compact", 1);
        persistenceService.persistMapping("van", "regular", 3);
        persistenceService.persistMapping("motorcycle", "motorcycle", 1);

        persistenceService.persistParkingSpots("compact", 3);
        persistenceService.persistParkingSpots("regular", 4);
        persistenceService.persistParkingSpots("motorcycle", 3);
    }

    @Test
    void dataNotFound_whenPersistParkingSpot(){
        assertThrows(DataNotFound.class, () ->
                persistenceService.persistParkingSpots("compact", 3)
        );
    }

}
