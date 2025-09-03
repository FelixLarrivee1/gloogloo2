

package org.equipe39.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.equipe39.domain.Cut.RestrictedArea;
import org.equipe39.domain.InteractiveEntity;

import java.awt.*;
import java.util.UUID;

public class RestrictedAreaDTO extends CutDTO {
    public double x, y;
    public double widthFactor, heightFactor;
    public Color color;


    @JsonCreator
    public RestrictedAreaDTO(@JsonProperty("id") UUID id,
                             @JsonProperty("isSelected") boolean isSelected,
                             @JsonProperty("x") double x,
                             @JsonProperty("y") double y,
                             @JsonProperty("widthFactor") double widthFactor,
                             @JsonProperty("heightFactor") double heightFactor){
        super(id, isSelected);
        this.x = x;
        this.y = y;
        this.widthFactor = widthFactor;
        this.heightFactor = heightFactor;
    }

    @Override
    public String toString() {
        return "RestrictedArea{" +
                "id: " + this.id +
                '}';
    }

    @Override
    public RestrictedArea toCut() {
        return new RestrictedArea(id, isSelected, x, y, widthFactor, heightFactor);
    }
}