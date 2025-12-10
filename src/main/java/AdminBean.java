import java.io.Serializable;
import java.util.List;

import businessLogic.BLFacade;
import domain.Driver;
import domain.User;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named("admin")
@ViewScoped
public class AdminBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<Driver> drivers;
	private String selectedDriverEmail;
	private Boolean isAdmin;
	
	public AdminBean() {
		System.out.println("[DEBUG] AdminBean - Constructor called");
	}
	
	@PostConstruct
	public void init() {
		System.out.println("[DEBUG] AdminBean - init() called");
		checkAdminStatus();
		if (isAdmin) {
			loadDrivers();
		}
	}
	
	/**
	 * Check admin status once and cache it
	 */
	private void checkAdminStatus() {
		FacesContext context = FacesContext.getCurrentInstance();
		User currentUser = (User) context.getExternalContext().getSessionMap().get("currentUser");
		
		if (currentUser != null) {
			System.out.println("[DEBUG] AdminBean - User type: " + currentUser.getUserType());
			isAdmin = currentUser.getUserType() != null && 
					  currentUser.getUserType().toString().equals("ADMIN");
		} else {
			isAdmin = false;
		}
	}
	
	/**
	 * Load all drivers from the database
	 */
	public void loadDrivers() {
		try {
			BLFacade facade = FacadeBean.getBusinessLogic();
			drivers = facade.getAllDrivers();
			System.out.println("[DEBUG] AdminBean - Loaded " + drivers.size() + " drivers");
		} catch (Exception e) {
			System.err.println("[ERROR] AdminBean - Error loading drivers: " + e.getMessage());
			e.printStackTrace();
			addMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to load drivers");
		}
	}
	
	/**
	 * Delete the selected driver
	 */
	public void deleteDriver() {
		if (selectedDriverEmail == null || selectedDriverEmail.isEmpty()) {
			addMessage(FacesMessage.SEVERITY_WARN, "Warning", "Please select a driver to delete");
			return;
		}
		
		try {
			BLFacade facade = FacadeBean.getBusinessLogic();
			boolean success = facade.deleteDriver(selectedDriverEmail);
			
			if (success) {
				addMessage(FacesMessage.SEVERITY_INFO, "Success", 
					"Driver " + selectedDriverEmail + " and their rides have been deleted");
				loadDrivers(); // Reload the list
				selectedDriverEmail = null; // Clear selection
			} else {
				addMessage(FacesMessage.SEVERITY_ERROR, "Error", 
					"Failed to delete driver " + selectedDriverEmail);
			}
		} catch (Exception e) {
			System.err.println("[ERROR] AdminBean - Error deleting driver: " + e.getMessage());
			e.printStackTrace();
			addMessage(FacesMessage.SEVERITY_ERROR, "Error", 
				"An error occurred while deleting the driver");
		}
	}
	
	/**
	 * Check if the current user is an admin (cached value)
	 */
	public boolean isAdmin() {
		return isAdmin != null && isAdmin;
	}
	
	/**
	 * Navigate back to index
	 */
	public String backToMenu() {
		return "index";
	}
	
	/**
	 * Add a faces message
	 */
	private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, 
			new FacesMessage(severity, summary, detail));
	}
	
	// Getters and Setters
	public List<Driver> getDrivers() {
		return drivers;
	}
	
	public void setDrivers(List<Driver> drivers) {
		this.drivers = drivers;
	}
	
	public String getSelectedDriverEmail() {
		return selectedDriverEmail;
	}
	
	public void setSelectedDriverEmail(String selectedDriverEmail) {
		this.selectedDriverEmail = selectedDriverEmail;
	}
}
