package superapp.util.GeoLocationAPI;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ResturantDetails {
    private String address;
    private String category;
    private List<String> coordinates;
    private String name;
    private String district;

    public ResturantDetails(String address, String category, List<String> coordinates, String name, String district) {
        this.address = address;
        this.category = category;
        this.coordinates = coordinates;
        this.name = name;
        this.district = district;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<String> coordinates) {
        this.coordinates = coordinates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
