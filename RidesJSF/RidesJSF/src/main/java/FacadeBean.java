import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;
import dataAccess.DataAccess;

public class FacadeBean {
    // Singleton para mantener una única instancia de la lógica de negocio
    private static FacadeBean singleton = new FacadeBean();
    private static BLFacade facadeInterface;

    private FacadeBean() {
        try {
            // Aquí se conecta con rides.jar y ObjectDB
            facadeInterface = new BLFacadeImplementation(new DataAccess());
        } catch (Exception e) {
            System.out.println("FacadeBean: Fallo al crear lógica de negocio: " + e.getMessage());
        }
    }

    public static BLFacade getBusinessLogic() {
        return facadeInterface;
    }
}