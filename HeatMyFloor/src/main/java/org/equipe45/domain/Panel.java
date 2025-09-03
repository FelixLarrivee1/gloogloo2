package org.domain;

import java.awt.*;
import java.util.List;

public class Panel {
    private int x, y;
    private double widthFactor, heightFactor;
    //private double initialGridSize;
    public Panel(int x, int y, double widthFactor, double heightFactor/*, double initialGridSize*/) {
        this.x = x;
        this.y = y;
        this.widthFactor = widthFactor;
        this.heightFactor = heightFactor;
        //this.initialGridSize = initialGridSize;
    }

    public List<CutLine> getCutLines() {
        //should return every line of the panel, for now just the border
        Point start = new Point(x, y);
        Point end = new Point(x + (int) (widthFactor), y);
        Point start2 = new Point(x, y);
        Point end2 = new Point(x, y + (int) (heightFactor));
        Point start3 = new Point(x + (int) (widthFactor), y);
        Point end3 = new Point(x + (int) (widthFactor), y + (int) (heightFactor));
        Point start4 = new Point(x, y + (int) (heightFactor));
        Point end4 = new Point(x + (int) (widthFactor), y + (int) (heightFactor));
        return List.of(new CutLine(start, end, null), new CutLine(start2, end2, null), new CutLine(start3, end3, null), new CutLine(start4, end4, null));

    }

    public void changeSize(double width, double height)
    {
        this.widthFactor = width;
        this.heightFactor = height;
    }

    public double getHeightFactor() {
        return heightFactor;
    }

    public double getWidthFactor() {
        return widthFactor;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /*public double getInitialGridSize() {
        return initialGridSize;
    }*/

    public void setHeightFactor(double heightFactor) {
        this.heightFactor = heightFactor;
    }



}


