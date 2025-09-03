package org.equipe39.domain.Cut;


import org.equipe39.domain.CutLine;
import org.equipe39.domain.Outil;
import org.equipe39.domain.PointReference;
import org.equipe39.dto.CutDTO;
import org.equipe39.dto.SquareDTO;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.UUID;

public class Square extends Cut {

    private PointReference referencePoint;
    private double x, y;
    private double widthFactor, heightFactor;
    private Outil tool;
    private double profondeur;
    private static final double HITBOX_SIZE = 20.0;
    private double originalPointReferenceDistanceX, originalPointReferenceDistanceY;
    public Square(double x, double y, double widthFactor, double heightFactor, Outil tool, double profondeur, PointReference referencePoint) {
        /*this.id = UUID.randomUUID();

        this.x = x;
        this.y = y;
        this.widthFactor = widthFactor;
        this.heightFactor = heightFactor;
        this.color = color;
        this.tool = tool;
        this.profondeur = profondeur;
        this.referencePoint = referencePoint;

        this.originalPointReferenceDistanceX = this.x - this.referencePoint.getX();
        this.originalPointReferenceDistanceY = this.y - this.referencePoint.getY();*/

        this.id = UUID.randomUUID();

        this.x = x;
        this.y = y;
        this.widthFactor = widthFactor;
        this.heightFactor = heightFactor;

        this.tool = tool;
        this.profondeur = profondeur;
        this.referencePoint = referencePoint;

        this.originalPointReferenceDistanceX = this.x - this.referencePoint.getX();
        this.originalPointReferenceDistanceY = this.y - this.referencePoint.getY();

    }



    public Square(UUID id, boolean isSelected, double x, double y, double widthFactor, double heightFactor, Outil tool, double profondeur, PointReference referencePoint) {
        this.id = id;
        this.isSelected = isSelected;
        this.x = x;
        this.y = y;
        this.widthFactor = widthFactor;
        this.heightFactor = heightFactor;

        this.tool = tool;
        this.profondeur = profondeur;
        this.referencePoint = referencePoint;
    }

    //getters and setters
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidthFactor() {
        return widthFactor;
    }

    public double getHeightFactor() {
        return heightFactor;
    }


    public Outil getTool() {
        return tool;
    }

    public double getProfondeur() {
        return profondeur;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setWidthFactor(double widthFactor) {
        this.widthFactor = widthFactor;
    }

    public void setHeightFactor(double heightFactor) {
        this.heightFactor = heightFactor;
    }


    public void setTool(Outil tool) {
        this.tool = tool;
    }

    public void setProfondeur(double profondeur) {
        this.profondeur = profondeur;
    }

    public void changeSize(double width, double height)
    {
        this.widthFactor = width;
        this.heightFactor = height;
    }


    public void moveTo(double newX, double newY) {
        this.x = newX;
        this.y = newY;
    }

    public PointReference getReferencePoint()
    {
        return this.referencePoint;
    }

    public void setReferencePoint(PointReference referencePoint)
    {
        this.referencePoint = referencePoint;
    }

    //REFERENCE POINT UPDATE SHOULD ALSO UPDATE THE X AND Y
    

    @Override
    public boolean isClicked(double mouseX, double mouseY) {
        List<CutLine> cutLines = getCutLines();
        Point2D.Double mousePoint = new Point2D.Double(mouseX, mouseY);

        for (CutLine cutLine : cutLines) {
            Point2D.Double start = cutLine.getStart();
            Point2D.Double end = cutLine.getEnd();

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
    public CutDTO toDTO() {
        return ((this.tool == null) ?  new SquareDTO(id, isSelected, x, y, widthFactor, heightFactor, null, profondeur, referencePoint.toDTO()) : new SquareDTO(id, isSelected, x, y, widthFactor, heightFactor, tool.toDTO(), profondeur, referencePoint.toDTO()));

        //return new SquareDTO(id, isSelected, x, y, widthFactor, heightFactor, tool.toDTO(), profondeur, referencePoint.toDTO());
    }


    /*@Override
    public String toString() {
        return "Square{" +
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
        return "Square{" +
                "id" + id +
                '}';
    }
    public List<CutLine> getCutLines() {
        Point2D.Double start = new Point2D.Double(x, y);
        Point2D.Double end = new Point2D.Double(x +  (widthFactor), y);
        Point2D.Double start2 = new Point2D.Double(x, y);
        Point2D.Double end2 = new Point2D.Double(x, y +  (heightFactor));
        Point2D.Double start3 = new Point2D.Double(x +  (widthFactor), y);
        Point2D.Double end3 = new Point2D.Double(x + (widthFactor), y +  (heightFactor));
        Point2D.Double start4 = new Point2D.Double(x, y +  (heightFactor));
        Point2D.Double end4 = new Point2D.Double(x +  (widthFactor), y +  (heightFactor));
        return List.of(new CutLine(1, start, end, this),
                new CutLine(2, start2, end2, this),
                new CutLine(3, start3, end3, this),
                new CutLine(4, start4, end4, this));

    }

    @Override
    public boolean isTouching(Cut cut) {
        if (cut instanceof RestrictedArea) {
            RestrictedArea restrictedArea = (RestrictedArea) cut;
            return this.x < restrictedArea.getX() + restrictedArea.getWidthFactor() && this.x + this.widthFactor > restrictedArea.getX() && this.y < restrictedArea.getY() + restrictedArea.getHeightFactor() && this.y + this.heightFactor > restrictedArea.getY();
        }
        else
        {
            for (CutLine cutLine : cut.getCutLines()) {
                for (CutLine thisCutLine : this.getCutLines()) {
                    if (cutLine.getIntersection(thisCutLine) != null) {
                        return true;
                    }
                }
            }
        }

        
        return false;
    }


    @Override
    //equals
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Square square = (Square) o;

        return id.equals(square.id) && square.x == x && square.y == y && square.widthFactor == widthFactor && square.heightFactor == heightFactor && square.tool.equals(tool) && square.profondeur == profondeur && square.referencePoint.equals(referencePoint);
    }

    //clone
    @Override
    public Square clone() {
        return new Square(this.id, this.isSelected, this.x, this.y, this.widthFactor, this.heightFactor, this.tool, this.profondeur, this.referencePoint);
    }
}