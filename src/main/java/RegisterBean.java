import java.io.Serializable;

import businessLogic.BLFacade;
import domain.User;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named("register")
@ViewScoped
public class RegisterBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String email;
	private String name;
	private String password;
	private String confirmPassword;
	private String userType;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String register() {
		try {
			// Validate input
			if (email == null || email.trim().isEmpty()) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email is required", null));
				return null;
			}
			
			if (name == null || name.trim().isEmpty()) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Name is required", null));
				return null;
			}
			
			if (password == null || password.trim().isEmpty()) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password is required", null));
				return null;
			}
			
			if (!password.equals(confirmPassword)) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords do not match", null));
				return null;
			}
			
			if (userType == null || userType.trim().isEmpty()) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "User type is required", null));
				return null;
			}

			// Get business logic facade
			BLFacade facade = FacadeBean.getBusinessLogic();

			// Register user
			User user = facade.registerUser(email, name, password, userType);
			
			if (user != null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Registration successful! Please login.", null));
				return "login";
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "User with this email already exists", null));
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + e.getMessage(), null));
			return null;
		}
	}
	
	public String cancel() {
		return "menu";
	}

}