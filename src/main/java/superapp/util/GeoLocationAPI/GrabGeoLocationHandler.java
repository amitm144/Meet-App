package superapp.util.GeoLocationAPI;

import org.springframework.web.client.RestTemplate;
import superapp.util.exceptions.ThridPartyAPIException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
public class GrabGeoLocationHandler extends MapBox{
    private String CuasieURL;
    private MapBoxConverter mapBoxConverter;
    public GrabGeoLocationHandler(MapBoxConverter mapBoxConverter) {
        super();
        this.CuasieURL = super.getBase_URL() + "geocoding/v5/mapbox.places/";
        this.mapBoxConverter = mapBoxConverter;
    }
    public ArrayList<Map<String, Object>> getResturantbyCuasie(GrabCuasine cuisine, int limit, int radius){
        RestTemplate restTemplate = new RestTemplate();
        String url = this.CuasieURL+EnumToStringCheck(cuisine)+".json?country=IL&proximity=" + super.getAfekaCollageCordiantes() +"&limit="+limit+"&radius="+radius+"&access_token="
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
    restaurants.forEach(restaurant -> rv.add(mapBoxConverter.mapToResturantDetails(restaurant)));
    return rv;
}

private String EnumToStringCheck(GrabCuasine cuisine){
        if (cuisine.toString().contains("_"))
            return cuisine.toString().replace("_", " ");
        return cuisine.toString();
}




}
