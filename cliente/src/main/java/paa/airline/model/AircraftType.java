package paa.airline.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name="AIRCRAFTTYPES")
public class AircraftType {
	@Id
	@GeneratedValue
    private Long id;
	
    private String manufacturer;
    
    private String model;
    
    private int seatRows;
    
    private int seatColumns;


    public AircraftType() { }

    public AircraftType(String manufacturer, String model, int seatRows, int seatColumns) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.seatRows = seatRows;
        this.seatColumns = seatColumns;
    }
	
    public Long getId() {
        return id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getSeatRows() {
        return seatRows;
    }

    public void setSeatRows(int rows) {
        this.seatRows = rows;
    }

    public int getSeatColumns() {
        return seatColumns;
    }

    public void setSeatColumns(int columns) {
        this.seatColumns = columns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AircraftType)) return false;
        AircraftType otherAircraft = (AircraftType) o;
        return Objects.equals(id, otherAircraft.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s %s", this.manufacturer, this.model);

    }
}
