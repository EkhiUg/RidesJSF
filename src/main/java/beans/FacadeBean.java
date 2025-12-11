package beans;
import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;
import dataAccess.HibernateDataAccess;

public class FacadeBean {
    // Singleton para mantener una única instancia de la lógica de negocio
    private static FacadeBean singleton = new FacadeBean();
    private static BLFacade facadeInterface;

    private FacadeBean() {
        try {
            // Aquí se conecta con rides.jar usando Hibernate/MariaDB
            facadeInterface = new BLFacadeImplementation(new HibernateDataAccess());
        } catch (Exception e) {
            System.out.println("FacadeBean: Fallo al crear lógica de negocio: " + e.getMessage());
        }
    }

    public static BLFacade getBusinessLogic() {
        return facadeInterface;
    }
}