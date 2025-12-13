package domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Entity
public class DriverUser extends User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	
	public DriverUser() {
		super();
	}
	
	public DriverUser(String email, String name, String password) {
		super(email, name, password, UserType.DRIVER);
	}
	
	@Override
	public String toString() {
		return "DriverUser [email=" + getEmail() + ", name=" + getName() + "]";
	}
}
