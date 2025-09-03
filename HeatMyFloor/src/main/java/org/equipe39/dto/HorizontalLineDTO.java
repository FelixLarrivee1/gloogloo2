package org.equipe39.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.equipe39.domain.Cut.HorizontalLine;
import org.equipe39.domain.InteractiveEntity;
import org.equipe39.domain.Outil;

import java.awt.Color;
import java.util.UUID;

public class HorizontalLineDTO extends CutDTO {
    public double y;
    public double profondeur;
    public OutilDTO tool;


    @JsonCreator
    public HorizontalLineDTO(@JsonProperty("id") UUID id,
                             @JsonProperty("isSelected") boolean isSelected,
                             @JsonProperty("y") double y,
                             @JsonProperty("profondeur") double profondeur,
                             @JsonProperty("tool") OutilDTO tool) {
        super(id, isSelected);
        this.y = y;
        this.profondeur = profondeur;
        this.tool = tool;
    }


    @Override
    public String toString() {
        return "HorizontalLineDTO{" +
                "id: " + this.id +
                '}';
    }
    //double y, Color color, double profondeur, UUID tool
    @Override
    public HorizontalLine toCut()
    {
        //TODO WHAT????????
        return new HorizontalLine(id, isSelected, y, profondeur, tool.toOutil()/*, 14*/);
    }

    /*@Override
    public InteractiveEntity toInteractiveEntity() {
        return toCut();
    }*/
}