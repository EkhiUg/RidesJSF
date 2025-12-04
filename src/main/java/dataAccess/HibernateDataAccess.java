package dataAccess;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import configuration.ConfigXML;
import configuration.UtilDate;
import domain.Driver;
import domain.Ride;
import domain.User;
import domain.Customer;
import domain.Admin;
import domain.DriverUser;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

public class HibernateDataAccess {
	private EntityManager db;
	private EntityManagerFactory emf;

	ConfigXML c = ConfigXML.getInstance();

	public HibernateDataAccess() {
		open();

		initializeDB();

		// NO CERRAR AQUI
		System.out.println("HibernateDataAccess created => using persistence-unit 'iraunkortasuna'");
		System.out.println("DB initialized? " + c.isDatabaseInitialized());

	}

	public HibernateDataAccess(EntityManager db) {
		this.db = db;
	}

	public void initializeDB() {
		try {
			// Check if database is already initialized by checking for existing drivers
			Driver existingDriver = db.find(Driver.class, "driver1@gmail.com");
			if (existingDriver != null) {
				System.out.println("Database already initialized, skipping initialization");
				return;
			}
			
			db.getTransaction().begin();

			Calendar today = Calendar.getInstance();

			int month = today.get(Calendar.MONTH);
			int year = today.get(Calendar.YEAR);
			if (month == 12) {
				month = 1;
				year += 1;
			}

			Driver driver1 = new Driver("driver1@gmail.com", "Aitor Fernandez");
			Driver driver2 = new Driver("driver2@gmail.com", "Ane Gaztañaga");
			Driver driver3 = new Driver("driver3@gmail.com", "Test driver");

			driver1.addRide("Donostia", "Bilbo", UtilDate.newDate(year, month, 15), 4, 7);
			driver1.addRide("Donostia", "Gazteiz", UtilDate.newDate(year, month, 6), 4, 8);
			driver1.addRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 25), 4, 4);
			driver1.addRide("Donostia", "Iruña", UtilDate.newDate(year, month, 7), 4, 8);

