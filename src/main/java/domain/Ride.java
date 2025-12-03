package domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Table(name = "ride")
public class Ride implements Serializable {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rideNumber;

    @Column(name = "from_city") // Evita palabra reservada
    private String from;

    @Column(name = "to_city") // Evita palabra reservada
    private String to;

    @Temporal(TemporalType.DATE)
    @Column(name = "ride_date")
    private Date date;

    private int nPlaces;
    private float price;

    @ManyToOne
    @JoinColumn(name = "driver_email") // Clave foránea hacia Driver
    private Driver driver;  

    public Ride() {
        super();
    }

    public Ride(Integer rideNumber, String from, String to, Date date, int nPlaces, float price, Driver driver) {
        this.rideNumber = rideNumber;
        this.from = from;
        this.to = to;
        this.date = date;
        this.nPlaces = nPlaces;
        this.price = price;
        this.driver = driver;
    }

    public Ride(String from, String to, Date date, int nPlaces, float price, Driver driver) {
        this.from = from;
        this.to = to;
        this.date = date;
        this.nPlaces = nPlaces;
        this.price = price;
        this.driver = driver;
    }

    // Getters y setters
    public Integer getRideNumber() { return rideNumber; }
    public void setRideNumber(Integer rideNumber) { this.rideNumber = rideNumber; }

    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public int getnPlaces() { return nPlaces; }
    public void setnPlaces(int nPlaces) { this.nPlaces = nPlaces; }

    public float getPrice() { return price; }
    public void setPrice(float price) { this.price = price; }

    public Driver getDriver() { return driver; }
    public void setDriver(Driver driver) { this.driver = driver; }

    @Override
    public String toString() {
        return rideNumber + ";" + from + ";" + to + ";" + date;  
    }
}
