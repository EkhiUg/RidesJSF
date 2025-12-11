import java.io.Serializable;
import java.util.Date;

import org.primefaces.event.SelectEvent;

import businessLogic.BLFacade;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;

import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("createRide")
@ViewScoped
public class CreateRideBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private LoginBean loginBean;
	
	private String depart;
	private String arrival;
	private int seats;
	private int cash;
	private Date data;

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public CreateRideBean() {
	}

	public String getDepart() {
		return depart;
	}

	public void setDepart(String depart) {
		this.depart = depart;
	}

	public String getArrival() {
		return arrival;
	}

	public void setArrival(String arrival) {
		this.arrival = arrival;
	}

	public int getSeats() {
		return seats;
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}

	public int getCash() {
		return cash;
	}

	public void setCash(int cash) {
		this.cash = cash;
	}


	public void onDateSelect(SelectEvent event) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Data aukeratua: " + event.getObject()));
	}

	public String rideCreate() {
		try {
			// Check if user is logged in and is a driver
			if (loginBean == null || !loginBean.isLoggedIn()) {
				FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "You must be logged in to create a ride", null));
				return "login";
			}
			
			if (!loginBean.isDriver()) {
				FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Only drivers can create rides", null));
				return "index";
			}
			
			// Get the logged-in driver's email
			String driverEmail = loginBean.getCurrentUser().getEmail();
			
			// Get business logic facade
			BLFacade facade = FacadeBean.getBusinessLogic();

			// Create ride with the logged-in driver's email
			facade.createRide(depart, arrival, data, seats, cash, driverEmail);

			// Success message
			FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Ride created successfully", null));

			return null; // Stay on page to create more rides

		} catch (Exception e) {
			// Handle errors (e.g., past dates, database errors)
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + e.getMessage(), null));
			return null;
		}
	}

}