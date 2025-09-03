package org.equipe39.domain.Cut;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import org.equipe39.domain.ConversionPiedMM;
import org.equipe39.domain.CutLine;
import org.equipe39.dto.RestrictedAreaDTO;

import java.awt.*;
import java.util.UUID;

public class RestrictedArea extends Cut {
    private double x, y;
    private double widthFactor, heightFactor;

    public RestrictedArea(double x, double y, double widthFactor, double heightFactor) {
        this.id = UUID.randomUUID();

        this.x = x;
        this.y = y;
        this.widthFactor = widthFactor;
        this.heightFactor = heightFactor;
    }

    public RestrictedArea(UUID id, boolean isSelected, double x, double y, double widthFactor, double heightFactor) {
        this.id = id;
        this.isSelected = isSelected;
        this.x = x;
        this.y = y;
        this.widthFactor = widthFactor;
        this.heightFactor = heightFactor;
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
    public boolean isClicked(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + widthFactor && mouseY >= y && mouseY <= y + heightFactor;
    }

    @Override
    public RestrictedAreaDTO toDTO() {
        return new RestrictedAreaDTO(this.id, this.isSelected, this.x, this.y, this.widthFactor, this.heightFactor);
    }

    

    @Override
    public List<CutLine> getCutLines() {
        return new ArrayList<CutLine>();
    }



    public boolean isCutLineWithinRestrictedAreaBounds(List<CutLine> cutLines) {
        double thisLeft = this.x;
        double thisRight = this.x + this.widthFactor;
        double thisTop = this.y;
        double thisBottom = this.y + this.heightFactor;


        for (CutLine cutLine : cutLines) {
            Point2D.Double start = cutLine.getStart();
            Point2D.Double end = cutLine.getEnd();


            //if (isPointInsideRect(start, thisLeft, thisRight, thisTop, thisBottom) ||
            //        isPointInsideRect(end, thisLeft, thisRight, thisTop, thisBottom)) {
            //    return true;
            //}
            if (isClicked(start.x, start.y) ||
                    this.isClicked(end.x,end.y)) {
                return true;
            }


            CutLine topEdge = new CutLine(-1, new Point2D.Double(thisLeft, thisTop),
                    new Point2D.Double(thisRight, thisTop), null);
            CutLine bottomEdge = new CutLine(-1, new Point2D.Double(thisLeft, thisBottom),
                    new Point2D.Double(thisRight, thisBottom), null);
            CutLine leftEdge = new CutLine(-1, new Point2D.Double(thisLeft, thisTop),
                    new Point2D.Double(thisLeft, thisBottom), null);
            CutLine rightEdge = new CutLine(-1, new Point2D.Double(thisRight, thisTop),
                    new Point2D.Double(thisRight, thisBottom), null);

            if (cutLine.getIntersection(topEdge) != null ||
                    cutLine.getIntersection(bottomEdge) != null ||
                    cutLine.getIntersection(leftEdge) != null ||
                    cutLine.getIntersection(rightEdge) != null) {
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean isTouching(Cut cut) {
        double thisLeft = this.x;
        double thisRight = this.x + this.widthFactor;
        double thisTop = this.y;
        double thisBottom = this.y + this.heightFactor;

        List<CutLine> cutLines = cut.getCutLines();


        return this.isCutLineWithinRestrictedAreaBounds(cutLines);
        //return false;
    }

    /*public boolean isCutLineWithinRestrictedAreaBounds(List<CutLine> cutLines)
    {
        for(CutLine cutLine : cutLines)
        {

        }

        return false;
        //return true
    }


    @Override
    public boolean isTouching(Cut cut) {
        double thisLeft = this.x;
        double thisRight = this.x + this.widthFactor;
        double thisTop = this.y;
        double thisBottom = this.y + this.heightFactor;

        List<CutLine> cutLines = cut.getCutLines();


        return this.isCutLineWithinRestrictedAreaBounds(cutLines);
        //return false;
    }*/
    

    /*
    @Override
    public boolean isTouching(Cut cut) {
        double thisLeft = this.x;
        double thisRight = this.x + this.widthFactor;
        double thisTop = this.y;
        double thisBottom = this.y + this.heightFactor;

        if (cut instanceof VerticalLine) {
            VerticalLine verticalLine = (VerticalLine) cut;
            double thickness = (verticalLine.getTool() != null) ? verticalLine.getTool().getEpaisseur() : 1.0;
            double lineLeft = verticalLine.getX();
            double lineRight = lineLeft + thickness/2;
            // Intersection horizontale seulement, car une ligne verticale s'étend sur toute la hauteur théorique.
            // Ici, on simplifie en considérant qu'elle est infinie en hauteur, ou du moins suffisamment grande.
            return thisRight >= lineLeft && thisLeft <= lineRight;
        }
        else if (cut instanceof HorizontalLine) {
            HorizontalLine horizontalLine = (HorizontalLine) cut;
            double thickness = (horizontalLine.getTool() != null) ? horizontalLine.getTool().getEpaisseur() : 1.0;
            double lineTop = horizontalLine.getY();
            double lineBottom = lineTop + thickness/2;
            // Intersection verticale seulement, car une ligne horizontale s'étend sur toute la largeur théorique.
            return thisBottom >= lineTop && thisTop <= lineBottom;
        }
        else if (cut instanceof Square) {
            Square square = (Square) cut;
            double squareLeft = square.getX();
            double squareRight = square.getX() + square.getWidthFactor();
            double squareTop = square.getY();
            double squareBottom = square.getY() + square.getHeightFactor();

            // Test d'intersection rectangle-rectangle
            return thisLeft < squareRight && thisRight > squareLeft && thisTop < squareBottom && thisBottom > squareTop;
        }
//        else if (cut instanceof L) {
//            L l = (L) cut;
//            double lLeft = l.getX();
//            double lRight = l.getX() + l.getWidthFactor();
//            double lTop = l.getY();
//            double lBottom = l.getY() + l.getHeightFactor();
//
//            // Intersection rectangle-rectangle simplifiée
//            return thisLeft < lRight && thisRight > lLeft && thisTop < lBottom && thisBottom > lTop;
//        }
//        else if (cut instanceof Bordure) {
//            Bordure bordure = (Bordure) cut;
//            double bordureLeft = bordure.getX();
//            double bordureRight = bordure.getX() + bordure.getWidthFactor();
//            double bordureTop = bordure.getY();
//            double bordureBottom = bordure.getY() + bordure.getHeightFactor();
//
//            // Intersection rectangle-rectangle
//            return thisLeft < bordureRight && thisRight > bordureLeft && thisTop < bordureBottom && thisBottom > bordureTop;
//        }

        return false;
    }*/

    //setters and getters
    public double getX() {
        return x;
    }
    
    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
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



    
    //toString
    /*@Override
    public String toString() {
        return "RestrictedArea{" +
                "x=" + x +
                ", y=" + y +
                ", widthFactor=" + widthFactor +
                ", heightFactor=" + heightFactor +
                '}';
    }*/

    @Override
    //tostring
    public String toString() {
        return "RestrictedArea{" +
                "id" + id +
                '}';
    }

    //clone
    @Override
    public RestrictedArea clone() {
        return new RestrictedArea(this.id, this.isSelected, this.x, this.y, this.widthFactor, this.heightFactor);
    }


    //equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RestrictedArea restrictedArea = (RestrictedArea) o;

        return  id.equals(restrictedArea.id) && restrictedArea.x == x && restrictedArea.y == y && restrictedArea.widthFactor == widthFactor && restrictedArea.heightFactor == heightFactor;
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