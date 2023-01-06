package superapp.util.GeoLocationAPI;

public abstract class MapBox {
    protected final String afekaCollageCordiantes = "34.818080852283444,32.114965594875784";
    protected final String API_KEY = "pk.eyJ1IjoieXV2YWx4bSIsImEiOiJjbGNqOWJjanYwaTIxM29wNnVpcHV1dThpIn0.fbPca2Qoijwl0J0X1h_S8A";
    protected final String Base_URL = "https://api.mapbox.com/";

    public String getAfekaCollageCordiantes() {
        return afekaCollageCordiantes;
    }

    public String getAPI_KEY() {
        return API_KEY;
    }

    public String getBase_URL() {
        return Base_URL;
    }
}
