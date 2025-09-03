package org.equipe39.dto;

//import com.fasterxml.jackson.annotation.JsonCreator;
//import com.fasterxml.jackson.annotation.JsonProperty;
import org.equipe39.domain.Cut.Bordure;
import org.equipe39.domain.Cut.Cut;

import java.util.UUID;


public class BordureDTO extends CutDTO {
    public double x, y;
    public double widthFactor, heightFactor;
    public OutilDTO tool;
    public double profondeur;
    public PointReferenceDTO referencePoint;

    
    public BordureDTO( UUID id,
                       boolean isSelected,
                       double x,
                       double y,
                       double widthFactor,
                      double heightFactor,
                      OutilDTO tool,
                       double profondeur) {
        super(id, isSelected);
        this.x = x;
        this.y = y;
        this.widthFactor = widthFactor;
        this.heightFactor = heightFactor;
        this.tool = tool;
        this.profondeur = profondeur;
    }

    @Override
    public String toString() {
        return "BordureDTO{" +
                "id: " + this.id +
                '}';
    }
    //toCut
    public Cut toCut() {
        return new Bordure(id, isSelected, x, y, widthFactor, heightFactor, tool.toOutil(), profondeur);
    }
}