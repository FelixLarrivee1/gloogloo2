package org.equipe39.domain.Cut;


import org.equipe39.domain.CutLine;
import org.equipe39.domain.Outil;
import org.equipe39.domain.PointReference;
import org.equipe39.dto.LDTO;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.UUID;

public class L extends Cut {

    private List<CutLine> cutLines;
    private static final double HITBOX_SIZE = 20;
    private PointReference referencePoint;
    private double x, y;
    private double widthFactor, heightFactor;
    private Outil tool;
    private double profondeur;
    private boolean initialXPositive;
    private boolean initialYPositive;

    public L(double x, double y, Outil tool, double profondeur, PointReference referencePoint) {
        this.id = UUID.randomUUID();

        this.x = x;
        this.y = y;

        this.tool = tool;
        this.profondeur = profondeur;
        this.referencePoint = referencePoint;

        //width and height factor are not set and should be the length of x and y of the L and the reference point
        this.widthFactor = this.x - this.referencePoint.getX();
        this.heightFactor = this.y - this.referencePoint.getY();

        initialXPositive = (this.widthFactor >= 0);
        initialYPositive = (this.heightFactor >= 0);


    }



    public L(UUID id, boolean isSelected, double x, double y, Outil tool, double profondeur, PointReference referencePoint) {
        this.id = id;
        this.isSelected = isSelected;
        this.x = x;
        this.y = y;
        this.tool = tool;
        this.profondeur = profondeur;
        this.referencePoint = referencePoint;

        
        this.widthFactor = this.x - this.referencePoint.getX();
        this.heightFactor = this.y - this.referencePoint.getY();
    }





@Override
public boolean isClicked(double mouseX, double mouseY) {
    List<CutLine> cutLines = getCutLines();
    Point2D.Double mousePoint = new Point2D.Double(mouseX, mouseY);

    for (CutLine cutLine : cutLines) {
        Point2D.Double start = new Point2D.Double(cutLine.getStart().getX(), cutLine.getStart().getY());
        Point2D.Double end = new Point2D.Double(cutLine.getEnd().getX(), cutLine.getEnd().getY());

        double distance = distancePointToSegment(mousePoint, start, end);
        if (distance <= HITBOX_SIZE) {
            return true;
        }
    }
    return false;
}

private double distancePointToSegment(Point2D.Double point, Point2D.Double lineStart, Point2D.Double lineEnd) {
    double x0 = point.getX();
    double y0 = point.getY();
    double x1 = lineStart.getX();
    double y1 = lineStart.getY();
    double x2 = lineEnd.getX();
    double y2 = lineEnd.getY();

    double dx = x2 - x1;
    double dy = y2 - y1;
    double lineLengthSquared = dx * dx + dy * dy;

    if (lineLengthSquared == 0) {
        return point.distance(lineStart);
    }

    double t = ((x0 - x1) * dx + (y0 - y1) * dy) / lineLengthSquared;
    t = Math.max(0, Math.min(1, t)); // Clamp t to the segment [0,1]

    double closestX = x1 + t * dx;
    double closestY = y1 + t * dy;

    double distance = point.distance(closestX, closestY);
    return distance;
}



    @Override
    public LDTO toDTO() {
        return ((this.tool == null) ?  new LDTO(id, isSelected, x, y, widthFactor, heightFactor, null, profondeur, this.referencePoint.toDTO()) : new  LDTO(id, isSelected, x, y, widthFactor, heightFactor, tool.toDTO(), profondeur, referencePoint.toDTO()));

        //return new LDTO(id, isSelected, x, y, widthFactor, heightFactor, tool.toDTO(), profondeur, referencePoint.toDTO());
    }


    public double getX() {
        return x;
    }

    public void setX(double x) {

        this.x = x;
        updateFactors();


    }

    public double getY() {
        return y;
    }

    public void setY(double y) {


        this.y = y;
        updateFactors();

    }

    public double getProfondeur() {
        return this.profondeur;
    }

    public double getWidthFactor() {
        return widthFactor;
    }

    public void setWidthFactor(double newWidth) {
//        if (this.x < this.referencePoint.getX()) {
//            setX(this.referencePoint.getX() - newWidth);
//        } else {
//            setX(this.referencePoint.getX() + newWidth);
//        }
        this.widthFactor = newWidth;
//        double absWidth = Math.abs(newWidth);
//        // Si initialement on était du côté positif de X
//        if (initialXPositive) {
//            this.widthFactor = absWidth;
//            this.x = this.referencePoint.getX() + absWidth;
//        } else {
//            this.widthFactor = -absWidth;
//            this.x = this.referencePoint.getX() - absWidth;
//        }
        //updateFactors();

    }

