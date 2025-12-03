package domain;

import java.io.Serializable;
import javax.persistence.*;

@Entity
public class Admin extends User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public Admin() {
		super();
	}
	
	public Admin(String email, String name, String password) {
		super(email, name, password, UserType.ADMIN);
	}
	
	@Override
	public String toString() {
		return "Admin [email=" + getEmail() + ", name=" + getName() + "]";
	}
}
