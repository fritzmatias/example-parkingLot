package assessment.parkinglot;

import assessment.parkinglot.config.AppConfigurationProperties;
import assessment.parkinglot.config.AppTestConfig;
import assessment.parkinglot.config.components.PersistenceCleanerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(classes = { ParkingLotServiceApplication.class, AppTestConfig.class })
@Profile("test")
@ActiveProfiles({"test"})
public class ParkingLotServiceApplicationTests {

	private final AppConfigurationProperties appConfigurationProperties;
	private final PersistenceCleanerService persistenceCleanerService;

	@Autowired
    public ParkingLotServiceApplicationTests(AppConfigurationProperties appConfigurationProperties, PersistenceCleanerService persistenceCleanerService) {
        this.appConfigurationProperties = appConfigurationProperties;
        this.persistenceCleanerService = persistenceCleanerService;
    }

	@BeforeEach
	void beforeAll(){
		this.persistenceCleanerService.cleanup("PUBLIC");
	}

    @Test
	void contextLoads() {
	}

	@Test()
	void loadConfiguration(){
		assertThat(this.appConfigurationProperties).isNotNull();
		assertThat(this.appConfigurationProperties.mappings()).isNotNull();
		assertThat(this.appConfigurationProperties.mappings().size()).isGreaterThan(0);
		assertThat(this.appConfigurationProperties.mappings().get(0).vehicleType()).isEqualTo("carTest");
		assertThat(this.appConfigurationProperties.mappings().get(0).spotType()).isEqualTo("regularTest");
		assertThat(this.appConfigurationProperties.parkingSpots()).isNotNull();
		assertThat(this.appConfigurationProperties.parkingSpots().size()).isGreaterThan(0);
		assertThat(this.appConfigurationProperties.parkingSpots().get(0).type()).isEqualTo("regularTest");
		assertThat(this.appConfigurationProperties.parkingSpots().get(0).spots()).isGreaterThan(0);
	}


}
