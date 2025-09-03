/*package org.equipe39.domain;

import org.equipe39.domain.Cut.Cut;
import org.equipe39.dto.CutDTO;
import org.equipe39.dto.CutLineDTO;

import java.awt.*;

public class CutLine
{
    private Point start;
    private Point end;
    private Cut cut;

    public CutLine(Point start, Point end, Cut cut)
    {
        this.start = start;
        this.end = end;
        this.cut = cut;
    }

    public Point getStart()
    {
        return this.start;
    }

    public Point getEnd()
    {
        return this.end;
    }

    //to dto
    public CutLineDTO toDTO()
    {
        return new CutLineDTO(start, end, (CutDTO) cut.toDTO());
    }

}*/

package org.equipe39.domain;

import org.equipe39.domain.Cut.Cut;
import org.equipe39.dto.CutDTO;
import org.equipe39.dto.CutLineDTO;
import org.equipe39.dto.InteractiveEntityDTO;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CutLine {
    private final int id;
    private Point2D.Double start;
    private Point2D.Double end;
    private InteractiveEntity cut;


    public CutLine(int id, Point2D.Double start, Point2D.Double end, InteractiveEntity cut) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.cut = cut;
    }

    public Point2D.Double getStart() {
        return this.start;
    }

    public Point2D.Double getEnd() {
        return this.end;
    }

    public InteractiveEntity getCut() {
        return this.cut;
    }

    public int getId() {
        return this.id;
    }

    public void setStart(Point2D.Double start) {
        this.start = start;
    }

    public void setEnd(Point2D.Double end) {
        this.end = end;
    }

    public void setCut(Cut cut) {
        this.cut = cut;
    }

    public CutLineDTO toDTO() {
        if(cut == null)
        {
            return new CutLineDTO(id, start, end, null);
        }
        return new CutLineDTO(id, start, end, (InteractiveEntityDTO) cut.toDTO());
    }


    /*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CutLine)) return false;
        CutLine cutLine = (CutLine) o;
        return cutLine.id == this.getId() && cutLine.cut == this.cut;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }*/

    /*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CutLine cutLine = (CutLine) o;
        return id == cutLine.id && Objects.equals(cut, cutLine.cut);
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        CutLine cutLine = (CutLine) o;
        //System.out.println("FOR POINT: " + cutLine + " and " + this);
        if (cut == null) {
            //System.out.println(id == cutLine.id && cutLine.cut == null);
            return id == cutLine.id && cutLine.cut == null;
        }
        else if (cutLine.cut == null) {
            //System.out.println(id == cutLine.id && cut == null);
            return id == cutLine.id && cut == null;
        }

        boolean equalityID = id == cutLine.id;
        //boolean equalityCutId = cutLine.cut.id == cut.id;
        boolean equalityCutId = cutLine.cut.id.equals(cut.id);
        return equalityID && equalityCutId;
//        return id == cutLine.id && cutLine.cut.id == cut.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cut);
    }

    @Override
    public String toString() {
        return "CutLine{" +
                "id=" + id +
                ", start=" + start +
                ", end=" + end +
                ", cut=" + cut +
                '}';
    }





    public Point2D.Double getIntersection(CutLine other) {
        double x1 = this.getStart().getX();
        double y1 = this.getStart().getY();
        double x2 = this.getEnd().getX();
        double y2 = this.getEnd().getY();

        double x3 = other.getStart().getX();
        double y3 = other.getStart().getY();
        double x4 = other.getEnd().getX();
        double y4 = other.getEnd().getY();

        double denominator = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);

        if (Math.abs(denominator) < 1e-10) {
            return null;
        }

        double det1 = x1 * y2 - y1 * x2;
        double det2 = x3 * y4 - y3 * x4;

        double Px = (det1 * (x3 - x4) - (x1 - x2) * det2) / denominator;
        double Py = (det1 * (y3 - y4) - (y1 - y2) * det2) / denominator;

        if (isBetween(Px, x1, x2) && isBetween(Py, y1, y2) &&
                isBetween(Px, x3, x4) && isBetween(Py, y3, y4)) {
            return new Point2D.Double((int) Px, (int) Py);
        } else {
            return null;
        }
    }

    private boolean isBetween(double val, double end1, double end2) {
        double epsilon = 1e-10;
        return val >= Math.min(end1, end2) - epsilon && val <= Math.max(end1, end2) + epsilon;
    }


    //clone
    @Override
    public CutLine clone() {
        return new CutLine(this.id, this.start, this.end, this.cut);
    }

}
