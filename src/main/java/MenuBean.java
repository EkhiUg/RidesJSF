import java.io.Serializable;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named("menu")
@ViewScoped
public class MenuBean implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	public MenuBean() {
		System.out.println("[DEBUG] MenuBean - Constructor called");
	}
	
	public String login() {
		System.out.println("login");
		return "Login";
	}
	
	public String register() {
		System.out.println("register");
		return "Register";
	}
	
}
