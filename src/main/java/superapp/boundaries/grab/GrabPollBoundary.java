package superapp.boundaries.grab;

import superapp.data.GrabCuisines;

import java.util.ArrayList;
import java.util.Map;

public class GrabPollBoundary {
    private GrabCuisines selectedCuisine;

    private Map<GrabCuisines,Integer> cuisinesVotes;
    private ArrayList<Map<String, Object>> suggestedRestaurants;

    public GrabPollBoundary() {}

    public GrabPollBoundary(GrabCuisines selectedCuisine, ArrayList<Map<String, Object>> suggestedRestaurants) {
        this.selectedCuisine = selectedCuisine;
        this.suggestedRestaurants = suggestedRestaurants;
    }
//
//    public GrabPollBoundary(GrabCuisines selectedCuisine, String url , ArrayList<Map<String, Object>> suggestedRestaurants) {
//        this.selectedCuisine = selectedCuisine;
//        this.suggestedRestaurants = suggestedRestaurants;
//        this.cuisinesVotes = cuisinesVotes;
//    }

    public GrabCuisines getSelectedCuisine() {
        return selectedCuisine;
    }

    public void setSelectedCuisine(GrabCuisines selectedCuisine) {
        this.selectedCuisine = selectedCuisine;
    }

    public ArrayList<Map<String, Object>> getSuggestedRestaurants() {
        return suggestedRestaurants;
    }

    public void setSuggestedRestaurants(ArrayList<Map<String, Object>> suggestedRestaurants) {
        this.suggestedRestaurants = suggestedRestaurants;
    }

    @Override
    public String toString() {
        return "GrabPollBoundary{" +
                "selectedCuisine=" + selectedCuisine +
                ", Suggested Restaurants='" + suggestedRestaurants + '\'' +
                (cuisinesVotes!=null ? "votes: " + cuisinesVotes :"")+
        '}';
    }
}
