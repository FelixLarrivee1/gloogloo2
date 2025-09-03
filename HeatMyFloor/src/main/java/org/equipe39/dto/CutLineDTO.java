package org.equipe39.dto;


import org.equipe39.domain.CutLine;
import org.equipe39.domain.InteractiveEntity;

import java.awt.geom.Point2D;

public class CutLineDTO {
    public int id;
    public Point2D.Double start;
    public Point2D.Double end;
    public InteractiveEntityDTO cut;

    public CutLineDTO(int id,
                       Point2D.Double start,
                       Point2D.Double end,
                       InteractiveEntityDTO cut)
    {
        this.id = id;
        this.start = start;
        this.end = end;
        this.cut = cut;
    }

    public CutLine toCutLine()
    {
        return new CutLine(id, start, end, (InteractiveEntity) cut.toInteractiveEntity());

    }

    //toString
    @Override
    public String toString() {
        return "CutLineDTO{" +
                "start=" + start +
                ", end=" + end +
                ", cut=" + cut +
                '}';
    }




}

