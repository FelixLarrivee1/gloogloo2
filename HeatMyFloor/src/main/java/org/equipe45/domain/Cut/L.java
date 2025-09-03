package org.domain.Cut;


import org.domain.CutLine;
import org.equipe45.domain.PointReference;
import org.equipe45.dto.CutDTO;
import org.equipe45.dto.LDTO;
import org.equipe45.dto.PointReferenceDTO;
import org.equipe45.dto.SquareDTO;

import java.util.List;
import java.util.UUID;

public class L extends Cut {

    private static final int HITBOX_SIZE = 20;
    private PointReference referencePoint;
    private int x, y;
    private double widthFactor, heightFactor;
    private Color color;
    private UUID tool;
    private int thickness;
    public L(int x, int y, Color color, UUID tool, int thickness, PointReference referencePoint) {
        this.id = UUID.randomUUID();

        this.x = x;
        this.y = y;

        this.color = color;
        this.tool = tool;
        this.thickness = thickness;
        this.referencePoint = referencePoint;

        //width and height factor are not set and should be the length of x and y of the L and the reference point
        this.widthFactor = this.x - this.referencePoint.getX();
        this.heightFactor = this.y - this.referencePoint.getY();

    }



    public L(UUID id, boolean isSelected, int x, int y, Color color, UUID tool, int thickness, PointReference referencePoint) {
        this.id = id;
        this.isSelected = isSelected;
        this.x = x;
        this.y = y;
        this.color = color;
        this.tool = tool;
        this.thickness = thickness;
        this.referencePoint = referencePoint;


        
        this.widthFactor = this.x - this.referencePoint.getX();
        this.heightFactor = this.y - this.referencePoint.getY();
    }

    /*public void moveTo(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }*/

    @Override
    public boolean isClicked(int mouseX, int mouseY) {
        //CHECK IF EITHER OF THE TWO LINES ARE CLICKED

        int x1 = this.x;
        int y1 = this.y;
        int x2 = this.x + (int) (widthFactor);
        int y2 = this.y;
        int x3 = this.x;
        int y3 = this.y;
        int x4 = this.x;
        int y4 = this.y + (int) (heightFactor);

        //check if the mouse is close to the line
        if (Math.abs((y2 - y1) * mouseX - (x2 - x1) * mouseY + x2 * y1 - y2 * x1) / Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2)) < HITBOX_SIZE) {
            return true;
        }
        if (Math.abs((y3 - y4) * mouseX - (x3 - x4) * mouseY + x3 * y4 - y3 * x4) / Math.sqrt(Math.pow(y3 - y4, 2) + Math.pow(x3 - x4, 2)) < HITBOX_SIZE) {
            return true;
        }
        return false;
    }

    @Override
    public LDTO toDTO() {
        return new LDTO(id, isSelected, x, y, widthFactor, heightFactor, color, tool, thickness, referencePoint.toDTO());
    }


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
    //update the height factor and width factor when the reference point is moved
    public void updateFactors(){
        this.widthFactor = this.x - this.referencePoint.getX();
        this.heightFactor = this.y - this.referencePoint.getY();
    }

    public PointReference getReferencePoint() {
        return referencePoint;
    }

    public void setReferencePoint(PointReference referencePoint) {
        this.referencePoint = referencePoint;
        this.updateFactors();
    }

    //move to (x, y)
    public void moveTo(int x, int y){
        this.x = x;
        this.y = y;
        this.updateFactors();
    }


    public List<CutLine> getCutLines() {
        /*Point start = new Point(x, y);
        Point end = new Point(x + (int) (widthFactor), y);
        Point start2 = new Point(x, y);
        Point end2 = new Point(x, y + (int) (heightFactor));
        Point start3 = new Point(x + (int) (widthFactor), y);
        Point end3 = new Point(x + (int) (widthFactor), y + (int) (heightFactor));
        Point start4 = new Point(x, y + (int) (heightFactor));
        Point end4 = new Point(x + (int) (widthFactor), y + (int) (heightFactor));
        return List.of(new CutLine(start, end, this), new CutLine(start2, end2, this), new CutLine(start3, end3, this), new CutLine(start4, end4, this));*/
        //do this but for L so only 2 lines
        Point start = new Point(x, y);
        Point end = new Point(x + (int) (widthFactor), y);
        Point start2 = new Point(x, y);
        Point end2 = new Point(x, y + (int) (heightFactor));
        return List.of(new CutLine(start, end, this), new CutLine(start2, end2, this));

    }
}