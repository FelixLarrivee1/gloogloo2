package org.equipe39.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.equipe39.domain.Cut.Cut;
import org.equipe39.domain.CutLine;
import org.equipe39.domain.InteractiveEntity;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.UUID;

public class CutLineDTO {
    public int id;
    public Point2D.Double start;
    public Point2D.Double end;
    public InteractiveEntityDTO cut;

    @JsonCreator
    public CutLineDTO(@JsonProperty("id") int id,
                      @JsonProperty("start") Point2D.Double start,
                      @JsonProperty("end") Point2D.Double end,
                      @JsonProperty("cut") InteractiveEntityDTO cut)
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

