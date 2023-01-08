package superapp.util.GeoLocationAPI;

import java.util.ArrayList;
import java.util.Map;

public interface GrabGeoLocaionHandlers {
    public ArrayList<Map<String, Object>>  getResturantbyCuasie(GrabCuasine cuisine, int limit, int radius);
}
