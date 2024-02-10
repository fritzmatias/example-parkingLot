package assessment.parkinglot.peristence.repositories;

import assessment.parkinglot.model.SpotByVehicleRecord;
import assessment.parkinglot.peristence.entities.ValidSpotTypeByVehicleType;
import assessment.parkinglot.peristence.entities.ValidSpotTypeByVehicleTypeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ValidSpotTypeByVehicleTypeRepository
extends JpaRepository<ValidSpotTypeByVehicleType, ValidSpotTypeByVehicleTypeId>
{
    @Query("""
            SELECT new assessment.parkinglot.model.SpotByVehicleRecord(
                    t.id.vehicleType.name
                    , t.id.spotType.name
                    , t.slots
                )
            FROM ValidSpotTypeByVehicleType t
            WHERE
                t.id.vehicleType.name = :vehicleType
    """)
    public List<SpotByVehicleRecord> findViewById_VehicleType_Name(
            @Param("vehicleType") String vehicleType
    );

    public Optional<ValidSpotTypeByVehicleType> findFirstById_VehicleType_NameAndId_SpotType_Name(
            String vehicleType, String SpotType);


}