    public void setWidthFactor2(double newWidth) {
        if (this.x < this.referencePoint.getX()) {
            setX(this.referencePoint.getX() - newWidth);
        } else {
            setX(this.referencePoint.getX() + newWidth);
        }
    }




    public double getHeightFactor() {
        return heightFactor;
    }

    public void setHeightFactor(double newHeight) {
//        if (this.y < this.referencePoint.getY()) {
//            setY(this.referencePoint.getY() - newHeight);
//        } else {
//            setY(this.referencePoint.getY() + newHeight);
//        }
        this.heightFactor = newHeight;
//        double absHeight = Math.abs(newHeight);
//        // Si initialement on était du côté positif de Y
//        if (initialYPositive) {
//            this.heightFactor = absHeight;
//            this.y = this.referencePoint.getY() + absHeight;
//        } else {
//            this.heightFactor = -absHeight;
//            this.y = this.referencePoint.getY() - absHeight;
//        }
        //updateFactors();

    }

    public void setHeightFactor2(double newHeight) {
        if (this.y < this.referencePoint.getY()) {
            setY(this.referencePoint.getY() - newHeight);
        } else {
            setY(this.referencePoint.getY() + newHeight);
        }
    }

    //update the height factor and width factor when the reference point is moved
    public void updateFactors(){
        System.out.println("updateFactors triggered");

        System.out.println("hauteur: " + heightFactor);
        System.out.println("largeur: " + widthFactor);
        System.out.println("==========================================================");
//        if (this.y < this.referencePoint.getY()) {
//            setY(this.referencePoint.getY() - heightFactor);
//        } if (this.y > this.referencePoint.getY()) {
//            setY(this.referencePoint.getY() + heightFactor);
//        }
//        if (this.x < this.referencePoint.getX()) {
//            setX(this.referencePoint.getX() - widthFactor);
//        } if (this.x < this.referencePoint.getX()) {
//            setX(this.referencePoint.getX() + widthFactor);
//        }

        ///

//        if (this.y < this.referencePoint.getY()) {
//            this.widthFactor =-( this.x - this.referencePoint.getX());
//
//        } else {
//            this.widthFactor = (this.x - this.referencePoint.getX());
//
//        }
//        if (this.x < this.referencePoint.getX()) {
//            this.heightFactor = -(this.y - this.referencePoint.getY());
//
//        } else {
//            this.heightFactor = (this.y - this.referencePoint.getY());
//        }


//        if (this.y < this.referencePoint.getY()) {
//            this.widthFactor = -(this.x - this.referencePoint.getX());
//        } else {
//            this.widthFactor = (this.x - this.referencePoint.getX());
//        }
//
//        if (this.x < this.referencePoint.getX()) {
//            this.heightFactor = -(this.y - this.referencePoint.getY());
//        } else {
//            this.heightFactor = (this.y - this.referencePoint.getY());
//        }


        this.widthFactor = this.x - this.referencePoint.getX();
        this.heightFactor = this.y - this.referencePoint.getY();
    }



    public PointReference getReferencePoint() {
        //this.updateFactors();
        return referencePoint;
    }

    public void setReferencePoint(PointReference referencePoint) {
        //keep the exact same distance between the reference point and the L shape
        
        //if reference point belongs to square 
        if(referencePoint.getCutLineOrigin1().getCut() instanceof Square && referencePoint.getCutLineOrigin2().getCut().id ==  referencePoint.getCutLineOrigin1().getCut().id){
//            Square square = (Square)  referencePoint.getCutLineOrigin2().getCut();
            this.x = referencePoint.getX() + this.widthFactor;
            this.y = referencePoint.getY() + this.heightFactor;
                                

        }
        else
        {
            this.updateFactors();
        }
        




        this.referencePoint = referencePoint;

    }

    public Outil getTool() {
        return tool;
    }

    public void setTool(Outil tool) {
        this.tool = tool;
    }

    //move to (x, y)
    public void moveTo(double x, double y){
        this.x = x;
        this.y = y;
        this.updateFactors();
    }


