package ca.cmpt213.as4.restapi;

import ca.cmpt213.as4.model.GameLogic;
import ca.cmpt213.as4.model.Pair;
import ca.cmpt213.as4.model.map_element.MapElement;

import java.util.ArrayList;
import java.util.List;

public class ApiBoardWrapper {
    public int boardWidth;
    public int boardHeight;
    public ApiLocationWrapper mouseLocation;
    public ApiLocationWrapper cheeseLocation;
    public List<ApiLocationWrapper> catLocations;
    public boolean[][] hasWalls;
    public boolean[][] isVisible;

    // MAY NEED TO CHANGE PARAMETERS HERE TO SUITE YOUR PROJECT
    public static ApiBoardWrapper makeFromGame(GameLogic game) throws CloneNotSupportedException {
        ApiBoardWrapper wrapper = new ApiBoardWrapper();
        wrapper.boardHeight = 15;
        wrapper.boardWidth = 20;
        wrapper.hasWalls = new boolean[15][20];
        wrapper.isVisible = new boolean[15][20];

        // Populate this object!

        List<Pair<Integer, Integer>> list = new ArrayList<>();
        for (int i = 0; i < wrapper.boardHeight; i++) {
            for (int j = 0; j < wrapper.boardWidth; j++) {
                MapElement mapElement = game.getElementInRowCol(i, j);
                wrapper.hasWalls[i][j] = mapElement.isWall();
                wrapper.isVisible[i][j] = mapElement.isFound();
                if (mapElement.isContainMouse()) {
                    wrapper.mouseLocation = ApiLocationWrapper.makeFromCellLocation(new Pair<>(i, j));
                }
                if (mapElement.isContainCheese()) {
                    wrapper.cheeseLocation = ApiLocationWrapper.makeFromCellLocation(new Pair<>(i, j));
                }
                if (mapElement.isContainCat()) {
                    list.add(new Pair<>(i, j));
                }
            }
            wrapper.catLocations = ApiLocationWrapper.makeFromCellLocations(list);
        }
        return wrapper;
    }
}
