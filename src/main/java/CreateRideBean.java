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
@Named("createRide")
@ViewScoped
public class CreateRideBean implements Serializable{
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
public CreateRideBean(){
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
//public String egiaztatu() {
//if (izena.length()!=pasahitza.length()){
// FacesContext.getCurrentInstance().addMessage(null,
// new FacesMessage("Errorea: izenaren eta pasahitzaren luzera desberdinak dira."));
// return null;
//}
//if (izena.equals("pirata"))
//return "error";
//else
//return "ok";
//}
public void onDateSelect(SelectEvent event) {
FacesContext.getCurrentInstance().addMessage(null,
 new FacesMessage("Data aukeratua: "+event.getObject()));
}

public String rideCreate() {
    try {
        // 1. Obtener la lógica desde el FacadeBean [cite: 184]
        BLFacade facade = FacadeBean.getBusinessLogic();
        
        // 2. Llamar al método createRide (asumo que existe en tu jar antiguo)
        // Nota: Como no tenemos Login aún, pondremos un conductor "Test Driver" fijo.
        facade.createRide(depart, arrival, data, seats, cash, "driver1@gmail.com");
        
        // 3. Mensaje de éxito
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Ride created successfully"));
        
        return "ok"; // O null para quedarse en la página
        
    } catch (Exception e) {
        // Capturar errores (ej: fechas pasadas, errores de BD)
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + e.getMessage(), null));
        return null;
    }
}

}