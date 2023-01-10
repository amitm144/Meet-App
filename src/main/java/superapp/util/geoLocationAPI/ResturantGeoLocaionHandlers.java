package superapp.util.geoLocationAPI;

import java.util.ArrayList;
import java.util.Map;

public interface ResturantGeoLocaionHandlers {
    public ArrayList<Map<String, Object>>  getResturantbyCuasie(String cuisine, int limit, int radius);
}
