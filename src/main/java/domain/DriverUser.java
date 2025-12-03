package domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.persistence.*;

@Entity
public class DriverUser extends User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<Ride> rides = new Vector<Ride>();
	
	public DriverUser() {
		super();
	}
	
	public DriverUser(String email, String name, String password) {
		super(email, name, password, UserType.DRIVER);
	}
	
	public List<Ride> getRides() {
		return rides;
	}
	
	public void setRides(List<Ride> rides) {
		this.rides = rides;
	}
	
	/**
	 * This method creates a ride for the driver
	 * 
	 * @param from origin location
	 * @param to destination location
	 * @param date the date of the ride
	 * @param nPlaces number of available seats
	 * @param price price of the ride
	 * @return Ride the created ride
	 */
	public Ride addRide(String from, String to, Date date, int nPlaces, float price) {
		Ride ride = new Ride(from, to, date, nPlaces, price, null);
		rides.add(ride);
		return ride;
	}
	
	/**
	 * This method checks if the ride already exists for that driver
	 * 
	 * @param from the origin location 
	 * @param to the destination location 
	 * @param date the date of the ride 
	 * @return true if the ride exists and false in other case
	 */
	public boolean doesRideExists(String from, String to, Date date) {
		for (Ride r : rides) {
			if ((java.util.Objects.equals(r.getFrom(), from)) 
				&& (java.util.Objects.equals(r.getTo(), to)) 
				&& (java.util.Objects.equals(r.getDate(), date)))
				return true;
		}
		return false;
	}
	
	public Ride removeRide(String from, String to, Date date) {
		boolean found = false;
		int index = 0;
		Ride r = null;
		while (!found && index < rides.size()) {
			r = rides.get(index);
			if ((java.util.Objects.equals(r.getFrom(), from)) 
				&& (java.util.Objects.equals(r.getTo(), to)) 
				&& (java.util.Objects.equals(r.getDate(), date)))
				found = true;
			else
				index++;
		}
		
		if (found) {
			rides.remove(index);
			return r;
		} else {
			return null;
		}
	}
	
	@Override
	public String toString() {
		return "DriverUser [email=" + getEmail() + ", name=" + getName() + ", rides=" + rides.size() + "]";
	}
}
