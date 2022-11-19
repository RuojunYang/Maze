package ca.cmpt213.as4.model;


import ca.cmpt213.as4.model.game_element.Cat;
import ca.cmpt213.as4.model.game_element.Cheese;
import ca.cmpt213.as4.model.game_element.Mouse;
import ca.cmpt213.as4.model.map_element.MapElement;
import ca.cmpt213.as4.model.map_element.Path;
import ca.cmpt213.as4.model.map_element.Wall;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static ca.cmpt213.as4.model.Direction.*;

/*
Maze class, can generator maze, and put game element on it.
 */
public class Maze {
    private int rowSize;
    private int colSize;
    private int finalGoal;
    private List<Cat> Cats = new ArrayList<>();
    private Mouse mouse;
    private Cheese cheese;
    public MapElement[][] map = null;
    private int currentGoal = 0;

    public int getRowSize() {
        return rowSize;
    }

    public int getColSize() {
        return colSize;
    }

    public MapElement getMapElement(int row, int col) throws CloneNotSupportedException {
        return (MapElement) map[row][col].clone();
    }

    // to check the direction is walkable
    // true is walkable
    // false is not walkable
    public boolean playerMoveCheck(Direction direction) {
        if (direction == left) {
            if (this.map[mouse.getRow()][mouse.getCol() - 1].isWall() == true) {
                return false;
            } else {
                return true;
            }
        } else if (direction == up) {
            if (this.map[mouse.getRow() - 1][mouse.getCol()].isWall() == true) {
                return false;
            } else {
                return true;
            }
        } else if (direction == right) {
            if (this.map[mouse.getRow()][mouse.getCol() + 1].isWall() == true) {
                return false;
            } else {
                return true;
            }
        } else if (direction == down) {
            if (this.map[mouse.getRow() + 1][mouse.getCol()].isWall() == true) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    // current goal made getter
    public int getCurrentGoal() {
        return currentGoal;
    }

    // constructor
    public Maze(int row, int col, int finalGoal) {
        this.rowSize = row;
        this.colSize = col;
        this.finalGoal = finalGoal;
        createMaze();
    }

    // add a cat in [row][col] location
    public void addCat(int row, int col) {
        this.Cats.add(new Cat(row, col));
    }

    // add a cheese in [row][col] location
    public void addCheese(int row, int col) {
        cheese = new Cheese(row, col);
    }


    // goal getter
    public int getFinalGoal() {
        return this.finalGoal;
    }

    // goal setter for cheat code
    public void setFinalGoal(int finalGoal) {
        this.finalGoal = finalGoal;
    }

    // check if player eat enough cheese
    // return true if win
    // return false if not win
    public boolean winningCheck() {
        if (currentGoal >= this.finalGoal) {
            return true;
        } else {
            return false;
        }
    }

    // check if player is eaten by cat
    // return true if lose
    // return false if not lose
    public boolean loseCheck() {
        for (Cat cat : this.Cats) {
            if ((cat.getRow() == this.mouse.getRow()) && (cat.getCol() == this.mouse.getCol())) {
                return true;
            }
        }
        return false;
    }

    // make all maze visible
    // for cheat code and the end of game
    public void showAllMaze() {
        for (int i = 0; i < this.rowSize; i++) {
            for (int j = 0; j < this.colSize; j++) {
                this.map[i][j].setFound(true);
            }
        }
    }

    // creatMaze
    private void createMaze() {
        // put wall, and make wall around it
        this.map = new MapElement[this.rowSize][this.colSize];
        for (int i = 0; i < this.rowSize; i++) {
            for (int j = 0; j < this.colSize; j++) {
                if (i == 0 || i == this.rowSize - 1 || j == 0 || j == this.colSize - 1) {
                    this.map[i][j] = new Wall();
                    this.map[i][j].setFound(true);
                } else {
                    this.map[i][j] = new Path();
                }
            }
        }

        // initial the Maze, build wall inside
        // recursion
        initialMaze(1, this.rowSize - 2, 1, this.colSize - 2);
        // check if there is 2*2 empty or 2*2 wall
        // if yes, regenerate maze again
        while (IsTwoByTow() == true) {
            for (int i = 0; i < this.rowSize; i++) {
                for (int j = 0; j < this.colSize; j++) {
                    if (i == 0 || i == this.rowSize - 1 || j == 0 || j == this.colSize - 1) {
                        this.map[i][j] = new Wall();
                        this.map[i][j].setFound(true);
                    } else {
                        this.map[i][j] = new Path();
                    }
                }
            }
            initialMaze(1, this.rowSize - 2, 1, this.colSize - 2);
        }

        // creat some inner loop
        for (int i = 0; i < 13; i++) {
            createLoop();
        }
        // make all direction around mouse visible
        this.map[1][1].setFound(true);
        this.map[1][2].setFound(true);
        this.map[2][2].setFound(true);
        this.map[2][1].setFound(true);
        // place mouse in map
        placeMouseAndCat();
        // place cat in map
        placeCheese();
        // make map contain is true
        this.map[this.mouse.getRow()][this.mouse.getCol()].setContainMouse(true);
        for (Cat cat : this.Cats) {
            this.map[cat.getRow()][cat.getCol()].setContainCat(true);
        }
        this.map[this.cheese.getRow()][this.cheese.getCol()].setContainCheese(true);
    }

    private void createLoop() {
        List<Integer> row = new ArrayList<>();
        List<Integer> col = new ArrayList<>();
        // visit all points
        for (int i = 1; i < this.rowSize - 1; i++) {
            for (int j = 1; j < this.colSize - 1; j++) {
                // itself must be wall
                if (this.map[i][j].isWall() == true) {
                    // must not generate a 2*2 empty
                    if (!(this.map[i - 1][j - 1].isWall() == false && this.map[i - 1][j].isWall() == false && this.map[i][j - 1].isWall() == false)
                            && !(this.map[i - 1][j].isWall() == false && this.map[i - 1][j + 1].isWall() == false && this.map[i][j + 1].isWall() == false)
                            && !(this.map[i + 1][j].isWall() == false && this.map[i + 1][j - 1].isWall() == false && this.map[i][j - 1].isWall() == false)
                            && !(this.map[i][j + 1].isWall() == false && this.map[i + 1][j + 1].isWall() == false && this.map[i + 1][j].isWall() == false)) {
                        // must not generate a empty around by 4 walls
                        if (this.map[i - 1][j].isWall() == false || this.map[i][j - 1].isWall() == false || this.map[i][j + 1].isWall() == false || this.map[i + 1][j].isWall() == false) {
                            row.add(i);
                            col.add(j);
                        }
                    }
                }
            }
        }
        // random set one to be empty
        if (row.size() > 0) {
            int random = ThreadLocalRandom.current().nextInt(0, row.size());
            this.map[row.get(random)][col.get(random)] = new Path();
        }
    }

    // placeCheese in map
    private void placeCheese() {
        List<Integer> row = new ArrayList<>();
        List<Integer> col = new ArrayList<>();
        for (int i = 0; i < this.rowSize; i++) {
            for (int j = 0; j < this.colSize; j++) {
                // not place cheese on wall and mouse
                if (this.map[i][j].isWall() == false && this.map[i][j].isContainMouse() == false) {
                    row.add(i);
                    col.add(j);
                }
            }
        }

        // make a random choice
        int random = ThreadLocalRandom.current().nextInt(0, row.size());

        // set these are cheese
        addCheese(row.get(random), col.get(random));
    }

    // check if there are any 2*2 empty or wall
    // return true if there is
    // return false if there is not
    private boolean IsTwoByTow() {
        // visit all points
        for (int i = 1; i < this.rowSize - 1; i++) {
            for (int j = 1; j < this.colSize - 1; j++) {
                if (this.map[i][j].isWall() == false && this.map[i + 1][j].isWall() == false && this.map[i][j + 1].isWall() == false && this.map[i + 1][j + 1].isWall() == false) {
                    return true;
                }
                if (this.map[i][j].isWall() == true && this.map[i + 1][j].isWall() == true && this.map[i][j + 1].isWall() == true && this.map[i + 1][j + 1].isWall() == true) {
                    return true;
                }
            }
        }
        return false;
    }

    // use recursion to build a maze
    private void initialMaze(int start_row, int end_row, int start_col, int end_col) {
        // there atlest to be a 2*3 empty or 3*2 empty to do this operation
        if ((end_row - start_row >= 2 && end_col - start_col >= 1) || (end_row - start_row >= 1 && end_col - start_col >= 2)) {
            // build horizontal wall
            if (end_row - start_row >= end_col - start_col) {
                // random pick one point between start + 1 and end - 1
                int randomWall = ThreadLocalRandom.current().nextInt(start_row + 1, end_row);
                // make sure both sides of the wall is not the path
                while (this.map[randomWall][start_col - 1].isWall() == false && this.map[randomWall][end_col + 1].isWall() == false) {
                    // no solution, we left it and going to regenerate maze again
                    if (end_row - start_row == 2) {
                        return;
                    }
                    // random pick again
                    randomWall = ThreadLocalRandom.current().nextInt(start_row + 1, end_row);
                }

                // build a horizontal wall based on that point
                for (int i = start_col; i <= end_col; i++) {
                    this.map[randomWall][i] = new Wall();
                }

                // open a door on that wall
                int randomDoor;
                // special case if the end of the wall is the path
                // we have to choose the square near the path to be the door to make it maze walkable
                if (this.map[randomWall][start_col - 1].isWall() == false) {
                    randomDoor = start_col;
                } else if (this.map[randomWall][end_col + 1].isWall() == false) {
                    randomDoor = end_col;
                }
                // normal case
                else {
                    randomDoor = ThreadLocalRandom.current().nextInt(start_col, end_col + 1);
                }
                this.map[randomWall][randomDoor] = new Path();
                // recursion to divide this maze
                initialMaze(start_row, randomWall - 1, start_col, end_col);
                initialMaze(randomWall + 1, end_row, start_col, end_col);
            }
            // build vertical wall
            else {
                // random pick one point between start + 1 and end - 1
                int randomWall = ThreadLocalRandom.current().nextInt(start_col + 1, end_col);
                // make sure both sides of the wall is not the path
                while (this.map[start_row - 1][randomWall].isWall() == false && this.map[end_row + 1][randomWall].isWall() == false) {
                    // no solution, we left it and going to regenerate maze again
                    if (end_col - start_col == 2) {
                        return;
                    }
                    // random pick again
                    randomWall = ThreadLocalRandom.current().nextInt(start_col + 1, end_col);
                }
                // build a vertical wall based on that point
                for (int i = start_row; i <= end_row; i++) {
                    this.map[i][randomWall] = new Wall();
                }
                // open a door on that wall
                int randomDoor;
                // special case if the end of the wall is the path
                // we have to choose the square near the path to be the door to make it maze walkable
                if (this.map[start_row - 1][randomWall].isWall() == false) {
                    randomDoor = start_row;
                } else if (this.map[end_row + 1][randomWall].isWall() == false) {
                    randomDoor = end_row;
                }
                // normal case
                else {
                    randomDoor = ThreadLocalRandom.current().nextInt(start_row, end_row + 1);
                }
                this.map[randomDoor][randomWall] = new Path();
                // recursion to divide this maze
                initialMaze(start_row, end_row, start_col, randomWall - 1);
                initialMaze(start_row, end_row, randomWall + 1, end_col);
            }
        }
    }

    // place mouse in left up
    // place 3 cats in other conner
    private void placeMouseAndCat() {
        this.mouse = new Mouse(1, 1);
        this.map[1][1].setContainMouse(true);
        addCat(this.rowSize - 2, 1);
        this.map[this.rowSize - 2][1].setContainCat(true);
        addCat(this.rowSize - 2, this.colSize - 2);
        this.map[this.rowSize - 2][this.colSize - 2].setContainCat(true);
        addCat(1, this.colSize - 2);
        this.map[1][colSize - 2].setContainCat(true);
    }

    // cat move
    public void catMove() {
        for (Cat cat : Cats) {
            this.map[cat.getRow()][cat.getCol()].setContainCat(false);
            List<Integer> row = new ArrayList<>();
            List<Integer> col = new ArrayList<>();
            List<Direction> direction = new ArrayList<>();
            Direction tryNotMove = cat.getLastMove();
            // up
            if (this.map[cat.getRow() - 1][cat.getCol()].isWall() == false && tryNotMove != Direction.down) {
                row.add(cat.getRow() - 1);
                col.add(cat.getCol());
                direction.add(Direction.up);
            }
            // left
            if (this.map[cat.getRow()][cat.getCol() - 1].isWall() == false && tryNotMove != Direction.right) {
                row.add(cat.getRow());
                col.add(cat.getCol() - 1);
                direction.add(Direction.left);
            }
            // right
            if (this.map[cat.getRow()][cat.getCol() + 1].isWall() == false && tryNotMove != Direction.left) {
                row.add(cat.getRow());
                col.add(cat.getCol() + 1);
                direction.add(Direction.right);
            }
            // down
            if (this.map[cat.getRow() + 1][cat.getCol()].isWall() == false && tryNotMove != Direction.up) {
                row.add(cat.getRow() + 1);
                col.add(cat.getCol());
                direction.add(Direction.down);
            }
            // try not move back
            if (row.size() > 0) {
                int random = ThreadLocalRandom.current().nextInt(0, row.size());
                this.map[row.get(random)][col.get(random)].setContainCat(true);
                cat.setLastMove(direction.get(random));
                cat.move(direction.get(random));
            }
            // there is a dead end
            else {
                Direction lastMove = cat.getLastMove();
                if (cat.getLastMove() == Direction.up) {
                    lastMove = Direction.down;
                    this.map[cat.getRow() + 1][cat.getCol()].setContainCat(true);
                } else if (cat.getLastMove() == Direction.left) {
                    lastMove = Direction.right;
                    this.map[cat.getRow()][cat.getCol() + 1].setContainCat(true);
                } else if (cat.getLastMove() == Direction.right) {
                    lastMove = Direction.left;
                    this.map[cat.getRow()][cat.getCol() - 1].setContainCat(true);
                } else {
                    lastMove = Direction.up;
                    this.map[cat.getRow() - 1][cat.getCol()].setContainCat(true);
                }
                cat.setLastMove(lastMove);
                cat.move(lastMove);
            }
        }
    }

    // mouse move
    public void mouseMove(Direction direction) {
        this.map[mouse.getRow()][mouse.getCol()].setContainMouse(false);
        // set around visible
        // actually only at most three new square will found
        if (direction == left) {
            this.map[mouse.getRow()][mouse.getCol() - 1].setContainMouse(true);
            this.map[mouse.getRow() - 1][mouse.getCol() - 2].setFound(true);
            this.map[mouse.getRow()][mouse.getCol() - 2].setFound(true);
            this.map[mouse.getRow() + 1][mouse.getCol() - 2].setFound(true);
            mouse.move(left);
        } else if (direction == up) {
            this.map[mouse.getRow() - 1][mouse.getCol()].setContainMouse(true);
            this.map[mouse.getRow() - 2][mouse.getCol() - 1].setFound(true);
            this.map[mouse.getRow() - 2][mouse.getCol()].setFound(true);
            this.map[mouse.getRow() - 2][mouse.getCol() + 1].setFound(true);
            mouse.move(up);
        } else if (direction == right) {
            this.map[mouse.getRow()][mouse.getCol() + 1].setContainMouse(true);
            this.map[mouse.getRow() - 1][mouse.getCol() + 2].setFound(true);
            this.map[mouse.getRow()][mouse.getCol() + 2].setFound(true);
            this.map[mouse.getRow() + 1][mouse.getCol() + 2].setFound(true);
            mouse.move(right);
        } else {
            this.map[mouse.getRow() + 1][mouse.getCol()].setContainMouse(true);
            this.map[mouse.getRow() + 2][mouse.getCol() - 1].setFound(true);
            this.map[mouse.getRow() + 2][mouse.getCol()].setFound(true);
            this.map[mouse.getRow() + 2][mouse.getCol() + 1].setFound(true);
            mouse.move(down);
        }
        // check if eat cheese
        // if eat, it will make it cheese disappear and goal made increment
        if (mouse.getRow() == cheese.getRow() && mouse.getCol() == cheese.getCol()) {
            currentGoal++;
            this.map[mouse.getRow()][mouse.getCol()].setContainCheese(false);
            placeCheese();
            this.map[cheese.getRow()][cheese.getCol()].setContainCheese(true);
        }
    }
}
