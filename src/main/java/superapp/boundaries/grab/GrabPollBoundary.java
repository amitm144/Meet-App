package superapp.boundaries.grab;

import superapp.data.GrabCuisines;

import java.util.List;
import java.util.Map;

public class GrabPollBoundary {
    private GrabCuisines selectedCuisine;
    private List<Map<String, Object>> suggestedRestaurants;

    public GrabPollBoundary() {}

    public GrabPollBoundary(GrabCuisines selectedCuisine, List<Map<String, Object>> suggestedRestaurants) {
        this.selectedCuisine = selectedCuisine;
        this.suggestedRestaurants = suggestedRestaurants;
    }

    public GrabCuisines getSelectedCuisine() {
        return selectedCuisine;
    }

    public void setSelectedCuisine(GrabCuisines selectedCuisine) {
        this.selectedCuisine = selectedCuisine;
    }

    public List<Map<String, Object>> getSuggestedRestaurants() {
        return suggestedRestaurants;
    }

    public void setSuggestedRestaurants(List<Map<String, Object>> suggestedRestaurants) {
        this.suggestedRestaurants = suggestedRestaurants;
    }

    @Override
    public String toString() {
        return "GrabPollBoundary{" +
                "selectedCuisine=" + selectedCuisine +
                ", SuggestedRestaurants='" + suggestedRestaurants + '\'' +

        '}';
    }
}
