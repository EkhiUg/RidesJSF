import java.io.Serializable;
import java.util.Date;

import org.primefaces.event.SelectEvent;

import businessLogic.BLFacade;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;

import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named("Register")
@ViewScoped
public class RegisterBean implements Serializable {
	private String email;
	private String name;
	private String role;

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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	

	public String userCreate() {
		try {
			// 1. Obtener la lógica desde el FacadeBean [cite: 184]
			BLFacade facade = FacadeBean.getBusinessLogic();

			// 2. Llamar al método createRide (asumo que existe en tu jar antiguo)
			// Nota: Como no tenemos Login aún, pondremos un conductor "Test Driver" fijo.
			// facade.createRide(depart, arrival, data, seats, cash, "driver1@gmail.com");

			// 3. Mensaje de éxito
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Successfully registered"));

			return "ok"; // O null para quedarse en la página

		} catch (Exception e) {
			// Capturar errores (ej: fechas pasadas, errores de BD)
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + e.getMessage(), null));
			return null;
		}
	}

}