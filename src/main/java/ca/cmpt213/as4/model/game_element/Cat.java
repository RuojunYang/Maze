package ca.cmpt213.as4.model.game_element;

import ca.cmpt213.as4.model.Direction;

import static ca.cmpt213.as4.model.Direction.*;


/*
cat element in maze
 */
public class Cat extends GameElement {

    public Cat(int row, int col) {
        super(row, col);
    }

    private Direction lastMove;

    private void setRow(int row) {
        this.row = row;
    }

    private void setCol(int col) {
        this.col = col;
    }

    // move dependent on the direction
    public void move(Direction direction) {
        if (direction == left) {
            setCol(this.col - 1);
        } else if (direction == up) {
            setRow(this.row - 1);
        } else if (direction == right) {
            setCol(this.col + 1);
        } else if (direction == down) {
            setRow(this.row + 1);
        }
    }

    public Direction getLastMove() {
        return lastMove;
    }

    public void setLastMove(Direction lastMove) {
        this.lastMove = lastMove;
    }

}
