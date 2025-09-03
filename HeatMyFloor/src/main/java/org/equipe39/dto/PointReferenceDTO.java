package org.equipe39.dto;


import org.equipe39.domain.InteractiveEntity;
import org.equipe39.domain.PointReference;

import java.awt.geom.Point2D;
import java.util.UUID;

public class PointReferenceDTO extends InteractiveEntityDTO {

    public Point2D.Double point;
    public UUID id;
    public boolean isHovered;
    public CutLineDTO cutLineOrigin1;
    public CutLineDTO cutLineOrigin2;
    public static final double HITBOX_SIZE = 16;

    
    public PointReferenceDTO( UUID id,
                              boolean isSelected,
                              boolean isHovered,
                              Point2D.Double point,
                              CutLineDTO cutLineOrigin1,
                              CutLineDTO cutLineOrigin2
     ) {
        super(id, isSelected);
        this.id = id;
        this.isSelected = isSelected;
        this.isHovered = isHovered;
        this.cutLineOrigin1 = cutLineOrigin1;
        this.cutLineOrigin2 = cutLineOrigin2;
        this.point = point;

    }

    public PointReference toPointReference()
    {
        return new PointReference(this.id, isSelected, isHovered, point, cutLineOrigin1.toCutLine(), cutLineOrigin2.toCutLine());
    }

    @Override
    public InteractiveEntity toInteractiveEntity() {
        return toPointReference();
    }

    @Override
    public String toString() {
        return "PointReferenceDTO{" +
                "id=" + id +
                " at (" + point.getX() + "," + point.getY() + ")" +
                ", belongs to " + cutLineOrigin1.id + "th line of " + cutLineOrigin1.cut +
                ", and " + cutLineOrigin2.id + "th line of " + cutLineOrigin2.cut +
                ", and isSelected:" + this.isSelected +
                '}';
    }
}
