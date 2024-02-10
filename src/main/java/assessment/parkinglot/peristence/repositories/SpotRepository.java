package assessment.parkinglot.peristence.repositories;

import assessment.parkinglot.model.SpotRecord;
import assessment.parkinglot.peristence.entities.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface SpotRepository extends JpaRepository<Spot, String> {

    public long countByVehicleIdIsNullAndType_Name(String spotType);

    public List<Spot> findByVehicleIdIsNullAndType_Name(String spotType);

    @Query("""
        SELECT new assessment.parkinglot.model.SpotRecord(
            spot.id,
            spot.type.name,
            spot.vehicleId,
            spot.asOf
        )
        FROM Spot spot
        WHERE spot.type.name in :spotTypes AND spot.vehicleId is not null
    """)
    public List<SpotRecord> findViewByVehicleIdIsNotNullAndType_NameIn(@Param("spotTypes") Collection<String> spotTypes);
    public boolean existsByVehicleIdIs(String vehicleId);
    public List<Spot> findByVehicleIdIs(String vehicleId);
    public List<Spot> findByType_Name(String spotType);
    public boolean existsByType_Name(String spotType);

    Object findViewByVehicleIdIs(String s);
}
