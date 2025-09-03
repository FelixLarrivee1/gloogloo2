
package org.equipe39.dto;


import org.equipe39.domain.Cut.Cut;
import org.equipe39.domain.Cut.Square;

import java.util.UUID;

public class SquareDTO extends CutDTO {
    public double x, y;
    public double widthFactor, heightFactor;
    public OutilDTO tool;
    public double profondeur;
    public PointReferenceDTO referencePoint;

    
    public SquareDTO( UUID id,
                      boolean isSelected,
                      double x,
                      double y,
                      double widthFactor,
                     double heightFactor,
                     OutilDTO tool,
                      double profondeur,
                      PointReferenceDTO referencePoint) {
        super(id, isSelected);
        this.id =id;
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
        return "SquareDTO{" +
                "id: " + this.id +
                '}';
    }

    public Cut toCut() {
        return new Square(id, isSelected, x, y, widthFactor, heightFactor, tool.toOutil(), profondeur, referencePoint.toPointReference());
    }
}