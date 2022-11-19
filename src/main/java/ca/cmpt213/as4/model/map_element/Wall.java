package ca.cmpt213.as4.model.map_element;

/*
wall element in maze
 */
public class Wall extends MapElement {

    public Wall() {
        super(false);
        containCat = false;
        containCheese = false;
        containMouse = false;
        isWall = true;
    }


}
