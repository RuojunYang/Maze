package ca.cmpt213.as4.model.map_element;

/*
path element in maze
 */
public class Path extends MapElement {

    public Path() {
        super(false);
        containCat = false;
        containCheese = false;
        containMouse = false;
        isWall = false;
    }

}
