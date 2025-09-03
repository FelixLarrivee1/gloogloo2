package org.dto;


import org.equipe45.domain.Cut.VerticalLine;
import org.equipe45.domain.InteractiveEntity;

import java.awt.Color;
import java.util.UUID;

public class VerticalLineDTO extends CutDTO {
    public int x;
    public Color color;
    public int thickness;
    public UUID tool;

    public VerticalLineDTO(UUID id, boolean isSelected, int x, Color color, int thickness, UUID tool) {
        super(id, isSelected);
        this.x = x;
        this.color = color;
        this.thickness = thickness;
        this.tool = tool;
    }

//    public VerticalLine toVerticalLine()
//    {
//        return new VerticalLine(x, color, thickness, tool);
//    }

    @Override
    public VerticalLine toCut()
    {
        return new VerticalLine(id, isSelected, x, color, thickness, tool);
    }

    /*@Override
    public InteractiveEntity toInteractiveEntity() {
        return toCut();
    }*/
}