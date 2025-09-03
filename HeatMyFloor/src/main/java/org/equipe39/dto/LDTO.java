


package org.equipe39.dto;



import org.equipe39.domain.Cut.L;

import java.util.UUID;

public class LDTO extends CutDTO {
    public double x, y;
    public double widthFactor, heightFactor;
    public OutilDTO tool;
    public double profondeur;
    public PointReferenceDTO referencePoint;


    
    public LDTO( UUID id,
                 boolean isSelected,
                 double x,
                 double y,
                 double widthFactor,
                double heightFactor,
                OutilDTO tool,
                 double profondeur,
                 PointReferenceDTO referencePoint) {
        super(id, isSelected);
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


    @Override
    public String toString() {
        return "LDTO{" +
                "id: " + this.id +
                '}';
    }
    @Override
    public L toCut() {
        return new L(id, isSelected, x, y, tool.toOutil(), profondeur, referencePoint.toPointReference());
    }

    /*@Override
    public InteractiveEntity toInteractiveEntity() {
        return toCut();
    }*/
}