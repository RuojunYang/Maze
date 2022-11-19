package ca.cmpt213.as4.model;

import ca.cmpt213.as4.model.map_element.MapElement;

/*
gamelogic create all function for UI that easily to understand
 */
public class GameLogic {
    private Maze maze;

    // get specific element in map
    public MapElement getElementInRowCol(int row, int col) throws CloneNotSupportedException {
        return this.maze.getMapElement(row, col);
    }

    public GameLogic(Maze maze) {
        this.maze = maze;
    }

    public int getMazeRowSize() {
        return maze.getRowSize();
    }

    public int getMazeColSize() {
        return maze.getColSize();
    }

    // return currentGoal
    public int getCurrentGoalMade() {
        return maze.getCurrentGoal();
    }


    // reutn finalGoal
    public int getGoal() {
        return maze.getFinalGoal();
    }

    // cheat or in the end of game, make all map visible
    public void showAllMap() {
        maze.showAllMaze();
    }

    // cheat code to set goal to 1
    public void setGoalToOne() {
        maze.setFinalGoal(1);
    }

    // player move into the choose direction
    public void playerMove(Direction direction) {
        maze.mouseMove(direction);
    }

    // all cats take their move
    public void catsMove() {
        maze.catMove();
    }

    // check is the direction for player is walkable
    public boolean playerMoveCheck(Direction direction) {
        return this.maze.playerMoveCheck(direction);
    }

    // check if game win
    public boolean gameWiningCheck() {
        return this.maze.winningCheck();
    }

    // check if game lose
    public boolean gameLoseCheck() {
        return this.maze.loseCheck();
    }

}
