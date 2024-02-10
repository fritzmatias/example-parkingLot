package assessment.parkinglot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ParkingLotServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParkingLotServiceApplication.class, args);
	}

}
