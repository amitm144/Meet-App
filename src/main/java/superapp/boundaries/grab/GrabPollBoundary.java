package superapp.boundaries.grab;

import superapp.boundaries.object.SuperAppObjectIdBoundary;
import superapp.data.GrabCuisines;

public class GrabPollBoundary {
    private SuperAppObjectIdBoundary groupId;
    private GrabCuisines selectedCuisine;

    public GrabPollBoundary() {}

    public GrabPollBoundary(SuperAppObjectIdBoundary groupId, GrabCuisines selectedCuisine) {
        this.groupId = groupId;
        this.selectedCuisine = selectedCuisine;
    }

    public SuperAppObjectIdBoundary getGroupId() { return groupId; }

    public void setGroupId(SuperAppObjectIdBoundary groupId) { this.groupId = groupId; }

    public GrabCuisines getSelectedCuisine() {
        return selectedCuisine;
    }

    public void setSelectedCuisine(GrabCuisines selectedCuisine) {
        this.selectedCuisine = selectedCuisine;
    }

    @Override
    public String toString() {
        return "SelectedCuisineBoundary{" +
                "groupId=" + groupId +
                ", selectedCuisine=" + selectedCuisine +
                '}';
    }
}
