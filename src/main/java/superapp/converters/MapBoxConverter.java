package superapp.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import superapp.util.GeoLocationAPI.ResturantDetails;
import superapp.util.exceptions.InvalidInputException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class MapBoxConverter {
    private ObjectMapper jackson;

    public MapBoxConverter() {
        this.jackson = new ObjectMapper();
    }
    public Map<String, Object> detailsToMap(String details) {
        try {
            return (Map<String, Object>)this.jackson.readValue(details, Map.class);
        } catch (Exception e) { throw new InvalidInputException(e.getMessage()); }
    }
    public ResturantDetails mapToResturantDetails(LinkedHashMap resturant) {
        String name = (String) resturant.get("text");

        LinkedHashMap properties = (LinkedHashMap)resturant.get("properties");
        String address = (String) ((LinkedHashMap)properties).get("address");
        String category = (String) ((LinkedHashMap)properties).get("category");

        LinkedHashMap geo = (LinkedHashMap)resturant.get("geometry");
        List<String> coordinates = (List<String>) geo.get("coordinates");

        ArrayList<LinkedHashMap> context = (ArrayList<LinkedHashMap>)resturant.get("context");
        String district= (String) context.get(1).get("text");
        return new ResturantDetails(address,category,coordinates,name,district);
    }
}
