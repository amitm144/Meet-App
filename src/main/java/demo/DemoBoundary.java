package demo;

public class DemoBoundary {
	private String message;

	public DemoBoundary() {
	}

	public DemoBoundary(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Demo [message=" + message + "]";
	}
}
