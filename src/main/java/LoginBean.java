import java.io.Serializable;

import businessLogic.BLFacade;
import domain.User;
import domain.UserType;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

@Named("login")
@SessionScoped
public class LoginBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String email;
	private String password;
	private User currentUser;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
	
	public boolean isLoggedIn() {
		return currentUser != null;
	}
	
	public boolean isCustomer() {
		return currentUser != null && currentUser.getUserType() == UserType.CUSTOMER;
	}
	
	public boolean isAdmin() {
		return currentUser != null && currentUser.getUserType() == UserType.ADMIN;
	}
	
	public boolean isDriver() {
		return currentUser != null && currentUser.getUserType() == UserType.DRIVER;
	}

	public String login() {
		try {
			// Validate input
			if (email == null || email.trim().isEmpty()) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email is required", null));
				return null;
			}
			
			if (password == null || password.trim().isEmpty()) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password is required", null));
				return null;
			}

			// Get business logic facade
			BLFacade facade = FacadeBean.getBusinessLogic();

			// Authenticate user
			User user = facade.loginUser(email, password);
			
			if (user != null) {
				currentUser = user;
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome " + user.getName() + "!", null));
				return "Index"; // Redirect to main page
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid email or password", null));
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + e.getMessage(), null));
			return null;
		}
	}
	
	public String logout() {
		currentUser = null;
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "You have been logged out", null));
		return "Menu";
	}
	
	public String cancel() {
		return "Menu";
	}

}
