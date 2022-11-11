package demo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/*
 {"message":"hello", "id":123, "important":true, "currentTimestamp":"2022-11-10T20:31:12.353+0200",
  "version":1.1, "status":"OK", "name":{"firstName":"Jane", "lastName":"Smith"}, "details":{"x1":1, "y":"string", "z":{}}
 }

 */
public class DemoBoundary {
	private long id;
	private String message;
	private Boolean important;
	private Date currentTimestamp;
	private float version;
	private StatusEnum status; // OK ERROR UNDEFINED
	private NameBoundary name;
	private Map<String, Object> details;

	public DemoBoundary() {
		// NOTE: in general, the business logic should initialize these values
		// TODO have the business logic initialize the values by default
		this.id = new Random().nextLong();
		this.important = false;
		this.currentTimestamp = new Date();
		this.version = 1.0f;
		this.status = StatusEnum.UNDEFINED;
		this.name = new NameBoundary("Jane", "Smith");
		this.details = new HashMap<>();
		this.details.put("x1", 42);
		this.details.put("y", "string");
		this.details.put("z", new NameBoundary("some", "object"));
	}

	public DemoBoundary(String message) {
		this();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Boolean getImportant() {
		return important;
	}

	public void setImportant(Boolean important) {
		this.important = important;
	}

	public Date getCurrentTimestamp() {
		return currentTimestamp;
	}

	public void setCurrentTimestamp(Date currentTimestamp) {
		this.currentTimestamp = currentTimestamp;
	}

	public float getVersion() {
		return version;
	}

	public void setVersion(float version) {
		this.version = version;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public NameBoundary getName() {
		return name;
	}

	public void setName(NameBoundary name) {
		this.name = name;
	}

	public Map<String, Object> getDetails() {
		return details;
	}

	public void setDetails(Map<String, Object> details) {
		this.details = details;
	}

	@Override
	public String toString() {
		return "DemoBoundary [id=" + id + ", message=" + message + ", important=" + important + ", currentTimestamp="
				+ currentTimestamp + ", version=" + version + ", status=" + status + ", name=" + name + ", details="
				+ details + "]";
	}

}
