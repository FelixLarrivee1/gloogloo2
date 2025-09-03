package org.equipe39.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.equipe39.domain.Cut.VerticalLine;
import org.equipe39.domain.InteractiveEntity;
import org.equipe39.domain.Outil;

import java.awt.Color;
import java.util.UUID;

public class VerticalLineDTO extends CutDTO {
    public double x;
    public Color color;
    public double profondeur;
    public OutilDTO tool;

    @JsonCreator
    public VerticalLineDTO(@JsonProperty("id") UUID id,
                           @JsonProperty("isSelected") boolean isSelected,
                           @JsonProperty("x") double x,
                           @JsonProperty("profondeur") double profondeur,
                           @JsonProperty("tool") OutilDTO tool) {
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
