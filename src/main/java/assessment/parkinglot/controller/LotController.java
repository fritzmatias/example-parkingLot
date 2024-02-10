package assessment.parkinglot.controller;

import assessment.parkinglot.controller.model.*;
import assessment.parkinglot.errors.DataDuplication;
import assessment.parkinglot.errors.DataNotFound;
import assessment.parkinglot.errors.ThrowerFactory;
import assessment.parkinglot.services.LotService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@RestController
public class LotController {
    private final LotService lotService;

    public static class endpoint {
        public final static String parkVehicle="/parkVehicle";
        public final static String departVehicle="/departVehicle";
        public final static String remainingSpots ="/remainingSpots";
        public final static String takenParkingSpots="/takenParkingSpots";
    }

    @Autowired
    public LotController(LotService lotService) {
        this.lotService = lotService;
    }

    @PostMapping(endpoint.parkVehicle)
    public ParkingResponse parkingVehicle(@Valid @RequestBody ParkingRequest parkingRequest)
            throws Exception, DataDuplication {
        return this.lotService.parkVehicle(parkingRequest).orElseThrow(
            ThrowerFactory.DataNotFoundSupplier(
                "Parking lot not found"
                , parkingRequest.type()+","+parkingRequest.vehicleId()
            )
        );
    }

    @PostMapping(endpoint.departVehicle)
    public DepartingResponse departVehicle(@Valid @RequestBody DepartingRequest departingRequest)
            throws DataNotFound {
        return this.lotService.departVehicleById(
            departingRequest.parkingId()
        ).orElseThrow(
            ThrowerFactory.DataNotFoundSupplier(
                "Vehicle not found inside the lot"
                , departingRequest.parkingId()
            )
        );
    }

    @GetMapping(endpoint.remainingSpots)
    public LotStateResponse remainingSots(@RequestParam(value = "vt", required = false) String vehicleType)
            throws DataNotFound {
        if(vehicleType == null || vehicleType.isBlank()){
            return this.lotService.lotState().orElseThrow(
                    ThrowerFactory.DataNotFoundSupplier(
                            "Vehicle not found"
                            , "empty request"
                    )
            );
        }
        return this.lotService.lotStateByVehicleType(vehicleType).orElseThrow(
                ThrowerFactory.DataNotFoundSupplier(
                        "Vehicle not found"
                        , vehicleType
                        )
        );
    }

    @GetMapping(endpoint.takenParkingSpots)
    public LotSpotStateResponse takenParkingSpots(@RequestParam(value = "vt")@Valid @NotBlank String vehicleType) {
        return new LotSpotStateResponse(
                OffsetDateTime.now(),
                this.lotService.takenSpotsByVehicleType(vehicleType)
        );
    }

}
