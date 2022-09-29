package sortingvisualizer;

import java.awt.Color;

/**
 * Class responsible for creating line objects
 *
 * @author eroe
 */
public class Line {

    int x1;
    int y1;
    int x2;
    int y2;
    Color color;

    /**
     * Constructs a new Line object with given length, width, and color.
     *
     * @param x1 The first x coordinate the line is drawn from.
     * @param y1 The first y coordinate the line is drawn from.
     * @param x2 The second x coordinate the line is drawn to.
     * @param y2 The second y coordinate the line is drawn to.
     * @param color The color of the line
     */
    public Line(int x1, int y1, int x2, int y2, Color color) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;

    }

    /**
     * Method that sets the x coordinates of this line object.
     *
     * @param x1 The first x coordinate the line is drawn from.
     * @param x2 The second x coordinate the line is drawn to.
     */
    public void setxs(int x1, int x2) {
        this.x1 = x1;
        this.x2 = x2;
    }

    /**
     * Method that returns 'this' lines x1 coordinate.
     *
     * @return
     */
    public int getx1() {
        return this.x1;
    }

    /**
     * Method that returns 'this' lines x2 coordinate.
     *
     * @return
     */
    public int getx2() {
        return this.x2;
    }

    /**
     * Method that returns 'this' lines y1 coordinate.
     *
     * @return
     */
    public int gety1() {
        return this.y1;
    }

    /**
     * Method that returns 'this' lines y2 coordinate.
     *
     * @return
     */
    public int gety2() {
        return this.y2;
    }

    /**
     * Method that sets the color of 'this' line object.
     *
     * @param c
     */
    public void setColor(Color c) {
        this.color = c;
    }

    /**
     * Method that returns the size of the line by getting the difference
     * between the first and second y coordinate.
     *
     * @return
     */
    public int getYSize() {
        return Math.abs(this.y1 - this.y2);
    }

}
