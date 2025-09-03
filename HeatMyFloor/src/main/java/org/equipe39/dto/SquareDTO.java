
package org.equipe39.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.equipe39.domain.Cut.Cut;
import org.equipe39.domain.Cut.Square;
import org.equipe39.domain.InteractiveEntity;
import org.equipe39.domain.Outil;
import org.equipe39.domain.PointReference;
import org.equipe39.domain.Outil;

import java.awt.*;
import java.util.UUID;

public class SquareDTO extends CutDTO {
    public double x, y;
    public double widthFactor, heightFactor;
    public OutilDTO tool;
    public double profondeur;
    public PointReferenceDTO referencePoint;

    @JsonCreator
    public SquareDTO(@JsonProperty("id") UUID id,
                     @JsonProperty("isSelected") boolean isSelected,
                     @JsonProperty("x") double x,
                     @JsonProperty("y") double y,
                     @JsonProperty("widthFactor") double widthFactor,
                     @JsonProperty("heightFactor") double heightFactor,
                     @JsonProperty("tool") OutilDTO tool,
                     @JsonProperty("profondeur") double profondeur,
                     @JsonProperty("referencePoint") PointReferenceDTO referencePoint) {
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