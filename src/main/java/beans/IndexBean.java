package beans;
import java.io.Serializable;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("index")
@ViewScoped
public class IndexBean implements Serializable {

	
	private static final long serialVersionUID = 1L;
	private String language = "en";
	
	@Inject
	private LoginBean loginBean;
	
	public IndexBean() {
		System.out.println("[DEBUG] IndexBean - Constructor called");
	}
	
	public String createRide() {
		System.out.println("createRide");
		
		// Check if user is logged in
		if (loginBean == null || !loginBean.isLoggedIn()) {
			FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "You must be logged in to create a ride", null));
			return "login";
		}
		
		// Check if user is a driver
		if (!loginBean.isDriver()) {
			FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Only drivers can create rides", null));
			return null;
		}
		
		return "CreateRide";
	}
	
	public String queryRides() {
		System.out.println("queryRides");
		return "QueryRides";
	}
	
	public String getLanguage() {
		return language;
	}
	
	public void setLanguage(String language) {
		this.language = language;
	}
}
