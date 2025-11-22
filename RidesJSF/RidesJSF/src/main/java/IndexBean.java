import java.io.Serializable;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named("index")
@ViewScoped
public class IndexBean implements Serializable {

	
	private static final long serialVersionUID = 1L;
	private String language = "en";
	
	public IndexBean() {
		System.out.println("[DEBUG] IndexBean - Constructor called");
	}
	
	public String createRide() {
		System.out.println("createRide");
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
