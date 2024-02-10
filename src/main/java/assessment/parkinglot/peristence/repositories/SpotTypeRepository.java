package assessment.parkinglot.peristence.repositories;

import assessment.parkinglot.model.SpotTypeRecord;
import assessment.parkinglot.peristence.entities.SpotType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpotTypeRepository extends JpaRepository<SpotType, String> {
    public Optional<SpotType> findFirstByName(String type);
    public Optional<SpotTypeRecord> findFirstViewByName(String type);
}