			driver2.addRide("Donostia", "Bilbo", UtilDate.newDate(year, month, 15), 3, 3);
			driver2.addRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 25), 2, 5);
			driver2.addRide("Eibar", "Gasteiz", UtilDate.newDate(year, month, 6), 2, 5);

			driver3.addRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 14), 1, 3);

			db.persist(driver1);
			db.persist(driver2);
			db.persist(driver3);

			db.getTransaction().commit();
			System.out.println("Db initialized (Hibernate) with sample drivers and rides");
		} catch (Exception e) {
			e.printStackTrace();
			if (db.getTransaction().isActive())
				db.getTransaction().rollback();
			System.err.println("Error initializing database: " + e.getMessage());
		}
	}

	public List<String> getDepartCities() {
		TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.from FROM Ride r WHERE r.from IS NOT NULL ORDER BY r.from", String.class);
		List<String> cities = query.getResultList();
		return cities;
	}

	public List<String> getArrivalCities(String from) {
		TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.to FROM Ride r WHERE r.from=?1 AND r.to IS NOT NULL ORDER BY r.to",
				String.class);
		query.setParameter(1, from);
		List<String> arrivingCities = query.getResultList();
		return arrivingCities;
	}

	public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverEmail)
			throws RideAlreadyExistException, RideMustBeLaterThanTodayException {
		System.out.println(">> HibernateDataAccess: createRide=> from= " + from + " to= " + to + " driver="
				+ driverEmail + " date " + date);
		try {
			if (new Date().compareTo(date) > 0) {
				throw new RideMustBeLaterThanTodayException(
						ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorRideMustBeLaterThanToday"));
			}
			db.getTransaction().begin();

			// Try to find DriverUser first (new system), fallback to Driver (old system)
			DriverUser driverUser = db.find(DriverUser.class, driverEmail);
			Driver driver = null;
			
			if (driverUser != null) {
				// DriverUser exists (new authentication system)
				// Find or create the Driver entity for ride management
				driver = db.find(Driver.class, driverEmail);
				if (driver == null) {
					// Create Driver entity from DriverUser for ride association
					driver = new Driver(driverEmail, driverUser.getName());
					// DON'T persist yet - let it be persisted with the ride
				}
				
				// Check if ride already exists
				if (driver.doesRideExists(from, to, date)) {
					db.getTransaction().rollback();
					throw new RideAlreadyExistException(
							ResourceBundle.getBundle("Etiquetas").getString("DataAccess.RideAlreadyExist"));
				}
			} else {
				// Fallback to old Driver system (for initial test data)
				driver = db.find(Driver.class, driverEmail);
				if (driver == null) {
					db.getTransaction().rollback();
					throw new NullPointerException("Driver not found: " + driverEmail);
				}
				if (driver.doesRideExists(from, to, date)) {
					db.getTransaction().rollback();
					throw new RideAlreadyExistException(
							ResourceBundle.getBundle("Etiquetas").getString("DataAccess.RideAlreadyExist"));
				}
			}
			
			// Create ride using Driver entity
			Ride ride = driver.addRide(from, to, date, nPlaces, price);
			
			// Persist or merge the driver (which will cascade to the ride)
			if (db.contains(driver)) {
				db.merge(driver);
			} else {
				db.persist(driver);
			}
			
			db.getTransaction().commit();
			System.out.println("Ride created successfully: " + ride);

			return ride;
		} catch (NullPointerException e) {
			if (db.getTransaction().isActive())
				db.getTransaction().rollback();
			System.err.println("Error: Driver not found or null - " + e.getMessage());
			return null;
		}
	}

	public List<Ride> getRides(String from, String to, Date date) {
		System.out.println(">> HibernateDataAccess: getRides=> from= " + from + " to= " + to + " date " + date);

		List<Ride> res = new ArrayList<Ride>();
		TypedQuery<Ride> query = db.createQuery("SELECT r FROM Ride r WHERE r.from=?1 AND r.to=?2 AND r.date=?3 AND r.from IS NOT NULL AND r.to IS NOT NULL AND r.date IS NOT NULL",
				Ride.class);
		query.setParameter(1, from);
		query.setParameter(2, to);
		query.setParameter(3, date);
		List<Ride> rides = query.getResultList();
		for (Ride ride : rides) {
			res.add(ride);
		}
		return res;
	}

	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
		System.out.println(">> HibernateDataAccess: getThisMonthDatesWithRides");
		List<Date> res = new ArrayList<Date>();

		Date firstDayMonthDate = UtilDate.firstDayMonth(date);
		Date lastDayMonthDate = UtilDate.lastDayMonth(date);

		TypedQuery<Date> query = db.createQuery(
				"SELECT DISTINCT r.date FROM Ride r WHERE r.from=?1 AND r.to=?2 AND r.date BETWEEN ?3 and ?4",
				Date.class);

		query.setParameter(1, from);
		query.setParameter(2, to);
		query.setParameter(3, firstDayMonthDate);
		query.setParameter(4, lastDayMonthDate);
		List<Date> dates = query.getResultList();
		for (Date d : dates) {
			res.add(d);
		}
		return res;
	}

	public void open() {
		emf = Persistence.createEntityManagerFactory("iraunkortasuna");
		db = emf.createEntityManager();
		System.out.println("HibernateDataAccess opened => using persistence-unit: iraunkortasuna");
	}

	public void close() {
		if (db != null && db.isOpen())
			db.close();
		if (emf != null && emf.isOpen())
			emf.close();
		System.out.println("HibernateDataAccess closed");
	}
	
	/**
	 * This method registers a new user in the database
	 * 
	 * @param email the user's email
	 * @param name the user's name
	 * @param password the user's password
	 * @param userType the type of user (CUSTOMER, ADMIN, DRIVER)
	 * @return the created user, or null if user already exists
	 */
	public User registerUser(String email, String name, String password, String userType) {
		System.out.println(">> HibernateDataAccess: registerUser=> email= " + email + " name= " + name + " userType= " + userType);
		try {
			db.getTransaction().begin();
			
			// Check if user already exists
			User existingUser = db.find(User.class, email);
			if (existingUser != null) {
				db.getTransaction().rollback();
				System.out.println("User already exists with email: " + email);
				return null;
			}
			
			User user = null;
			switch (userType.toUpperCase()) {
				case "CUSTOMER":
					user = new Customer(email, name, password);
					break;
				case "ADMIN":
					user = new Admin(email, name, password);
					break;
				case "DRIVER":
					user = new DriverUser(email, name, password);
					break;
				default:
					db.getTransaction().rollback();
					System.out.println("Invalid user type: " + userType);
					return null;
			}
			
			db.persist(user);
			db.getTransaction().commit();
			System.out.println("User registered successfully: " + email);
			return user;
			
		} catch (Exception e) {
			e.printStackTrace();
			if (db.getTransaction().isActive())
				db.getTransaction().rollback();
			return null;
		}
	}
	
	/**
	 * This method authenticates a user
	 * 
	 * @param email the user's email
	 * @param password the user's password
	 * @return the authenticated user, or null if credentials are invalid
	 */
	public User loginUser(String email, String password) {
		System.out.println(">> HibernateDataAccess: loginUser=> email= " + email);
		try {
			User user = db.find(User.class, email);
			
			if (user != null && user.getPassword().equals(password)) {
				System.out.println("User authenticated successfully: " + email);
				return user;
			} else {
				System.out.println("Invalid credentials for email: " + email);
				return null;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
