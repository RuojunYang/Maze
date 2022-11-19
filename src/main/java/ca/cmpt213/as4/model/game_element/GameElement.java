package ca.cmpt213.as4.model.game_element;

/*
parent class of Mouse, Cat and Cheese
GameElement is immutable class
 */
public class GameElement {

    protected int row;
    protected int col;

    public GameElement(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

}
