package beans;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import org.primefaces.event.SelectEvent;

import businessLogic.BLFacade;
import domain.Ride; 

@Named("eserlekuBidaiak")
@SessionScoped
public class eserlekuBidaiakBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String seats;
    private List<Ride> rides;

    // Constructor vacío
    public eserlekuBidaiakBean() {
    }

    // Se ejecuta automáticamente al cargar la página para rellenar el primer desplegable [cite: 15, 18]
    @PostConstruct
    public void init() {
        // Inicializamos listas vacías para evitar NullPointer
        rides = new ArrayList<>();
    }
    


    // EVENTO 2: Cuando el usuario selecciona una cantidad de seats
    public void onQueryRides(SelectEvent<String> event) {
        this.seats = event.getObject();
        try {
            BLFacade facade = FacadeBean.getBusinessLogic();
            // Buscamos los viajes en la BD
            rides = facade.getAllRides(seats);
            
            if (rides.isEmpty()) {
                 FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("No hay viajes para esta fecha."));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String close() {
    	return "index";
    }
    
    public String kargatu() {
    	System.out.println("[DEBUG] kargatu() llamado con seats: " + seats);
    	setSeats(seats);
    	try {
            BLFacade facade = FacadeBean.getBusinessLogic();
            // Load rides based on the number of seats
            rides = facade.getAllRides(seats);
            
            if (rides == null || rides.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Ez dira bidaiak aurkitu espezifikazio honekin"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Errorea", e.getMessage()));
        }
    	return "kargatu";
    	
    }

    // --- Getters y Setters ---

    public String getSeats() { return seats; }
    public void setSeats(String seats) { this.seats = seats; }

    public List<Ride> getRides() { return rides; }
    public void setRides(List<Ride> rides) { this.rides = rides; }
}