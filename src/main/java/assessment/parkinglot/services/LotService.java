package assessment.parkinglot.services;

import assessment.parkinglot.controller.model.*;
import assessment.parkinglot.errors.DataDuplication;
import assessment.parkinglot.errors.ThrowerFactory;
import assessment.parkinglot.model.SpotByVehicleRecord;
import assessment.parkinglot.model.SpotRecord;
import assessment.parkinglot.model.VehicleTypeRecord;
import assessment.parkinglot.peristence.entities.Spot;
import assessment.parkinglot.peristence.repositories.SpotRepository;
import assessment.parkinglot.peristence.repositories.ValidSpotTypeByVehicleTypeRepository;
import assessment.parkinglot.peristence.repositories.VehicleTypeRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class LotService {
    final private Logger logger= LoggerFactory.getLogger(LotService.class);
    final private ValidSpotTypeByVehicleTypeRepository mappingRepository;
    final private SpotRepository spotRepository;
    final private VehicleTypeRepository vehicleTypeRepository;

    @Autowired
    public LotService(
            ValidSpotTypeByVehicleTypeRepository mappingRepository
            , SpotRepository spotRepository, VehicleTypeRepository vehicleTypeRepository
    ) {
        this.mappingRepository = mappingRepository;
        this.spotRepository = spotRepository;
        this.vehicleTypeRepository = vehicleTypeRepository;
    }

    public Optional<ParkingResponse> parkVehicle(ParkingRequest parkingRequest) throws DataDuplication {
        verifySpotUsage(parkingRequest);

        var spotMapping=mappingRepository
                .findViewById_VehicleType_Name(parkingRequest.type());
        for(var mapping :spotMapping) {
            var availableSpots=this.spotRepository
                .findByVehicleIdIsNullAndType_Name(mapping.spotType());
            if(availableSpots.size() >= mapping.slots()){
                return chooseSpotAndPersist(parkingRequest, mapping, availableSpots);
            }
        }
        return Optional.empty();
    }

    private Optional<ParkingResponse> chooseSpotAndPersist(
            ParkingRequest parkingRequest
            , SpotByVehicleRecord mapping
            , List<Spot> availableSpots
            ) {
        var spotsToUpdate=availableSpots.stream()
                .limit(mapping.slots())
                .map(spot -> spot.park(parkingRequest.vehicleId()))
                .collect(Collectors.toList());
        return persistParkingSpot(parkingRequest, mapping, spotsToUpdate);
    }

    private Optional<ParkingResponse> persistParkingSpot(
            ParkingRequest parkingRequest
            , SpotByVehicleRecord mapping
            , List<Spot> spotsToUpdate) {
        spotRepository.saveAllAndFlush(spotsToUpdate);
        return Optional.of(
                new ParkingResponse(
                        mapping.spotType()
                        , parkingRequest.vehicleId()
                        , spotsToUpdate.stream()
                        .map(Spot::getId)
                        .collect(Collectors.toSet())
                )
        );
    }

    private void verifySpotUsage(ParkingRequest parkingRequest) throws DataDuplication {
        if(this.spotRepository
                .existsByVehicleIdIs(parkingRequest.vehicleId())){
            ThrowerFactory.throwDataDuplication(
                    "Vehicle is parked"
                    , parkingRequest.vehicleId()
            );
        }
    }

    public Optional<DepartingResponse> departVehicleById(String vehicleId) {
        List<Spot> spots=this.spotRepository.findByVehicleIdIs(vehicleId);
        Set<String> spotIds=spots.stream()
                .map(Spot::getId)
                .map(Object::toString)
                .collect(Collectors.toSet());
        if(spotIds.isEmpty()){
            return Optional.empty();
        }
        this.spotRepository.saveAllAndFlush(spots.stream()
            .peek(Spot::depart)
            .toList()
        );
        return Optional.of(new DepartingResponse(OffsetDateTime.now(),vehicleId, spotIds));
    }

    public Set<SpotTypeStatusResponse> remainingSpotsSate(String vehicleType) {
        return this.mappingRepository.findViewById_VehicleType_Name(vehicleType).stream()
                .map( mapping -> {
                    long count = this.spotRepository.countByVehicleIdIsNullAndType_Name(
                            mapping.spotType()
                    );
                    return new SpotTypeStatusResponse(
                            mapping.spotType()
                            , count/mapping.slots());
                })
                .collect(Collectors.toSet());
        
    }

    public Optional<SpotTypeStatusResponse> remainingSpotsByVehicleTypeState(String vehicleType) {
        if(this.vehicleTypeRepository.findByName(vehicleType).isEmpty()){
            return Optional.empty();
        }

        var spotState=this.remainingSpotsSate(vehicleType);
        return Optional.of(
                new SpotTypeStatusResponse(
                    vehicleType
                    , spotState.stream()
                        .map(SpotTypeStatusResponse::remainingSpots)
                        .reduce(0L,Long::sum)
                )
        );
    }
    
    public Optional<LotStateResponse> lotStateByVehicleType(String vehicleType) {
        if(this.vehicleTypeRepository.findViewByName(vehicleType).isEmpty()){
            return Optional.empty();
        }

        return this.remainingSpotsByVehicleTypeState(vehicleType)
                    .map(spotTypeStatusResponse ->
                            this.createLotStateResponse(Set.of(spotTypeStatusResponse))
                    );
    }

    public Optional<LotStateResponse> lotState() {
        var state=this.vehicleTypeRepository.findAllViewBy().stream()
                .map(VehicleTypeRecord::name)
                .map(this::remainingSpotsByVehicleTypeState)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        if(state.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(this.createLotStateResponse(state));
    }

    private LotStateResponse createLotStateResponse(Set<SpotTypeStatusResponse> state) {
        return new LotStateResponse(OffsetDateTime.now(), state);
    }

    public Set<SpotRecord> takenSpotsByVehicleType(String vehicleType) {
        return new HashSet<>(this.spotRepository.findViewByVehicleIdIsNotNullAndType_NameIn(
                this.mappingRepository
                        .findViewById_VehicleType_Name(vehicleType).stream()
                        .map(SpotByVehicleRecord::spotType)
                        .collect(Collectors.toSet())
        ));
    }
}
