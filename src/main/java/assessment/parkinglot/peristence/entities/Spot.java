package assessment.parkinglot.peristence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

@Entity
public class Spot {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    protected int id;
    @NotNull
    @ManyToOne
    protected SpotType type;
    @Column(nullable = true)
    protected String vehicleId;
    @Version
    protected OffsetDateTime asOf;

    protected Spot() {}

    public Spot(SpotType spotTypeEntity) {
        this.type=spotTypeEntity;
    }

    public Spot(int id, SpotType type, String vehicleId) {
        this.id = id;
        this.type = type;
        this.vehicleId = vehicleId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    protected Spot setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
        return this;
    }

    public int getId() {
        return id;
    }

    public SpotType getType() {
        return type;
    }

    public OffsetDateTime getAsOf() {
        return asOf;
    }

    public Spot park(String vehicleId) {
        return this.setVehicleId(vehicleId);
    }
    public Spot depart() {
        return this.setVehicleId(null);
    }
}
