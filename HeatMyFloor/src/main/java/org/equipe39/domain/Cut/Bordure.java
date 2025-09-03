package org.equipe39.domain.Cut;


import org.equipe39.domain.CutLine;
import org.equipe39.domain.Outil;
import org.equipe39.dto.BordureDTO;
import org.equipe39.dto.CutDTO;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.UUID;

public class Bordure extends Cut {

    private double x, y;
    private double widthFactor, heightFactor;

    private Outil tool;
    private double profondeur;
    private static final double HITBOX_SIZE = 20.0;

    public Bordure(double x, double y, double widthFactor, double heightFactor, Outil tool, double profondeur) {
        

        this.id = UUID.randomUUID();

        this.x = x;
        this.y = y;
        this.widthFactor = widthFactor;
        this.heightFactor = heightFactor;

        this.tool = tool;
        this.profondeur = profondeur;


    }



    public Bordure(UUID id, boolean isSelected, double x, double y, double widthFactor, double heightFactor, Outil tool, double profondeur) {
        this.id = id;
        this.isSelected = isSelected;
        this.x = x;
        this.y = y;
        this.widthFactor = widthFactor;
        this.heightFactor = heightFactor;
        this.tool = tool;
        this.profondeur = profondeur;
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

    /**
     * Calcule la distance entre un point et un segment de ligne.
     */
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
        return ((this.tool == null) ?  new BordureDTO(id, isSelected, x, y, widthFactor, heightFactor, null, profondeur) : new BordureDTO(id, isSelected, x, y, widthFactor, heightFactor, tool.toDTO(), profondeur));

        //return new BordureDTO(id, isSelected, x, y, widthFactor, heightFactor, tool.toDTO(), profondeur);
    }


/*    @Override
    public String toString() {
        return "Coupe bordure{" +
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
    return "Bordure{" +
            "id" + id +
            '}';
}
    public List<CutLine> getCutLines() {
        //TODO: i genuinely have no idea what coupe bordure is supposed to do

        Point2D.Double start = new Point2D.Double(x, y);
        Point2D.Double end = new Point2D.Double(x + (double) (widthFactor), y);
        Point2D.Double start2 = new Point2D.Double(x, y);
        Point2D.Double end2 = new Point2D.Double(x, y + (double) (heightFactor));
        Point2D.Double start3 = new Point2D.Double(x + (double) (widthFactor), y);
        Point2D.Double end3 = new Point2D.Double(x + (double) (widthFactor), y + (double) (heightFactor));
        Point2D.Double start4 = new Point2D.Double(x, y + (double) (heightFactor));
        Point2D.Double end4 = new Point2D.Double(x + (double) (widthFactor), y + (double) (heightFactor));
        return List.of(new CutLine(1, start, end, this),
                new CutLine(2, start2, end2, this),
                new CutLine(3, start3, end3, this),
                new CutLine(4, start4, end4, this));
        //return List.of();
    }

    @Override
    public boolean isTouching(Cut cut) {

        return false;
    }


    @Override
    //equals
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        Bordure bordure = (Bordure) o;
        return id.equals(bordure.id) && isSelected == bordure.isSelected && Double.compare(bordure.x, x) == 0 && Double.compare(bordure.y, y) == 0 && Double.compare(bordure.widthFactor, widthFactor) == 0 && Double.compare(bordure.heightFactor, heightFactor) == 0 && Double.compare(bordure.profondeur, profondeur) == 0 && tool.equals(bordure.tool);
    }


    //clone
    @Override
    public Bordure clone() {
        return new Bordure(this.id, this.isSelected, this.x, this.y, this.widthFactor, this.heightFactor, this.tool, this.profondeur);
    }
}