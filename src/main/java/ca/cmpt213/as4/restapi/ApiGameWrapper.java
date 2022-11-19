package ca.cmpt213.as4.restapi;

import ca.cmpt213.as4.model.GameLogic;

public class ApiGameWrapper {
    public int gameNumber;
    public boolean isGameWon;
    public boolean isGameLost;
    public int numCheeseFound;
    public int numCheeseGoal;

    // MAY NEED TO CHANGE PARAMETERS HERE TO SUITE YOUR PROJECT
    public static ApiGameWrapper makeFromGame(GameLogic game, int id) {
        ApiGameWrapper wrapper = new ApiGameWrapper();
        wrapper.gameNumber = id;
        wrapper.isGameLost = game.gameLoseCheck();
        wrapper.isGameWon = game.gameWiningCheck();
        wrapper.numCheeseFound = game.getCurrentGoalMade();
        wrapper.numCheeseGoal = game.getGoal();
        // Populate this object!

        return wrapper;
    }
}
