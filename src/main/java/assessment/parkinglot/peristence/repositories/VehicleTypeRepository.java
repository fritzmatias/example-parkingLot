package assessment.parkinglot.peristence.repositories;

import assessment.parkinglot.model.VehicleTypeRecord;
import assessment.parkinglot.peristence.entities.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleTypeRepository extends JpaRepository<VehicleType, String> {
    public Optional<VehicleType> findByName(String name);
    public Optional<VehicleTypeRecord> findViewByName(String name);
    public List<VehicleTypeRecord> findAllViewBy();
}
