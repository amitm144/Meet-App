package demo;

// {"firstName"":"Jane", "lastName":"Smith"}
public class NameBoundary {
	private String firstName, lastName;

	public NameBoundary() {
	}

	public NameBoundary(String firstName, String lastName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return "NameBoundary [firstName=" + firstName + ", lastName=" + lastName + "]";
	}

}
