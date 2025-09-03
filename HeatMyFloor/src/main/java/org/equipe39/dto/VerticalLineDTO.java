package org.equipe39.dto;



import org.equipe39.domain.Cut.VerticalLine;

import java.awt.*;
import java.util.UUID;

public class VerticalLineDTO extends CutDTO {
    public double x;
    public Color color;
    public double profondeur;
    public OutilDTO tool;

    
    public VerticalLineDTO( UUID id,
                            boolean isSelected,
                            double x,
                            double profondeur,
                           OutilDTO tool) {
        super(id, isSelected);
        this.x = x;
        this.profondeur = profondeur;
        this.tool = tool;
    }

    @Override
    public VerticalLine toCut()
    {
        return new VerticalLine(id, isSelected, x, profondeur, tool.toOutil());
    }

    //toShortString
    /*@Override
    public String toString() {
        return "VerticalLineDTO{" +
                "x=" + x +
                ", color=" + color +
                ", profondeur=" + profondeur +
                ", tool=" + tool +
                '}';
    }*/

    @Override
    public String toString() {
        return "VerticalLineDTO{" +
                "id: " + this.id +
                '}';
    }
}
