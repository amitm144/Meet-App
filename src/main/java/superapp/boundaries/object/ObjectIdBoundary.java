package superapp.boundaries.object;


import org.springframework.beans.factory.annotation.Value;

public class ObjectIdBoundary {

    private String superapp;
    private String internalObjectId;

    public ObjectIdBoundary() {}

    public ObjectIdBoundary(@Value("${spring.application.name}") String superapp,
                            String internalObjectId) {
        this.superapp = superapp;
        this.internalObjectId = internalObjectId;
    }

    public String getSuperapp() {
        return superapp;
    }

    @Value("${spring.application.name}")
    public void setSuperapp(String superapp) {
        this.superapp = superapp;
    }

    public String getInternalObjectId() {
        return internalObjectId;
    }

    public void setInternalObjectId(String internalObjectId) {
        this.internalObjectId = internalObjectId;
    }

    @Override
    public String toString() {
        return "ObjectIdBoundary{" +
                "superapp='" + superapp + '\'' +
                ", internalObjectId='" + internalObjectId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass())
            return false;
        ObjectIdBoundary objId = (ObjectIdBoundary) obj;
        return this.superapp.equals(objId.superapp) && this.internalObjectId.equals(objId.internalObjectId);
    }

}
