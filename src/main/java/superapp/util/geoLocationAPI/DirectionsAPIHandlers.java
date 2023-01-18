package superapp.util.geoLocationAPI;

import java.util.List;
import java.util.Map;

public interface DirectionsAPIHandlers {
    public Map<String,Object> getDirectionsByAddress(String language, List<String> addresses);
}
