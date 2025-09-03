package org.equipe39.domain;

import org.equipe39.dto.PointReferenceDTO;
import org.equipe39.dto.CutLineDTO;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Objects;
import java.util.UUID;

public class PointReference extends InteractiveEntity {
    private Point2D.Double point;
    //private UUID id;
    private CutLine cutLineOrigin1;
    private CutLine cutLineOrigin2;
    //private boolean isSelected = false;
    private boolean isHovered = false;
    private static final double HITBOX_SIZE = 16;

    // Constructors
    public PointReference(Point2D.Double point, CutLine cutLineOrigin1, CutLine cutLineOrigin2) {
        this.id = UUID.randomUUID();
        this.cutLineOrigin1 = cutLineOrigin1;
        this.cutLineOrigin2 = cutLineOrigin2;
        this.point = point;
    }

//    public PointReference(UUID id, Point2D.Double point, CutLine cutLineOrigin1, CutLine cutLineOrigin2) {
//        this.id = id;
//        this.cutLineOrigin1 = cutLineOrigin1;
//        this.cutLineOrigin2 = cutLineOrigin2;
//        this.point = point;
//    }

    public PointReference(UUID id, boolean isSelected, boolean isHovered, Point2D.Double point, CutLine cutLineOrigin1, CutLine cutLineOrigin2) {
        this.id = id;
        this.isSelected = isSelected;
        this.isHovered = isHovered;
        this.cutLineOrigin1 = cutLineOrigin1;
        this.cutLineOrigin2 = cutLineOrigin2;
        this.point = point;
    }

    // Click detection
    @Override
    public boolean isClicked(double x, double y) {
        return this.point.getX() - HITBOX_SIZE <= x && x <= this.point.getX() + HITBOX_SIZE
                && this.point.getY() - HITBOX_SIZE <= y && y <= this.point.getY() + HITBOX_SIZE;
    }

    // Getters and setters
    public Point2D.Double getPoint() {
        return point;
    }

    public void setPoint(Point2D.Double point) {
        this.point = point;
    }

    public CutLine getCutLineOrigin1() {
        return cutLineOrigin1;
    }

    public void setCutLineOrigin1(CutLine cutLineOrigin1) {
        this.cutLineOrigin1 = cutLineOrigin1;
    }

    public CutLine getCutLineOrigin2() {
        return cutLineOrigin2;
    }

    public void setCutLineOrigin2(CutLine cutLineOrigin2) {
        this.cutLineOrigin2 = cutLineOrigin2;
    }

    public UUID getId() {
        return id;
    }

    public double getX() {
        return this.point.getX();
    }

    public double getY() {
        return this.point.getY();
    }

    // Conversion to DTO
    public PointReferenceDTO toDTO() {
        return new PointReferenceDTO(id, isSelected, isHovered, point, cutLineOrigin1.toDTO(), cutLineOrigin2.toDTO());
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PointReference)) return false;
        PointReference that = (PointReference) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int compareTo(PointReference other) {
        if (this.getY() == other.getY()) {
            return Double.compare(this.getX(), other.getX());
        }
        return Double.compare(this.getY(), other.getY());
    }

    //equal using cutLineOrigin1 and cutLineOrigin2
    public boolean equalsWithCutlines(PointReference other) {
        //check if the id of cutline1 is the same and if the id of cutline2 is the same OR if the id of cutline1 is the same as the id of cutline2 and vice versa
        //return (this.cutLineOrigin1.getId() == (other.cutLineOrigin1.getId()) && this.cutLineOrigin2.getId() == (other.cutLineOrigin2.getId())) || (this.cutLineOrigin1.getId() == (other.cutLineOrigin2.getId()) && this.cutLineOrigin2.getId().equals(other.cutLineOrigin1.getId()));

        boolean compare = (this.cutLineOrigin1.equals(other.cutLineOrigin1) && this.cutLineOrigin2.equals(other.cutLineOrigin2)) || (this.cutLineOrigin1.equals(other.cutLineOrigin2) && this.cutLineOrigin2.equals(other.cutLineOrigin1));


        //return (this.cutLineOrigin1.equals(other.cutLineOrigin1) && this.cutLineOrigin2.equals(other.cutLineOrigin2)) || (this.cutLineOrigin1.equals(other.cutLineOrigin2) && this.cutLineOrigin2.equals(other.cutLineOrigin1));
        return compare;
    }


    @Override
    public String toString() {
        return "PointReference{" +
                "id=" + id +
                " at (" + point.getX() + "," + point.getY() + ")" +
                ", belongs to " + cutLineOrigin1.getId() + "th line of " + cutLineOrigin1.getCut() +
                ", and " + cutLineOrigin2.getId() + "th line of " + cutLineOrigin2.getCut() +
                ", and isSelected:" + this.isSelected +
                '}';
    }

    // Update hover and selection states
    public void setHovered(boolean hovered) {
        this.isHovered = hovered;
    }

    public boolean isHovered() {
        return isHovered;
    }


    public int getCutLineOrigin1Id() {
        return cutLineOrigin1.getId();
    }
    public int getCutLineOrigin2Id()
    {
        return cutLineOrigin2.getId();
    }

    public void setX(double x) {
        this.point.setLocation(x, this.point.getY());
    }

    public void setY(double y) {
        this.point.setLocation(this.point.getX(), y);
    }

    //clone
    @Override
    public PointReference clone() {
        //return new PointReference(this.id, this.isSelected, this.isHovered, this.point, this.cutLineOrigin1, this.cutLineOrigin2);
        return new PointReference(this.id, this.isSelected, this.isHovered, this.point, this.cutLineOrigin1.clone(), this.cutLineOrigin2.clone());
    }
}
