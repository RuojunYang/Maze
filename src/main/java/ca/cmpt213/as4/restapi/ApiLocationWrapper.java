package ca.cmpt213.as4.restapi;


import ca.cmpt213.as4.model.Pair;

import java.util.ArrayList;
import java.util.List;

public class ApiLocationWrapper {
    public int x;
    public int y;

    // MAY NEED TO CHANGE PARAMETERS HERE TO SUITE YOUR PROJECT
    public static ApiLocationWrapper makeFromCellLocation(Pair<Integer, Integer> pair) {
        ApiLocationWrapper location = new ApiLocationWrapper();
        location.x = pair.getValue();
        location.y = pair.getKey();
        // Populate this object!

        return location;
    }
    // MAY NEED TO CHANGE PARAMETERS HERE TO SUITE YOUR PROJECT
    public static List<ApiLocationWrapper> makeFromCellLocations(List<Pair<Integer, Integer>> pairs) {
        List<ApiLocationWrapper> locations = new ArrayList<>();
        for(Pair<Integer, Integer> pair: pairs){
            locations.add(ApiLocationWrapper.makeFromCellLocation(pair));
        }
        // Populate this object!

        return locations;
    }
}
