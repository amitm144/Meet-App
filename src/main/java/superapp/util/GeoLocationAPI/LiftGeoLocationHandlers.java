package superapp.util.GeoLocationAPI;

import java.util.ArrayList;
import java.util.Map;

public interface LiftGeoLocationHandlers {
    public Map<String,Object> getDirectionsByAddress(LiftLanguage language, ArrayList<String> addresses);
}
