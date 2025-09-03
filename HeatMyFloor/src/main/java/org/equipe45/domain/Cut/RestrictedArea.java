package org.domain.Cut;

import java.util.ArrayList;
import java.util.List;

import org.domain.CutLine;
import org.equipe45.dto.RestrictedAreaDTO;

import java.util.UUID;

public class RestrictedArea extends Cut {
    private int x, y;
    private double widthFactor, heightFactor;
    private Color color;

    public RestrictedArea(int x, int y, double widthFactor, double heightFactor, Color color) {
        this.id = UUID.randomUUID();

        this.x = x;
        this.y = y;
        this.widthFactor = widthFactor;
        this.heightFactor = heightFactor;
        this.color = color;
    }

    public RestrictedArea(UUID id, boolean isSelected, int x, int y, double widthFactor, double heightFactor, Color color) {
        this.id = id;
        this.isSelected = isSelected;
        this.x = x;
        this.y = y;
        this.widthFactor = widthFactor;
        this.heightFactor = heightFactor;
        this.color = color;
    }


    /*public boolean isInside(Cut cut) {

        if (cut instanceof Square) {
            Square square = (Square) cut;
            int thisWidth = (int) (initialGridSize * widthFactor);
            int thisHeight = (int) (initialGridSize * heightFactor);
            int otherWidth = (int) (square.initialGridSize * square.widthFactor);
            int otherHeight = (int) (square.initialGridSize * square.heightFactor);

            boolean noOverlap = (this.x + thisWidth < square.x || square.x + otherWidth < this.x ||
                    this.y + thisHeight < square.y || square.y + otherHeight < this.y);

            return !noOverlap;
            //System.out.println("test");
            //Square square = (Square) cut;
            ////System.out.println(x <= square.x && y <= square.y && x + widthFactor >= square.x + square.widthFactor && y + heightFactor >= square.y + square.heightFactor);
            //return (x <= square.x + square.widthFactor) && (x + widthFactor >= square.x);

            //return x <= square.x && y <= square.y && x + widthFactor >= square.x + square.widthFactor && y + heightFactor >= square.y + square.heightFactor;
        } else if (cut instanceof HorizontalLine) {
            HorizontalLine horizontalLine = (HorizontalLine) cut;
            return y <= horizontalLine.getY() && y + heightFactor >= horizontalLine.getY();
        } else if (cut instanceof VerticalLine) {
            VerticalLine verticalLine = (VerticalLine) cut;
            return x <= verticalLine.getX() && x + widthFactor >= verticalLine.getX();
        }
        return false;

        //return x >= this.x && x <= this.x + this.widthFactor && y >= this.y && y <= this.y + this.heightFactor;
    }*/

/*
    //TODO: rework
    public boolean isInside(Cut cut) {


        //return x >= this.x && x <= this.x + this.widthFactor && y >= this.y && y <= this.y + this.heightFactor;
        if (cut instanceof Square) {
            Square square = (Square) cut;
            int thisWidth = (int) (ConversionPiedMM.FACTEUR_CONVERSION * widthFactor);
            int thisHeight = (int) (ConversionPiedMM.FACTEUR_CONVERSION * heightFactor);
            int otherWidth = (int) (square.initialGridSize * square.widthFactor);
            int otherHeight = (int) (square.initialGridSize * square.heightFactor);

            boolean noOverlap = (this.x + thisWidth < square.x || square.x + otherWidth < this.x ||
                    this.y + thisHeight < square.y || square.y + otherHeight < this.y);

            return !noOverlap;
            //System.out.println("test");
            //Square square = (Square) cut;
            ////System.out.println(x <= square.x && y <= square.y && x + widthFactor >= square.x + square.widthFactor && y + heightFactor >= square.y + square.heightFactor);
            //return (x <= square.x + square.widthFactor) && (x + widthFactor >= square.x);

            //return x <= square.x && y <= square.y && x + widthFactor >= square.x + square.widthFactor && y + heightFactor >= square.y + square.heightFactor;
        } else if (cut instanceof HorizontalLine) {
            HorizontalLine horizontalLine = (HorizontalLine) cut;
            return y <= horizontalLine.getY() && y + heightFactor >= horizontalLine.getY();
        } else if (cut instanceof VerticalLine) {
            VerticalLine verticalLine = (VerticalLine) cut;
            return x <= verticalLine.getX() && x + widthFactor >= verticalLine.getX();
        }
        return false;

    }
*/

    @Override
    public boolean isClicked(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + widthFactor && mouseY >= y && mouseY <= y + heightFactor;
    }

    @Override
    public RestrictedAreaDTO toDTO() {
        return new RestrictedAreaDTO(this.id, this.isSelected, this.x, this.y, this.widthFactor, this.heightFactor, this.color);
    }



    @Override
    public List<CutLine> getCutLines() {
        return new ArrayList<CutLine>();
    }

    //setters and getters
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getWidthFactor() {
        return widthFactor;
    }

    public void setWidthFactor(double widthFactor) {
        this.widthFactor = widthFactor;
    }

    public double getHeightFactor() {
        return heightFactor;
    }

    public void setHeightFactor(double heightFactor) {
        this.heightFactor = heightFactor;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }







    /*public void draw(Graphics g, int currentGridSize) {
        int adjustedWidth = (int) (widthFactor * currentGridSize);
        int adjustedHeight = (int) (heightFactor * currentGridSize);
        int adjustedX = (x / initialGridSize) * currentGridSize;
        int adjustedY = (y / initialGridSize) * currentGridSize;
        g.setColor(new Color(255, 0, 0, 100));

        //g.setColor(color);
        g.fillRect(adjustedX, adjustedY, adjustedWidth, adjustedHeight);

        g.setColor(new Color(255, 0, 0, 100));
    }*/
}