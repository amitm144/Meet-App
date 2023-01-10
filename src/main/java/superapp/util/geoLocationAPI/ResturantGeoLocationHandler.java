package superapp.util.geoLocationAPI;

import org.springframework.web.client.RestTemplate;
import superapp.util.exceptions.ThridPartyAPIException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
public class ResturantGeoLocationHandler extends MapBox implements ResturantGeoLocaionHandlers {
    private String CuisineURL;
    private MapBoxConverter mapBoxConverter;
    public ResturantGeoLocationHandler(MapBoxConverter mapBoxConverter) {
        super();
        this.CuisineURL = super.getBase_URL() + "geocoding/v5/mapbox.places/";
        this.mapBoxConverter = mapBoxConverter;
    }
    @Override
    public ArrayList<Map<String, Object>> getResturantbyCuasie(String cuisine, int limit, int radius){
        RestTemplate restTemplate = new RestTemplate();
        String url = this.CuisineURL +cuisine+".json?country=IL&proximity=" + super.getAFEKA_COLLEGE_LOCATION() +"&limit="+limit+"&radius="+radius+"&access_token="
                +super.getAPI_KEY();

        String response = restTemplate.getForObject(url, String.class);
        Map<String, Object> responseToMap =mapBoxConverter.detailsToMap(response);
        ArrayList<LinkedHashMap> restaurants = (ArrayList<LinkedHashMap>) responseToMap.get("features");

        if(restaurants.size() ==0)
            throw new ThridPartyAPIException("cuisine Not Found");

        return extractResturantFromMap(restaurants);

    }
    private ArrayList<Map<String, Object>> extractResturantFromMap(ArrayList<LinkedHashMap> restaurants){
        ArrayList<Map<String, Object>> rv = new ArrayList<Map<String, Object>>();
        restaurants.forEach(restaurant -> rv.add(mapBoxConverter.mapToRestaurantDetails(restaurant)));
        return rv;
    }
}
