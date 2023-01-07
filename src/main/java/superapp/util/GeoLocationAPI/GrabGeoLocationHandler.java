package superapp.util.GeoLocationAPI;

import org.springframework.web.client.RestTemplate;
import superapp.util.exceptions.NotFoundException;

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
    public ArrayList<Map<String, Object>> getResturantbyCuasie(GrabCuasine cuasie, int limit, int radius){
        RestTemplate restTemplate = new RestTemplate();
        String url = this.CuasieURL+cuasie+".json?proximity=" + super.getAfekaCollageCordiantes() +"&limit="+limit+"&radius="+radius+"&access_token="
                +super.getAPI_KEY();

        String response = restTemplate.getForObject(url, String.class);
        Map<String, Object> responseToMap =mapBoxConverter.detailsToMap(response);
        return extractResturantFromMap(responseToMap);

    }


private ArrayList<Map<String, Object>> extractResturantFromMap(Map<String, Object> response){
    ArrayList<LinkedHashMap> resturants = (ArrayList<LinkedHashMap>) response.get("features");
    if(resturants.size() ==0)
        throw new NotFoundException("Cuasie Not Found");
    ArrayList<Map<String, Object>> rv = new ArrayList<Map<String, Object>>();
    resturants.forEach(restaurant -> rv.add(mapBoxConverter.mapToResturantDetails(restaurant)));
    return rv;
}




}
