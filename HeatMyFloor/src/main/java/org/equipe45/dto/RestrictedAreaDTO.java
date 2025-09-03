

package org.dto;

import org.equipe45.domain.Cut.RestrictedArea;
import org.equipe45.domain.InteractiveEntity;

import java.util.UUID;

public class RestrictedAreaDTO extends CutDTO {
    public int x, y;
    public double widthFactor, heightFactor;
    public Color color;

    public RestrictedAreaDTO(UUID id, boolean isSelected, int x, int y, double widthFactor, double heightFactor, Color color) {
        super(id, isSelected);
        this.x = x;
        this.y = y;
        this.widthFactor = widthFactor;
        this.heightFactor = heightFactor;
        this.color = color;
    }

    /*public RestrictedArea toDomain()
    {
        return new RestrictedArea(x, y, widthFactor, heightFactor, color);
    }*/

    @Override
    public RestrictedArea toCut() {
        return new RestrictedArea(id, isSelected, x, y, widthFactor, heightFactor, color);
    }

    /*@Override
    public InteractiveEntity toInteractiveEntity() {
        return toCut();
    }*/
}