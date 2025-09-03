package org.dto;


import org.equipe45.domain.Cut.HorizontalLine;
import org.equipe45.domain.InteractiveEntity;

import java.awt.Color;
import java.util.UUID;

public class HorizontalLineDTO extends CutDTO {
    public int y;
    public Color color;
    public int thickness;
    public UUID tool;

    public HorizontalLineDTO(UUID id, boolean isSelected, int y, Color color, int thickness, UUID tool) {
        super(id, isSelected);
        this.y = y;
        this.color = color;
        this.thickness = thickness;
        this.tool = tool;
    }

    //int y, Color color, int thickness, UUID tool
    @Override
    public HorizontalLine toCut()
    {
        return new HorizontalLine(id, isSelected, y, color, thickness, tool);
    }

    /*@Override
    public InteractiveEntity toInteractiveEntity() {
        return toCut();
    }*/
}