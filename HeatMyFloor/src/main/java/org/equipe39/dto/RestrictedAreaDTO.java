

package org.equipe39.dto;


import org.equipe39.domain.Cut.RestrictedArea;

import java.awt.*;
import java.util.UUID;

public class RestrictedAreaDTO extends CutDTO {
    public double x, y;
    public double widthFactor, heightFactor;
    public Color color;


    
    public RestrictedAreaDTO( UUID id,
                              boolean isSelected,
                              double x,
                              double y,
                              double widthFactor,
                             double heightFactor){
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