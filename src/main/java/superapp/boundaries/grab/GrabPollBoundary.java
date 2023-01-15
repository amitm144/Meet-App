package superapp.boundaries.grab;

import superapp.data.GrabCuisines;

import java.util.Map;

public class GrabPollBoundary {
    private GrabCuisines selectedCuisine;

    private Map<GrabCuisines,Integer> cuisinesVotes;
    private String url;

    public GrabPollBoundary() {}

    public GrabPollBoundary(GrabCuisines selectedCuisine, String url) {
        this.selectedCuisine = selectedCuisine;
        this.url = url;
    }

    public GrabPollBoundary(GrabCuisines selectedCuisine, String url , Map<GrabCuisines,Integer> cuisinesVotes) {
        this.selectedCuisine = selectedCuisine;
        this.url = url;
        this.cuisinesVotes = cuisinesVotes;
    }

    public GrabCuisines getSelectedCuisine() {
        return selectedCuisine;
    }

    public void setSelectedCuisine(GrabCuisines selectedCuisine) {
        this.selectedCuisine = selectedCuisine;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "GrabPollBoundary{" +
                "selectedCuisine=" + selectedCuisine +
                ", url='" + url + '\'' +
                (cuisinesVotes!=null ? "votes: " + cuisinesVotes :"")+
        '}';
    }
}
