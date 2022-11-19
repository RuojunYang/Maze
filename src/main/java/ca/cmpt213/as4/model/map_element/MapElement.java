package ca.cmpt213.as4.model.map_element;

/*
parent class of path and wall
 */
public class MapElement implements Cloneable {

    // viability
    protected boolean found;
    protected boolean containCheese;
    protected boolean containCat;
    protected boolean containMouse;
    // is wall or path
    protected boolean isWall;

    public boolean isWall() {
        return isWall;
    }

    public void setContainCheese(boolean containCheese) {
        this.containCheese = containCheese;
    }

    public void setContainCat(boolean containCat) {
        this.containCat = containCat;
    }

    public void setContainMouse(boolean containMouse) {
        this.containMouse = containMouse;
    }

    public MapElement(boolean found) {
        this.found = found;
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public boolean isContainCheese() {
        return containCheese;
    }

    public boolean isContainCat() {
        return containCat;
    }

    public boolean isContainMouse() {
        return containMouse;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
