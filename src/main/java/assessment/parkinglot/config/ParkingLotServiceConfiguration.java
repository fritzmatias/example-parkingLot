package assessment.parkinglot.config;

import assessment.parkinglot.errors.DataDuplication;
import assessment.parkinglot.services.ConfigurationService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class ParkingLotServiceConfiguration {
    private final AppConfigurationProperties appConfigurationProperties;
    private final ConfigurationService configurationService;
    private final Logger log= LoggerFactory.getLogger(ParkingLotServiceConfiguration.class);

    @Autowired
    public ParkingLotServiceConfiguration(
            AppConfigurationProperties appConfigurationProperties
            , ConfigurationService configurationService
    ) {
        this.appConfigurationProperties = appConfigurationProperties;
        this.configurationService = configurationService;
    }

    @PostConstruct
    public void init() throws Exception, DataDuplication {
        try {
            configurationService.loadConfiguration(
                    this.appConfigurationProperties.mappings()
                    , this.appConfigurationProperties.parkingSpots()
            );
        }catch (Exception e){
            log.error("Configuration Data Error");
            System.exit(-1);
        }
    }

}
