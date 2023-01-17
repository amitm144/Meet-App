package superapp.util.geoLocationAPI;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import superapp.util.exceptions.InvalidInputException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

public class DirectionsAPIHandler extends MapBox implements DirectionsAPIHandlers {
    private String directionURL;
    private String searchPlacesURL;
    private MapBoxConverter mapBoxConverter;

    public DirectionsAPIHandler(MapBoxConverter mapBoxConverter) {
        super();
        this.directionURL = super.getBaseUrl() + "directions/v5/mapbox/driving/";
        this.searchPlacesURL = super.getBaseUrl() +"geocoding/v5/mapbox.places/";
        this.mapBoxConverter = mapBoxConverter;
    }

    @Override
    public Map<String,Object> getDirectionsByAddress(String language, List<String> addresses) {
        ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();
        addresses.forEach(address -> coordinates.add(addressToCoordinates(address)));
        return getDirectionByCoordinates(language,coordinates);
    }

    private Map<String,Object> getDirectionByCoordinates(String language, List<Coordinate> coordinates){
        if(coordinates.size() < 2 || coordinates.size() > 25)
            throw new InvalidInputException("Please Provide 2 to 25 coordinates ");

        RestTemplate restTemplate = new RestTemplate();
        String coordinatesConverted = mapBoxConverter.coordinatesToString(coordinates);
        String url = this.directionURL +coordinatesConverted+"?language="+language+"&steps=true&access_token="
                +super.getKey();
        String response;
        try {
            response = restTemplate.getForObject(url, String.class);
        }
        catch (HttpClientErrorException e){
            if(e.getStatusCode().equals(HttpStatus.valueOf(422))){
                throw new InvalidInputException("Route exceeds maximum distance limitation");
            }
            else if( e.getStatusCode().equals(HttpStatus.FORBIDDEN)){
                throw new InvalidInputException("API key exception");
            }
            else
                throw new RuntimeException("MAPBOX api failed to provide directions");
        }
        Map<String, Object> responseToMap = mapBoxConverter.detailsToMap(response);
        if(!responseToMap.get("code").equals("Ok"))
        throw new InvalidInputException("MapBoxAPI failed to provide directions!");
       return mapBoxConverter.filterMapBoxRequestToDirections(responseToMap);
    }

    private Coordinate addressToCoordinates(String address) {
        RestTemplate restTemplate = new RestTemplate();
        String url = this.searchPlacesURL+address+".json?&limit=1&access_token="+super.getKey();
        String response = restTemplate.getForObject(url, String.class);
        Map<String, Object> responseToMap =mapBoxConverter.detailsToMap(response);
        List< LinkedHashMap> features = (ArrayList< LinkedHashMap>)responseToMap.get("features");
        if(features.size() ==0)
            throw new InvalidInputException("MapBoxAPI failed to provide directions please check again your address ");
        List<Double> center = (ArrayList<Double>) features.get(0).get("center");
        return new Coordinate(center.get(0), center.get(1));
    }

}
