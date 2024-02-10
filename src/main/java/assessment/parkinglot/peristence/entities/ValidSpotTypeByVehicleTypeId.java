package assessment.parkinglot.peristence.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;

@Embeddable
public class ValidSpotTypeByVehicleTypeId implements Serializable {
    @ManyToOne
    protected VehicleType vehicleType;
    @ManyToOne
    protected SpotType spotType;

    protected ValidSpotTypeByVehicleTypeId() {}
    public ValidSpotTypeByVehicleTypeId(VehicleType vehicleType, SpotType spotType) {
        this.vehicleType = vehicleType;
        this.spotType = spotType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValidSpotTypeByVehicleTypeId that)) return false;

        if (!vehicleType.equals(that.vehicleType)) return false;
        return spotType.equals(that.spotType);
    }

    @Override
    public int hashCode() {
        int result = vehicleType.hashCode();
        result = 31 * result + spotType.hashCode();
        return result;
    }
}
