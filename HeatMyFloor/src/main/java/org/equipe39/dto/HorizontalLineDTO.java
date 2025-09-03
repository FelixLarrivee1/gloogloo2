package org.equipe39.dto;



import org.equipe39.domain.Cut.HorizontalLine;

import java.util.UUID;

public class HorizontalLineDTO extends CutDTO {
    public double y;
    public double profondeur;
    public OutilDTO tool;


    
    public HorizontalLineDTO( UUID id,
                              boolean isSelected,
                              double y,
                              double profondeur,
                             OutilDTO tool) {
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