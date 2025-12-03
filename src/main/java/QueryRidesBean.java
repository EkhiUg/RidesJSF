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

@Named("queryRides")
@ViewScoped
public class QueryRidesBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String selectedDepartCity;
    private String selectedArrivalCity;
    private List<String> departCities;
    private List<String> arrivalCities;
    private Date date;
    private List<Ride> rides; // Lista para guardar los viajes encontrados

    // Constructor vacío
    public QueryRidesBean() {
    }

    // Se ejecuta automáticamente al cargar la página para rellenar el primer desplegable [cite: 15, 18]
    @PostConstruct
    public void init() {
        try {
            BLFacade facade = FacadeBean.getBusinessLogic();
            departCities = facade.getDepartCities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Inicializamos listas vacías para evitar NullPointer
        arrivalCities = new ArrayList<>();
        rides = new ArrayList<>();
        System.out.println("ikusi"+departCities);
    }

    // EVENTO 1: Cuando el usuario cambia la ciudad de origen
    public void onDepartCityChange() {
        try {
            BLFacade facade = FacadeBean.getBusinessLogic();
            // Pedimos a la lógica las ciudades destino posibles desde la ciudad elegida
            arrivalCities = facade.getDestinationCities(selectedDepartCity);
            System.out.println(arrivalCities);
            rides.clear(); // Limpiamos la tabla si cambiamos de ciudad
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // EVENTO 2: Cuando el usuario selecciona una fecha en el calendario
    public void onDateSelect(SelectEvent<Date> event) {
        this.date = event.getObject();
        try {
            BLFacade facade = FacadeBean.getBusinessLogic();
            // Buscamos los viajes en la BD
            rides = facade.getRides(selectedDepartCity, selectedArrivalCity, date);
            
            if (rides.isEmpty()) {
                 FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("No hay viajes para esta fecha."));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String close() {
    	return "Index";
    }

    // --- Getters y Setters ---
    
    public String getSelectedDepartCity() { return selectedDepartCity; }
    public void setSelectedDepartCity(String selectedDepartCity) { this.selectedDepartCity = selectedDepartCity; }

    public String getSelectedArrivalCity() { return selectedArrivalCity; }
    public void setSelectedArrivalCity(String selectedArrivalCity) { this.selectedArrivalCity = selectedArrivalCity; }

    public List<String> getDepartCities() { return departCities; }
    public void setDepartCities(List<String> departCities) { this.departCities = departCities; }

    public List<String> getArrivalCities() { return arrivalCities; }
    public void setArrivalCities(List<String> arrivalCities) { this.arrivalCities = arrivalCities; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public List<Ride> getRides() { return rides; }
    public void setRides(List<Ride> rides) { this.rides = rides; }
}