    //tostring
    /*@Override
    public String toString() {
        return "L{" +
                "id=" + id +
                "referencePoint=" + referencePoint +
                ", x=" + x +
                ", y=" + y +
                ", widthFactor=" + widthFactor +
                ", heightFactor=" + heightFactor +
                ", tool=" + tool +
                ", profondeur=" + profondeur +
                '}';
    }*/

    @Override
    //tostring
    public String toString() {
        return "L{" +
                "id" + id +
                '}';
    }

    public List<CutLine> getCutLines() {
        this.updateFactors();
        System.out.println("getCutLines triggered");

        // Get the start and end points for the vertical and horizontal lines of the L-shape


        //poimtimg to the right
        if(y > getReferencePoint().getY() && x > getReferencePoint().getX()){
            Point2D.Double start1 = new Point2D.Double(x, y);
            Point2D.Double end1 = new Point2D.Double(x - 1, y - heightFactor - 1); // Vertical line end
            Point2D.Double start2 = new Point2D.Double(x, y);
            Point2D.Double end2 = new Point2D.Double(x - widthFactor - 1, y - 1); // Horizontal line end
            this.cutLines = List.of(
                    new CutLine(1, start1, end1, this),  // Vertical line
                    new CutLine(2, start2, end2, this)   // Horizontal line
            );
            return this.cutLines;
        }
        //pointing to the left
        else if(y > getReferencePoint().getY() && x < getReferencePoint().getX()){
            Point2D.Double start1 = new Point2D.Double(x, y);
            Point2D.Double end1 = new Point2D.Double(x + 1, y - heightFactor - 1); // Vertical line end
            Point2D.Double start2 = new Point2D.Double(x, y);
            Point2D.Double end2 = new Point2D.Double(x - widthFactor + 1, y - 1); // Horizontal line end
            this.cutLines = List.of(
                    new CutLine(1, start1, end1, this),  // Vertical line
                    new CutLine(2, start2, end2, this)   // Horizontal line
            );
            return this.cutLines;
        }

        //pointing to the bottom
        else if(y < getReferencePoint().getY() && x > getReferencePoint().getX()){
            Point2D.Double start1 = new Point2D.Double(x, y);
            Point2D.Double end1 = new Point2D.Double(x - 1, y - heightFactor + 1); // Vertical line end
            Point2D.Double start2 = new Point2D.Double(x, y);
            Point2D.Double end2 = new Point2D.Double(x - widthFactor - 1, y + 1); // Horizontal line end
            this.cutLines = List.of(
                    new CutLine(1, start1, end1, this),  // Vertical line
                    new CutLine(2, start2, end2, this)   // Horizontal line
            );
            return this.cutLines;
        }
        //pointing to the top
        else if(y < getReferencePoint().getY() && x < getReferencePoint().getX()){
            Point2D.Double start1 = new Point2D.Double(x, y);
            Point2D.Double end1 = new Point2D.Double(x + 1, y - heightFactor + 1); // Vertical line end
            Point2D.Double start2 = new Point2D.Double(x, y);
            Point2D.Double end2 = new Point2D.Double(x - widthFactor + 1, y + 1); // Horizontal line end
            this.cutLines = List.of(
                    new CutLine(1, start1, end1, this),  // Vertical line
                    new CutLine(2, start2, end2, this)   // Horizontal line
            );
            return this.cutLines;
        }

        //Point2D start1 = new Point2D.Double(x, y);
        //Point2D end1 = new Point2D.Double(x, y - heightFactor); // Vertical line end
        //Point2D start2 = new Point2D.Double(x, y);
        //Point2D end2 = new Point2D.Double(x - widthFactor, y); // Horizontal line end




        //this.cutLines = List.of(
        //        new CutLine(1, start1, end1, this),  // Vertical line
        //        new CutLine(2, start2, end2, this)   // Horizontal line
        //);

        return this.cutLines;



    }


    //equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        L l = (L) o;

        return id.equals(l.id) && l.x == x && l.y == y && l.widthFactor == widthFactor && l.heightFactor == heightFactor && l.tool.equals(tool) && l.profondeur == profondeur && l.referencePoint.equals(referencePoint);
    }


    //TODO: FINISH
    @Override
    public boolean isTouching(Cut cut) {
        return false;
    }

    //clone
    @Override
    public L clone() {
        return new L(this.id, this.isSelected, this.x, this.y, this.tool, this.profondeur, this.referencePoint);
    }


}