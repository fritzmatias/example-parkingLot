package assessment.parkinglot;

import assessment.parkinglot.config.AppConfigurationProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Profile("test")
@ActiveProfiles({"test"})
public class ParkingLotServiceApplicationTests {

	private final AppConfigurationProperties appConfigurationProperties;

	@Autowired
    public ParkingLotServiceApplicationTests(AppConfigurationProperties appConfigurationProperties) {
        this.appConfigurationProperties = appConfigurationProperties;
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
