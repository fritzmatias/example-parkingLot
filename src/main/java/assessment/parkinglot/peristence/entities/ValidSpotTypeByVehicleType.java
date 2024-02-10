package assessment.parkinglot.peristence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

@Entity
public class ValidSpotTypeByVehicleType {
    @EmbeddedId
    protected ValidSpotTypeByVehicleTypeId id;

    @Min(1)
    @Column(nullable = false)
    protected int slots;

    protected ValidSpotTypeByVehicleType() {
    }

    public ValidSpotTypeByVehicleType(VehicleType vehicleType, SpotType spotType, Integer slots) {
        this.id=new ValidSpotTypeByVehicleTypeId(vehicleType,spotType);
        this.id.vehicleType = vehicleType;
        this.id.spotType = spotType;
        this.slots = slots;
    }

    public VehicleType getVehicleType() {
        return this.id.vehicleType;
    }

    public SpotType getSpotType() {
        return this.id.spotType;
    }

    public Integer getSlots() {
        return slots;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValidSpotTypeByVehicleType that)) return false;

        if (getSlots() != that.getSlots()) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + getSlots();
        return result;
    }
}
