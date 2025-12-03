package domain;

import java.io.Serializable;
import javax.persistence.*;

@Entity
public class Customer extends User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public Customer() {
		super();
	}
	
	public Customer(String email, String name, String password) {
		super(email, name, password, UserType.CUSTOMER);
	}
	
	@Override
	public String toString() {
		return "Customer [email=" + getEmail() + ", name=" + getName() + "]";
	}
}
