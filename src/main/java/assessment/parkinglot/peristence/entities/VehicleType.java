package assessment.parkinglot.peristence.entities;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.HashSet;

@Entity
public class VehicleType {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    protected Integer id;

    @Column(nullable = false, unique = true)
    protected String name;

    @OneToMany(mappedBy = "id.vehicleType" , fetch = FetchType.EAGER)
    protected Collection<ValidSpotTypeByVehicleType> spots;

    protected VehicleType(){
        this.spots=new HashSet<>();
    }
    public VehicleType(String name){
        this.name = name;
        this.spots=new HashSet<>();
    }

    public Integer getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public Collection<ValidSpotTypeByVehicleType> getSpots() {
        return spots;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VehicleType that)) return false;

        if (!getId().equals(that.getId())) return false;
        if (!getName().equals(that.getName())) return false;
        return getSpots().equals(that.getSpots());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getSpots().hashCode();
        return result;
    }
}
