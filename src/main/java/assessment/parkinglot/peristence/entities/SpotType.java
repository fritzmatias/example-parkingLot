package assessment.parkinglot.peristence.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;

@Entity
public class SpotType {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    protected Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "id.spotType", fetch = FetchType.EAGER)
    private Collection<ValidSpotTypeByVehicleType> vehicles;

    protected SpotType() {
        this.vehicles=new ArrayList<>();
    }

    public SpotType(String name) {
        this.name = name;
        this.vehicles=new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public Collection<ValidSpotTypeByVehicleType> getVehicles() {
        return vehicles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpotType spotType)) return false;

        if (!getId().equals(spotType.getId())) return false;
        if (!getName().equals(spotType.getName())) return false;
        return getVehicles().equals(spotType.getVehicles());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getVehicles().hashCode();
        return result;
    }
}
