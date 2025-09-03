


package org.dto;


import org.equipe45.domain.Cut.L;
import org.equipe45.domain.InteractiveEntity;

import java.awt.Color;
import java.util.UUID;

public class LDTO extends CutDTO {
    public int x, y;
    public double widthFactor, heightFactor;
    public Color color;
    public UUID tool;
    public int thickness;
    public PointReferenceDTO referencePoint;

    public LDTO(UUID id, boolean isSelected, int x, int y, double widthFactor, double heightFactor, Color color, UUID tool, int thickness, PointReferenceDTO referencePoint) {
        super(id, isSelected);
        this.x = x;
        this.y = y;
        this.widthFactor = widthFactor;
        this.heightFactor = heightFactor;
        this.color = color;
        this.tool = tool;
        this.thickness = thickness;
        this.referencePoint = referencePoint;
    }



    @Override
    public L toCut() {
        return new L(id, isSelected, x, y, color, tool, thickness, referencePoint.toPointReference());
    }

    /*@Override
    public InteractiveEntity toInteractiveEntity() {
        return toCut();
    }*/
}