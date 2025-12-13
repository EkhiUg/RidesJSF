package businessLogic;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import configuration.ConfigXML;
import dataAccess.HibernateDataAccess;
import domain.Ride;
import domain.Driver;
import domain.User;
import exceptions.RideMustBeLaterThanTodayException;
import exceptions.RideAlreadyExistException;

/**
 * It implements the business logic as a web service.
 */

public class BLFacadeImplementation  implements BLFacade {
	HibernateDataAccess dbManager;

	public BLFacadeImplementation()  {		
		System.out.println("Creating BLFacadeImplementation instance");
		
		
		    dbManager=new HibernateDataAccess();
		    
		//dbManager.close();

		
	}
	
    public BLFacadeImplementation(HibernateDataAccess da)  {
		
		System.out.println("Creating BLFacadeImplementation instance with DataAccess parameter");
		ConfigXML c=ConfigXML.getInstance();
		
		dbManager=da;		
	}
    
    
    /**
     * {@inheritDoc}
     */
    public List<String> getDepartCities(){
    	dbManager.open();	
		
		 List<String> departLocations=dbManager.getDepartCities();		

		dbManager.close();
		
		return departLocations;
    	
    }
    /**
     * {@inheritDoc}
     */
	public List<String> getDestinationCities(String from){
		dbManager.open();	
		
		 List<String> targetCities=dbManager.getArrivalCities(from);		

		dbManager.close();
		
		return targetCities;
	}

	/**
	 * {@inheritDoc}
	 */
   
   public Ride createRide( String from, String to, Date date, int nPlaces, float price, String driverEmail ) throws RideMustBeLaterThanTodayException, RideAlreadyExistException{
	   
		dbManager.open();
		Ride ride=dbManager.createRide(from, to, date, nPlaces, price, driverEmail);		
		dbManager.close();
		return ride;
   };
	
   /**
    * {@inheritDoc}
    */

	public List<Ride> getRides(String from, String to, Date date){
		dbManager.open();
		List<Ride>  rides=dbManager.getRides(from, to, date);
		dbManager.close();
		return rides;
	}

    
	/**
	 * {@inheritDoc}
	 */

	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date){
		dbManager.open();
		List<Date>  dates=dbManager.getThisMonthDatesWithRides(from, to, date);
		dbManager.close();
		return dates;
	}
	
	
	public void close() {
		HibernateDataAccess dB4oManager=new HibernateDataAccess();

		dB4oManager.close();

	}

	/**
	 * {@inheritDoc}
	 */

	 public void initializeBD(){
    	dbManager.open();
		dbManager.initializeDB();
		dbManager.close();
	}
	 
	/**
	 * {@inheritDoc}
	 */
	public User registerUser(String email, String name, String password, String userType) {
		dbManager.open();
		User user = dbManager.registerUser(email, name, password, userType);
		dbManager.close();
		return user;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public User loginUser(String email, String password) {
		dbManager.open();
		User user = dbManager.loginUser(email, password);
		dbManager.close();
		return user;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Driver> getAllDrivers() {
		dbManager.open();
		List<Driver> drivers = dbManager.getAllDrivers();
		dbManager.close();
		return drivers;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean deleteDriver(String driverEmail) {
		dbManager.open();
		boolean result = dbManager.deleteDriver(driverEmail);
		dbManager.close();
		return result;
	}
	
	public List<Ride> getAllRides(String seats){
		dbManager.open();
		List<Ride> rides = dbManager.getAllRides(seats);
		dbManager.close();
		return rides;
		}

}

