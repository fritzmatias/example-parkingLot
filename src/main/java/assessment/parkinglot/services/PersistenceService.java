package assessment.parkinglot.services;

import assessment.parkinglot.controller.model.AvailableSpotsConfiguration;
import assessment.parkinglot.errors.DataDuplication;
import assessment.parkinglot.errors.DataNotFound;
import assessment.parkinglot.errors.ThrowerFactory;
import assessment.parkinglot.model.SpotByVehicleRecord;
import assessment.parkinglot.peristence.entities.Spot;
import assessment.parkinglot.peristence.entities.SpotType;
import assessment.parkinglot.peristence.entities.ValidSpotTypeByVehicleType;
import assessment.parkinglot.peristence.entities.VehicleType;
import assessment.parkinglot.peristence.repositories.SpotRepository;
import assessment.parkinglot.peristence.repositories.SpotTypeRepository;
import assessment.parkinglot.peristence.repositories.ValidSpotTypeByVehicleTypeRepository;
import assessment.parkinglot.peristence.repositories.VehicleTypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional
public class PersistenceService {
    private final VehicleTypeRepository vehicleTypeRepository;
    private final SpotTypeRepository spotTypeRepository;
    private final ValidSpotTypeByVehicleTypeRepository mappingRepository;
    private final SpotRepository spotRepository;

    @Autowired
    public PersistenceService(
            VehicleTypeRepository vehicleTypeRepository
            , SpotTypeRepository spotTypeRepository
            , ValidSpotTypeByVehicleTypeRepository mappingRepository
            , SpotRepository spotRepository
    ) {
        this.vehicleTypeRepository = vehicleTypeRepository;
        this.spotTypeRepository = spotTypeRepository;
        this.mappingRepository = mappingRepository;
        this.spotRepository = spotRepository;
    }

    private VehicleType persistAndFlushVehicleType(String vehicleTypeName) {
        var vehicleType= this.createVehicleType(vehicleTypeName);
        return this.vehicleTypeRepository.saveAndFlush(vehicleType);
    }

    private VehicleType createVehicleType(String vehicleTypeName) {
        return new VehicleType(vehicleTypeName);
    }

    private SpotType persistAndFlushSpotType(String spotTypeName) {
        var spotType= this.createSpotType(spotTypeName);
        return this.spotTypeRepository.saveAndFlush(spotType);
    }

    private SpotType createSpotType(String spotTypeName) {
        return new SpotType(spotTypeName);
    }

    private void persistAndFlushMapping(SpotByVehicleRecord mapping) {
        var vehicle= this.vehicleTypeRepository
                .findByName(mapping.vehicleType()).orElseGet(
                        ()->this.persistAndFlushVehicleType(mapping.vehicleType())
                );
        var spot= this.spotTypeRepository.findFirstByName(mapping.spotType())
                .orElseGet(
                        ()->this.persistAndFlushSpotType(mapping.spotType())
                );
        var mappingType = new ValidSpotTypeByVehicleType(vehicle, spot, mapping.slots());
        mappingType = mappingRepository.saveAndFlush(mappingType);
        vehicle.getSpots().add(mappingType);
        spot.getVehicles().add(mappingType);
        this.vehicleTypeRepository.save(vehicle);
        this.vehicleTypeRepository.flush();
        this.spotTypeRepository.save(spot);
        this.spotRepository.flush();
    }

    private void verifyMappingExistence(String vehicleType, String spotType) throws DataDuplication {
        if(this.mappingRepository.findFirstById_VehicleType_NameAndId_SpotType_Name(
                vehicleType
                , spotType)
            .isPresent()
        ) {
            ThrowerFactory.throwDataDuplication("Mapping exist"
                    , vehicleType+","+spotType);
        }
    }
    private void verifySpotsExistence(String spotType) throws DataDuplication {
        if(this.spotRepository.existsByType_Name(spotType)) {
            ThrowerFactory.throwDataDuplication("Mapping exist", spotType);
        }
    }

    public SpotByVehicleRecord createMapping(
            String vehicleType, String spotType, int slots
    ){
        return new SpotByVehicleRecord(vehicleType,spotType, slots);
    }
    public Optional<SpotByVehicleRecord> persistMapping(
            String vehicleType, String spotType, int slots
    ) throws DataDuplication {
        verifyMappingExistence(vehicleType, spotType);
        var entity=this.createMapping(vehicleType,spotType,slots);
        this.persistAndFlushMapping(entity);
        return Optional.of(entity);
    }

    public Optional<AvailableSpotsConfiguration> persistParkingSpots(String spotType, int spots)
            throws DataDuplication, DataNotFound {
        this.verifySpotsExistence(spotType);
        var spotTypeEntity=this.spotTypeRepository.findFirstByName(spotType)
                .orElseThrow(
                    ThrowerFactory.DataNotFoundSupplier(
                            "Spot type not found."
                            , spotType+","+spots
                    )
                );

        this.spotRepository.saveAllAndFlush(
            IntStream.range(0,spots)
                    .mapToObj(i-> this.createSpot(spotTypeEntity))
                    .collect(Collectors.toList())
        );
        return Optional.of(createAvailableSpotsConfiguration(spotType, spots));
    }

    private Spot createSpot(SpotType spotTypeEntity) {
        return new Spot(spotTypeEntity);
    }

    private AvailableSpotsConfiguration createAvailableSpotsConfiguration(String spotType, int spots) {
        return new AvailableSpotsConfiguration(spotType, spots);
    }

    public List<SpotByVehicleRecord> getMappings(String vehicleType) {
        return this.mappingRepository.findViewById_VehicleType_Name(vehicleType);
    }
}
