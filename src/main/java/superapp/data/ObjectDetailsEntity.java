package superapp.data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ObjectDetails")
public class ObjectDetailsEntity {

    @Id
    private String objectDetailsId;
    private String property;
    private String ObjectDetailsValue;
    private String type;

    public ObjectDetailsEntity() {
    }

    public ObjectDetailsEntity(String objectDetailsId, String property, String ObjectDetailsValue, String type) {
        this.objectDetailsId = objectDetailsId;
        this.property = property;
        this.ObjectDetailsValue = ObjectDetailsValue;
        this.type = type;
    }

    public String getObjectDetailsId() {
        return objectDetailsId;
    }

    public void setObjectDetailsId(String objectDetailsId) {
        this.objectDetailsId = objectDetailsId;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getObjectDetailsValue() {
        return ObjectDetailsValue;
    }

    public void setObjectDetailsValue(String objectDetailsValue) {
        this.ObjectDetailsValue = objectDetailsValue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ObjectDetailsEntity{" +
                "id='" + objectDetailsId + '\'' +
                ", property='" + property + '\'' +
                ", value='" + ObjectDetailsValue + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
